/*
 * Copyright (C) 2012 Helsingfors Segelklubb ry
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package fi.hoski.datastore;

import com.google.appengine.api.datastore.*;
import fi.hoski.datastore.repository.*;
import fi.hoski.datastore.repository.Event.EventType;
import fi.hoski.mail.MailService;
import fi.hoski.mail.MailServiceImpl;
import fi.hoski.util.Day;
import fi.hoski.util.EntityReferences;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * @author Timo Vesalainen
 */
public class EventsImpl implements Events
{
    public static final ResourceBundle resourceBundle = ResourceBundle.getBundle("fi/hoski/datastore/repository/fields");
    private DatastoreService datastore;
    private DSUtils entities;
    private MailService mailService;
    
    public EventsImpl()
    {
        this.datastore = DatastoreServiceFactory.getDatastoreService();
        this.entities = new DSUtilsImpl(datastore);
        this.mailService = new MailServiceImpl();
    }

    public EventsImpl(DatastoreService datastore, DSUtils entities)
    {
        this.datastore = datastore;
        this.entities = entities;
        this.mailService = new MailServiceImpl();
    }

    @Override
    public List<Event> getEvents(Event.EventType type)
    {
        return getEvents(type, null, Integer.MAX_VALUE);
    }
    @Override
    public List<Event> getEvents(EventType type, Day from, Integer cnt)
    {
        int count = 3;
        if (cnt != null)
        {
            count = cnt;
        }
        Key typeKey = Keys.getTypeKey(new Day(), type);
        Query query = new Query(Event.KIND);
        query.setAncestor(typeKey);
        if (from != null)
        {
            query.addFilter(Event.EVENTDATE, Query.FilterOperator.GREATER_THAN, from.getValue());
        }
        query.addSort(Event.EVENTDATE);
        PreparedQuery preparedQuery = datastore.prepare(query);
        List<Event> eventList = Event.create(preparedQuery.asList(FetchOptions.Builder.withLimit(count)));
        for (Event event : eventList)
        {
            int childCount = childCount(event);
            event.setChildCount(childCount);
        }
        return eventList;
    }

    @Override
    public void deleteEvents(List<Event> events)
    {
        Day now = new Day();
        List<Key> keyList = new ArrayList<Key>();
        for (Event event : events)
        {
            Day eventDate = (Day) event.get(Event.EVENTDATE);
            Key key = event.createKey();
            // if event is over we are allowed to remove it and all childs
            if (now.after(eventDate))
            {
                keyList.add(key);
                for (Entity child : getChilds(key))
                {
                    keyList.add(child.getKey());
                }
            }
            else
            {
                // we can remove it, if no childs (reservations) exists
                if (!hasChilds(key))
                {
                    keyList.add(key);
                }
            }
        }
        datastore.delete(keyList);
    }

    @Override
    public void createReservation(Event event, Reservation reservation, boolean replace) throws EntityNotFoundException, EventFullException, DoubleBookingException, BoatNotFoundException, MandatoryPropertyMissingException
    {
        reservation.setParent(event);
        createReservation(reservation, replace);
    }
    @Override
    public void createReservation(Reservation reservation, boolean replace) throws EntityNotFoundException, EventFullException, DoubleBookingException, BoatNotFoundException, MandatoryPropertyMissingException
    {
        Key boatKey = (Key) reservation.get(Reservation.BOAT);
        if (boatKey == null)
        {
            throw new BoatNotFoundException("boat not found");
        }
        Transaction tr = datastore.beginTransaction();
        try
        {
            // handle existing reservations of same type
            Query query = new Query(Reservation.KIND);
            query.setAncestor(Keys.getTypeKey(new Day(), reservation.getEventType()));
            query.addFilter(Reservation.BOAT, Query.FilterOperator.EQUAL, reservation.get(Reservation.BOAT));
            PreparedQuery prepared = datastore.prepare(query);
            if (replace)
            {
                for (Entity entity : prepared.asIterable())
                {
                    datastore.delete(entity.getKey());
                }
            }
            else
            {
                for (Entity entity : prepared.asIterable())
                {
                    Reservation or = (Reservation) entities.newInstance(entity);
                    Event event = or.getEvent();
                    if (event.isClosed())
                    {
                        throw new DoubleBookingException(reservation.toString());
                    }
                    else
                    {
                        datastore.delete(entity.getKey());
                    }
                }
            }
            Event event = reservation.getEvent();
            Key eventKey = event.createKey();
            long count = childCount(eventKey);
            long maxCount = (Long) event.get(Event.MAXENTRIES);
            if (count >= maxCount)
            {
                throw new EventFullException(reservation.toString());
            }
            String firstMandatoryNullProperty = reservation.firstMandatoryNullProperty();
            if (firstMandatoryNullProperty != null)
            {
                throw new MandatoryPropertyMissingException(firstMandatoryNullProperty);
            }
            entities.put(reservation);
            tr.commit();
            String recipient = (String) reservation.get(Reservation.EMAIL);
            if (recipient != null)
            {
                Key msgKey = KeyFactory.createKey(Keys.getRootKey(), Messages.KIND, Messages.NAME);
                Entity msgEntity = datastore.get(msgKey);
                String sender = (String) msgEntity.getProperty(Messages.PASSWORDFROMADDRESS);
                String messageSubject = (String) msgEntity.getProperty(Messages.RESERVATIONMESSAGESUBJECT);
                Text messageText = (Text) msgEntity.getProperty(Messages.RESERVATIONMESSAGEBODY);
                Day date = (Day) event.get(Event.EVENTDATE);
                EventType eventType = reservation.getEventType();
                String eventStr = resourceBundle.getString(eventType.name());
                String message = String.format(messageText.getValue(), date.toString(), eventStr.toLowerCase());
                try
                {
                    InternetAddress senderAddress = new InternetAddress(sender);
                    InternetAddress recipientAddress = new InternetAddress(recipient);
                    mailService.sendMail(senderAddress, messageSubject, message, null, recipientAddress);
                }
                catch (AddressException ex)
                {
                    throw new IllegalArgumentException(ex);
                }
            }
        }
        finally
        {
            if (tr.isActive())
            {
                tr.rollback();
            }
        }
    }

    private boolean hasChilds(Key parent)
    {
        Iterable<Entity> it = getChilds(parent);
        return it.iterator().hasNext();
    }

    private List<Entity> getChilds(Key parent)
    {
        Query query = new Query();
        query.setAncestor(parent);
        query.addFilter(Entity.KEY_RESERVED_PROPERTY, Query.FilterOperator.NOT_EQUAL, parent);
        PreparedQuery preparedQuery = datastore.prepare(query);
        return preparedQuery.asList(FetchOptions.Builder.withDefaults());
    }

    private int childCount(Key parent)
    {
        Query query = new Query();
        query.setAncestor(parent);
        query.addFilter(Entity.KEY_RESERVED_PROPERTY, Query.FilterOperator.NOT_EQUAL, parent);
        PreparedQuery preparedQuery = datastore.prepare(query);
        return preparedQuery.countEntities(FetchOptions.Builder.withDefaults());
    }

    @Override
    public int childCount(Event event)
    {
        Key key = event.createKey();
        return childCount(key);
    }

    @Override
    public boolean hasChilds(Event event)
    {
        Key key = event.createKey();
        return hasChilds(key);
    }

    @Override
    public Options<Event> getEventSelection(EventType eventType)
    {
        Options<Event> selection = new Options<Event>();
        for (Event event : getEvents(eventType))
        {
            String label = getEventLabel(event);
            if (selection.getSelection() == null)
            {
                selection.setSelection(event);
            }
            selection.addItem(label, event);
        }
        return selection;
    }

    public String getEventLabel(Event event)
    {
        int count = 0;
        count = childCount(event);
        Day eventDate = (Day) event.get(Event.EVENTDATE);
        Day closingDate = (Day) event.get(Event.CLOSINGDATE);
        Long maxEntries = (Long) event.get(Event.MAXENTRIES);
        String notes = (String) event.get(Event.NOTES);
        if (notes == null)
        {
            notes = "";
        }
        Day now = new Day();
        if (now.after(closingDate))
        {
            String format = resourceBundle.getString("EventLabelClosed");
            return String.format(format, eventDate, EntityReferences.encode(notes), count, maxEntries, closingDate);
        }
        else
        {
            String format = resourceBundle.getString("EventLabelOpen");
            return String.format(format, eventDate, EntityReferences.encode(notes), count, maxEntries, closingDate);
        }
    }
    @Override
    public List<Reservation> getReservations(Event event)
    {
        List<Reservation> rlist = new ArrayList<Reservation>();
        Key eventKey = event.createKey();
        Query query = new Query(Reservation.KIND);
        query.setAncestor(eventKey);
        query.addSort(Reservation.ORDER);
        PreparedQuery prepared = datastore.prepare(query);
        for (Entity e : prepared.asIterable())
        {
            rlist.add(new Reservation(event, e));
        }
        return rlist;
    }

    @Override
    public Event getEvent(EventType type, long id) throws EntityNotFoundException
    {
        Key typeKey = Keys.getTypeKey(new Day(), type);
        Key key = KeyFactory.createKey(typeKey, Event.KIND, id);
        Entity entity = datastore.get(key);
        return new Event(entity);
    }

    @Override
    public Event getEvent(String eventKey) throws EntityNotFoundException
    {
        Key key = KeyFactory.stringToKey(eventKey);
        Entity entity = datastore.get(key);
        return new Event(entity);
    }

    @Override
    public void updateInspection(List<Reservation> reservations)
    {
        Day now = new Day();
        int yearNow = now.getYear();
        for (Reservation reservation : reservations)
        {
            EventType eventType = reservation.getEventType();
            assert Event.isInspection(eventType);
            Transaction tr = datastore.beginTransaction();
            try
            {
                // is it stored yet for this year
                Query iq = new Query(Repository.KATSASTUSTIEDOT);
                iq.addFilter(Repository.VENEID, Query.FilterOperator.EQUAL, reservation.get(Reservation.BOAT));
                iq.addSort(Repository.PAIVA, Query.SortDirection.DESCENDING);
                PreparedQuery ip = datastore.prepare(iq);
                Entity entity = null;
                Iterator<Entity> it = ip.asIterator();
                if (it.hasNext())
                {
                    Entity e = it.next();
                    Day date = Day.getDay(e.getProperty(Repository.PAIVA));
                    if (date != null)
                    {
                        int y = date.getYear();
                        if (yearNow == y)
                        {
                            entity = e;
                        }
                    }
                }
                Boolean inspectedObj = (Boolean) reservation.get(Reservation.INSPECTED);
                boolean inspected = false;
                if (inspectedObj != null)
                {
                    inspected = inspectedObj;
                }
                if (inspected)
                {
                    Key root = entities.getRootKey();
                    if (entity == null)
                    {
                        entity = new Entity(Repository.KATSASTUSTIEDOT, root);
                        Key nk = datastore.put(entity);
                        entity.setUnindexedProperty(Repository.ID, nk.getId());
                    }
                    Inspection inspection = new Inspection(entity);
                    inspection.setAll(reservation.getAll());
                    inspection.set(Repository.PAIVA, now);
                    Boolean basic = (Boolean) reservation.get(Reservation.BASICINSPECTION);
                    if (basic != null && basic)
                    {
                        inspection.set(Repository.KATSASTUSTYYPPI, KeyFactory.createKey(root, Repository.KATSASTUSTYYPIT, 1));
                    }
                    else
                    {
                        inspection.set(Repository.KATSASTUSTYYPPI, KeyFactory.createKey(root, Repository.KATSASTUSTYYPIT, 2));
                    }
                    assert inspection.firstMandatoryNullProperty() == null;
                    datastore.put(entity);
                }
                else
                {
                    if (entity != null)
                    {
                        datastore.delete(entity.getKey());
                    }
                }
                entities.put(reservation);
                tr.commit();
            }
            finally
            {
                if (tr.isActive())
                {
                    tr.rollback();
                }
            }
        }
    }

}

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
package fi.hoski.datastore.repository;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.*;
import fi.hoski.datastore.DSUtils;
import fi.hoski.datastore.Events;
import fi.hoski.datastore.Races;
import fi.hoski.datastore.Repository;
import fi.hoski.util.BankingBarcode;
import fi.hoski.util.Day;
import fi.hoski.util.Time;
import fi.hoski.util.code128.CharNotInCodesetException;
import fi.hoski.util.code128.Code128;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Timo Vesalainen
 */
public class KeyInfo
{

    public static final ResourceBundle repositoryBundle = ResourceBundle.getBundle("fi/hoski/datastore/repository/fields");
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    private DSUtils entities;
    private Events events;
    private Races races;
    private String parameters;
    private Key key;
    private boolean authenticated;
    private String label;
    private String menuLink;
    private String editLink;

    public KeyInfo(
            DSUtils entities,
            Events events,
            Races races,
            String parameters,
            Key key,
            boolean authenticated) throws EntityNotFoundException
    {
        this.entities = entities;
        this.events = events;
        this.races = races;
        this.parameters = parameters;
        this.key = key;
        this.authenticated = authenticated;
        init(key);
    }

    private void init(Key key) throws EntityNotFoundException
    {
        Key prnt = key.getParent();
        if (prnt != null)
        {
            init(prnt);
        }
        label = key.toString(); // safe values
        menuLink = label;
        editLink = label;
        if (Repository.YEAR.equals(key.getKind()))
        {
            label = /*repositoryBundle.getString(key.getKind()) + " " + */""+key.getId();
        }
        if (Attachment.KIND.equals(key.getKind()))
        {
            Entity attachment = entities.get(key);
            label = (String) attachment.getProperty(Attachment.TITLE);
            Link link = (Link) attachment.getProperty(Attachment.URL);
            menuLink = "<a href=\""+link.getValue()+"\">"+label+"</a>";
            editLink = menuLink;
        }
        if (Repository.RACESERIES.equals(key.getKind()))
        {
            Entity rsEntity = entities.get(key);
            RaceSeries raceSeries = new RaceSeries(rsEntity);
            String event = (String) raceSeries.get(RaceSeries.EVENT);
            Day eventDate = (Day) raceSeries.get(RaceSeries.EventDate);
            Day closingDate = (Day) raceSeries.get(RaceSeries.ClosingDate);
            boolean closed = closed(closingDate);
            Day to = (Day) raceSeries.get(RaceSeries.TO);
            if (to == null)
            {
                to = eventDate;
            }
            String eventDateString = null;
            if (eventDate.equals(to))
            {
                eventDateString = eventDate.toString();
            }
            else
            {
                eventDateString = eventDate.toString() + "&ndash;" + to.toString();
            }
            String format = null;
            if (closed)
            {
                format = repositoryBundle.getString("RaceSeriesClosing");
            }
            else
            {
                format = repositoryBundle.getString("RaceSeriesOpen");
            }
            label = String.format(format, event, eventDateString, closingDate);
        }
        if (Repository.RACEFLEET.equals(key.getKind()))
        {
            Entity rfEntity = entities.get(key);
            RaceFleet raceFleet = (RaceFleet) entities.newInstance(rfEntity);
            RaceSeries raceSeries = raceFleet.getRaceSeries();
            String fleet = (String) raceFleet.get(RaceFleet.Fleet);
            Day eventDate = (Day) raceFleet.get(RaceFleet.EventDate);
            Day closingDate = (Day) raceFleet.get(RaceFleet.ClosingDate);
            boolean closed = closed(closingDate);
            Time time = (Time) raceFleet.get(RaceFleet.StartTime);
            Boolean ranking = (Boolean) raceFleet.get(RaceFleet.Ranking);
            String format = null;
            if (time != null)
            {
                if (ranking != null && ranking)
                {
                    if (closed)
                    {
                        format = repositoryBundle.getString("RankingStartClosing");
                    }
                    else
                    {
                        format = repositoryBundle.getString("RankingStartOpen");
                    }
                }
                else
                {
                    if (closed)
                    {
                        format = repositoryBundle.getString("RaceStartClosing");
                    }
                    else
                    {
                        format = repositoryBundle.getString("RaceStartOpen");
                    }
                }
            }
            else
            {
                if (closed)
                {
                    format = repositoryBundle.getString("RaceFleetClosing");
                }
                else
                {
                    format = repositoryBundle.getString("RaceFleetOpen");
                }
            }
            label = String.format(format, fleet, eventDate, closingDate, time);
            String page = (String) raceSeries.get(RaceSeries.PAGE);
            if (page == null)
            {
                String ratingSystem = (String) raceFleet.get(RaceFleet.RatingSystem);
                page = ratingSystem.toLowerCase() + ".html";
            }
            editLink = "<a href=\"" + page+"?ancestor=" + KeyFactory.keyToString(key) + "\" >" + label + "</a>";
            menuLink = "<a href=\"list?ancestor=" + KeyFactory.keyToString(key) + "&" + parameters + "\" >" + label + "</a>";
        }
        if (authenticated)
        {
            if (Repository.EVENTTYPE.equals(key.getKind()))
            {
                Event.EventType eventType = Event.EventType.values()[(int) key.getId() - 1];
                label = repositoryBundle.getString(eventType.name());
            }
            if (Repository.EVENT.equals(key.getKind()))
            {
                Key parent = key.getParent();
                Event.EventType eventType = Event.EventType.values()[(int) parent.getId() - 1];
                Entity eventEntity = entities.get(key);
                Event event = new Event(eventEntity);
                Day day = (Day) event.get(Event.EventDate);
                label = events.getEventLabel(event);
                editLink = "<a href=\"" + eventType.name().toLowerCase() + ".html?ancestor=" + KeyFactory.keyToString(key) + "&startDate=" + day.getDate().getTime() + "\" >" + label + "</a>";
                menuLink = "<a href=\"list?ancestor=" + KeyFactory.keyToString(key) + "&" + parameters + "\" >" + label + "</a>";
            }
        }
    }

    private boolean closed(Day closingDay)
    {
        if (closingDay != null)
        {
            Day now = new Day();
            if (now.getYear() == closingDay.getYear())
            {
                return now.after(closingDay);
            }
        }
        return false;
    }
    public Map<String, Object> getMap() throws EntityNotFoundException
    {
        Map<String, Object> map = new HashMap<String, Object>();
        populateMap(map, "", key);
        return map;
    }

    private String populateMap(Map<String, Object> map, String prefix, Key key) throws EntityNotFoundException
    {
        Key prnt = key.getParent();
        if (prnt != null)
        {
            String l = populateMap(map, prnt.getKind() + ".", prnt);
            map.put(prefix + "Label", l);
        }
        String lab = key.toString(); // safe values
        if (Title.KIND.equals(key.getKind()))
        {
            List<Attachment> attList = new ArrayList<Attachment>();
            attList.addAll(entities.getAttachmentsFor(key));
            Collections.sort(attList);
            List<String> attachmentList = new ArrayList<String>();
            for (Attachment att : attList)
            {
                attachmentList.add(att.getAnchor());
            }
            map.put(prefix + "Attachments", attachmentList);
        }
        if (Repository.YEAR.equals(key.getKind()))
        {
            lab = /*repositoryBundle.getString(key.getKind()) + " " +*/""+ key.getId();
            map.put(prefix + "Year", key.getId());
        }
        if (Repository.RACESERIES.equals(key.getKind()))
        {
            Entity rsEntity = entities.get(key);
            RaceSeries raceSeries = new RaceSeries(rsEntity);
            for (String property : raceSeries.getModel().getProperties())
            {
                map.put(prefix + property, raceSeries.get(property));
            }
            String event = (String) raceSeries.get(RaceSeries.EVENT);
            Day eventDate = (Day) raceSeries.get(RaceSeries.EventDate);
            Day closingDate = (Day) raceSeries.get(RaceSeries.ClosingDate);
            boolean closed = false;
            if (closingDate != null)
            {
                Day now = new Day();
                closed = now.after(closingDate);
            }
            Day to = (Day) raceSeries.get(RaceSeries.TO);
            if (to == null)
            {
                to = eventDate;
            }
            String eventDateString = null;
            if (eventDate.equals(to))
            {
                eventDateString = eventDate.toString();
            }
            else
            {
                eventDateString = eventDate.toString() + "&ndash;" + to.toString();
            }
            map.put(prefix + "FromTo", eventDateString);
            String format = null;
            if (closed)
            {
                format = repositoryBundle.getString("RaceSeriesClosing");
            }
            else
            {
                format = repositoryBundle.getString("RaceSeriesOpen");
            }
            lab = String.format(format, event, eventDateString, closingDate);
            //map.put(prefix+"RaceEvent", event);
            map.put(prefix + "RaceEventFrom", eventDate.toString());
            map.put(prefix + "RaceEventTo", to.toString());
        }
        if (Repository.RACEFLEET.equals(key.getKind()))
        {
            Entity rfEntity = entities.get(key);
            map.put("RaceFleetKey", KeyFactory.keyToString(key));
            map.putAll(rfEntity.getProperties());
            RaceFleet raceFleet = (RaceFleet) entities.newInstance(rfEntity);
            for (String property : raceFleet.getModel().getProperties())
            {
                map.put(prefix + property, raceFleet.get(property));
            }
            String fleet = (String) raceFleet.get(RaceFleet.Fleet);
            Day eventDate = (Day) raceFleet.get(RaceFleet.EventDate);
            Day closingDate = (Day) raceFleet.get(RaceFleet.ClosingDate);
            Day closingDate2 = (Day) raceFleet.get(RaceFleet.ClosingDate2);
            if (closingDate2 == null)
            {
                closingDate2 = closingDate;
            }
            boolean closed = false;
            if (closingDate2 != null)
            {
                Day now = new Day();
                closed = now.after(closingDate2);
                map.put(prefix + "isEventClosed", closed);
            }
            Time time = (Time) raceFleet.get(RaceFleet.StartTime);
            Boolean ranking = (Boolean) raceFleet.get(RaceFleet.Ranking);
            String format = null;
            if (time != null)
            {
                if (ranking != null && ranking)
                {
                    if (closed)
                    {
                        format = repositoryBundle.getString("RankingStartClosing");
                    }
                    else
                    {
                        format = repositoryBundle.getString("RankingStartOpen");
                    }
                }
                else
                {
                    if (closed)
                    {
                        format = repositoryBundle.getString("RaceStartClosing");
                    }
                    else
                    {
                        format = repositoryBundle.getString("RaceStartOpen");
                    }
                }
            }
            else
            {
                if (closed)
                {
                    format = repositoryBundle.getString("RaceFleetClosing");
                }
                else
                {
                    format = repositoryBundle.getString("RaceFleetOpen");
                }
            }
            lab = String.format(format, fleet, eventDate, closingDate, time);
            String ratingSystem = (String) raceFleet.get(RaceFleet.RatingSystem);
            map.put(prefix + "RaceFleet", lab);
            map.put(prefix + "RaceRatingSystem", ratingSystem);
            Double fee = (Double) raceFleet.get(RaceFleet.Fee);
            if (fee != null)
            {
                map.put(prefix + "isFree", fee == 0);
            }
            // RaceSeries & RaceFleet attachments
            List<Attachment> attList = new ArrayList<Attachment>();
            attList.addAll(entities.getAttachmentsFor(key));
            attList.addAll(entities.getAttachmentsFor(key.getParent()));
            Collections.sort(attList);
            List<String> attachmentList = new ArrayList<String>();
            for (Attachment att : attList)
            {
                attachmentList.add(att.getAnchor());
            }
            map.put(prefix + "Attachments", attachmentList);
            for (Attachment.Type type : Attachment.Type.values())
            {
                String parentString = KeyFactory.keyToString(key.getParent());
                String success = "/races/upload?type=" + type.name().toLowerCase() + "&attachTo=" + parentString;
                String action = blobstoreService.createUploadUrl(success);
                map.put(prefix + "Attach" + type.name(), action);
            }
        }
        if (RaceEntry.KIND.equals(key.getKind()))
        {
            RaceEntry raceEntry = (RaceEntry) entities.newInstance(key);
            for (String property : raceEntry.getModel().getProperties())
            {
                map.put(prefix + property, raceEntry.get(property));
            }
            BankingBarcode barcode = races.getBarcode(key);
            if (barcode != null)
            {
                try
                {
                    for (Map.Entry<String, Object> e : barcode.getMetaData().entrySet())
                    {
                        map.put(e.getKey(), e.getValue());
                    }
                    char[] bars = Code128.encode(barcode.toString());
                    int width = 0;
                    for (int cc : bars)
                    {
                        width += cc;
                    }
                    map.put("barwidth", width);
                    map.put("bars", bars);
                }
                catch (CharNotInCodesetException ex)
                {
                    Logger.getLogger(KeyInfo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if (authenticated)
        {
            if (Repository.EVENTTYPE.equals(key.getKind()))
            {
                Event.EventType eventType = Event.EventType.values()[(int) key.getId() - 1];
                lab = repositoryBundle.getString(eventType.name());
                map.put(prefix + "EventType", eventType.name());
                map.put(prefix + "EventTypeName", lab);
            }
            if (Repository.EVENT.equals(key.getKind()))
            {
                map.put("event", KeyFactory.keyToString(key));
                Key parent = key.getParent();
                Event.EventType eventType = Event.EventType.values()[(int) parent.getId() - 1];
                Entity eventEntity = entities.get(key);
                map.putAll(eventEntity.getProperties());
                Event event = new Event(eventEntity);
                Day day = (Day) event.get(Event.EventDate);
                lab = events.getEventLabel(event);
                Long maxEntriesL = (Long) event.get(Event.MAXENTRIES);
                if (maxEntriesL != null)
                {
                    long maxEntries = maxEntriesL;
                    long entries = events.childCount(event);
                    map.put(prefix + "isEventFull", (entries >= maxEntries));
                }
                map.put(prefix + "isEventClosed", event.isClosed());
            }
        }
        map.put(prefix + "Label", lab);
        for (Entry<String, Object> entry : map.entrySet())
        {
            Object value = entry.getValue();
            if (value != null)
            {
                if (
                        !(value instanceof Collection) &&
                        !(value instanceof char[]) 
                        )
                {
                    if (value instanceof Double)
                    {
                        Double d = (Double) value;
                        entry.setValue(String.valueOf(d.intValue()));
                    }
                    else
                    {
                        entry.setValue(value.toString());
                    }
                }
            }
        }
        return lab;
    }

    public String getEditLink()
    {
        return editLink;
    }

    public String getLabel()
    {
        return label;
    }

    public String getMenuLink()
    {
        return menuLink;
    }
}

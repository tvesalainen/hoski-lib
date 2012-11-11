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

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import fi.hoski.datastore.Repository;
import fi.hoski.util.Day;
import java.util.*;

/**
 * @author Timo Vesalainen
 */
public class Event extends DataObject implements Comparable<Event>, Reservable
{

    public enum EventType {LAUNCH, LIFT, INSPECTION, HULL_INSPECTION};
    
    public static final String KIND = Repository.EVENT;
    
    public static final String MAXENTRIES = "EventMaxEntries";
    public static final String NOTES = "EventNotes";

    public static final DataObjectModel MODEL = new DataObjectModel(KIND);
    
    static
    {
        MODEL.property(EVENTDATE, Day.class, true, true);
        MODEL.property(CLOSINGDATE, Day.class, true, true);
        MODEL.property(MAXENTRIES, Long.class, false, true);
        MODEL.property(NOTES, String.class);
    }

    private EventType eventType;
    
    private int childCount = -1;
    
    public Event(EventType eventType)
    {
        super(MODEL, new MapData(MODEL));
        this.eventType = eventType;
    }

    public Event(Entity entity)
    {
        super(MODEL, entity);
        this.eventType = getEventType(entity.getParent());
    }

    public static boolean isInspection(EventType eventType)
    {
        switch (eventType)
        {
            case LAUNCH:
            case LIFT:
                return false;
            case INSPECTION:
            case HULL_INSPECTION:
                return true;
            default:
                throw new IllegalArgumentException(eventType+" unknown");
        }
    }

    public void setEventType(EventType eventType)
    {
        this.eventType = eventType;
    }
    
    public int getChildCount()
    {
        assert childCount != -1;
        return childCount;
    }

    public void setChildCount(int childCount)
    {
        this.childCount = childCount;
    }
    
    public boolean isFull()
    {
        assert childCount != -1;
        Long max = (Long) get(MAXENTRIES);
        return childCount >= max;
    }
    public boolean isClosed()
    {
        Day closingDate = (Day) get(Event.CLOSINGDATE);
        if (closingDate != null)
        {
            Day now = new Day();
            return now.after(closingDate);
        }
        return false;
    }
    /**
     * Returns current year from EventType, Event or Reservation keys
     * @param key
     * @return 
     */
    public static long getYear(Key key)
    {
        return findAncestor(Repository.YEAR, key).getId();
    }
    /**
     * Returns current EventType from EventType, Event or Reservation keys
     * @param key
     * @return 
     */
    public static EventType getEventType(Key key)
    {
        Key ek = findAncestor(Repository.EVENTTYPE, key);
        return EventType.values()[(int)ek.getId()-1];
    }
    /**
     * Returns current Event Date from Event or Reservation keys
     * @param key
     * @return 
     */
    public static Date getEventDate(Key key)
    {
        Key ek = findAncestor(Repository.EVENT, key);
        return new Date(ek.getId());
    }
    private static Key findAncestor(String kind, Key key)
    {
        if (kind.equals(key.getKind()))
        {
            return key;
        }
        else
        {
            Key parent = key.getParent();
            if (parent == null)
            {
                throw new IllegalArgumentException(key+" cannot be resolved to "+kind);
            }
            else
            {
                return findAncestor(kind, parent);
            }
        }
    }
    @Override
    protected Object getDefault(String property)
    {
        if (CLOSINGDATE.equals(property))
        {
            Date date = (Date) get(EVENTDATE);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DAY_OF_YEAR, -7);
            return cal.getTime();
        }
        if (MAXENTRIES.equals(property))
        {
            return 999;
        }
        return super.getDefault(property);
    }

    public static List<Event> create(List<Entity> el)
    {
        List<Event> list = new ArrayList<Event>();
        for (Entity e : el)
        {
            if (!KIND.equals(e.getKind()))
            {
                throw new IllegalArgumentException("wrong kind "+e);
            }
            Event ev = new Event(e);
            list.add(ev);
        }
        Collections.sort(list);
        return list;
    }
    
    public EventType getEventType()
    {
        return eventType;
    }

    public Long getUid()
    {
        Day date = (Day) get(EVENTDATE);
        return date.getValue();
    }
    @Override
    public Key createKey()
    {
        Key typeKey = getTypeKey();
        return KeyFactory.createKey(typeKey, getKind(), getUid());
    }
    public Key getTypeKey()
    {
        Day eventDate = (Day) get(Event.EVENTDATE);
        return Keys.getTypeKey(eventDate, getEventType());
    }
    @Override
    public int compareTo(Event o)
    {
        Day d1 = (Day) get(EVENTDATE);
        Day d2 = (Day) o.get(EVENTDATE);
        return d1.compareTo(d2);
    }

    @Override
    public String toString()
    {
        Day date = (Day) get(EVENTDATE);
        return date.toString();
    }
}

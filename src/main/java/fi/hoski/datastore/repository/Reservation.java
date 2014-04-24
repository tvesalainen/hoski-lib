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
import com.google.appengine.api.datastore.Text;
import fi.hoski.datastore.Repository;
import fi.hoski.datastore.repository.Event.EventType;

/**
 * @author Timo Vesalainen
 */
public class Reservation extends DataObject implements Comparable<Reservation>
{
    public static final String KIND = "Reservation";
    
    public static final String BOAT = Repository.VENEID;
    public static final String ORDER = "Order";
    public static final String INSPECTED = "Inspected";
    public static final String INSPECTOR = Repository.KATSASTAJA;
    public static final String LASTNAME = "Jasenet.Sukunimi";
    public static final String FIRSTNAME = Repository.FIRSTNAME;
    public static final String BOATNAME = "Veneet.Nimi";
    public static final String MOBILEPHONE = "Jasenet.Mobile";
    public static final String EMAIL = "Jasenet.Email";
    public static final String NOTES = "Notes";

    public static final String BOATTYPE = "Venetyyppit.Tyyppi";
    public static final String DOCK = "Laiturit.Tunnus";
    public static final String DOCKNUMBER = "Laituripaikat.Paikka";
    public static final String HULLINSPECTION = "HullInspection";
    
    public static final String BASICINSPECTION = "BasicInspection";
    
    public static final String DOCKYARDPLACE = "Veneet.TalvipaikkaNo";
    
    public static final String WEIGHT = "Veneet.Paino";
    public static final String LENGTH = "Veneet.Pituus";

    public static final String CREATOR = Repository.CREATOR;
    
    public static final String INSPECTION_CLASS = Repository.KATSASTUSLUOKKA;
    public static final String INSPECTION_GASS = Repository.KAASU;
    
    public static final DataObjectModel BASE_MODEL = new DataObjectModel(KIND);
    public static final DataObjectModel HULL_INSPECTION_MODEL = new DataObjectModel(KIND);
    public static final DataObjectModel INSPECTION_MODEL = new DataObjectModel(KIND);
    public static final DataObjectModel LAUNCH_MODEL = new DataObjectModel(KIND);
    public static final DataObjectModel LIFT_MODEL = new DataObjectModel(KIND);
    
    static
    {
        BASE_MODEL.property(BOAT, Key.class, true);
        BASE_MODEL.property(ORDER, Long.class, true, false, 0L);
        BASE_MODEL.property(LASTNAME, String.class, false, true);
        BASE_MODEL.property(FIRSTNAME, String.class, false, true);
        BASE_MODEL.property(BOATNAME, String.class, false);
        BASE_MODEL.property(DOCKYARDPLACE, Long.class, false);
        BASE_MODEL.property(MOBILEPHONE, String.class);
        BASE_MODEL.property(EMAIL, String.class);
        BASE_MODEL.property(NOTES, Text.class);
        BASE_MODEL.property(CREATOR);
        
        LAUNCH_MODEL.property(BOAT, Key.class, true, true);
        LAUNCH_MODEL.property(ORDER, Long.class, true, false, 0L);
        LAUNCH_MODEL.property(LASTNAME, String.class, false, true);
        LAUNCH_MODEL.property(FIRSTNAME, String.class, false, true);
        LAUNCH_MODEL.property(BOATNAME, String.class, false);
        LAUNCH_MODEL.property(WEIGHT, Double.class, false, true);
        LAUNCH_MODEL.property(DOCKYARDPLACE, Long.class, false, true);
        LAUNCH_MODEL.property(MOBILEPHONE, String.class);
        LAUNCH_MODEL.property(EMAIL, String.class);
        LAUNCH_MODEL.property(NOTES, Text.class);
        LAUNCH_MODEL.property(CREATOR);
        
        LIFT_MODEL.property(BOAT, Key.class, true, true);
        LIFT_MODEL.property(ORDER, Long.class, true, false, 0L);
        LIFT_MODEL.property(LASTNAME, String.class, false, true);
        LIFT_MODEL.property(FIRSTNAME, String.class, false, true);
        LIFT_MODEL.property(BOATNAME, String.class, false);
        LIFT_MODEL.property(WEIGHT, Double.class, false, true);
        LIFT_MODEL.property(LENGTH, Double.class, false, true);
        LIFT_MODEL.property(DOCKYARDPLACE, Long.class, false, true);
        LIFT_MODEL.property(MOBILEPHONE, String.class);
        LIFT_MODEL.property(EMAIL, String.class);
        LIFT_MODEL.property(NOTES, Text.class);
        LIFT_MODEL.property(CREATOR);
        
        INSPECTION_MODEL.property(BOAT, Key.class, true, true);
        INSPECTION_MODEL.property(ORDER, Long.class, true, false, 0L);
        INSPECTION_MODEL.property(INSPECTED, Boolean.class, false, false);
        INSPECTION_MODEL.property(LASTNAME, String.class, false, true);
        INSPECTION_MODEL.property(FIRSTNAME, String.class, false, true);
        INSPECTION_MODEL.property(BOATNAME, String.class, false);
        INSPECTION_MODEL.property(MOBILEPHONE, String.class);
        INSPECTION_MODEL.property(EMAIL, String.class);
        INSPECTION_MODEL.property(BOATTYPE, String.class, false, true);
        INSPECTION_MODEL.property(DOCK);
        INSPECTION_MODEL.property(DOCKNUMBER, Long.class);
        INSPECTION_MODEL.property(INSPECTION_CLASS, Long.class);
        INSPECTION_MODEL.property(INSPECTION_GASS, Boolean.class);
        INSPECTION_MODEL.property(BASICINSPECTION, Boolean.class);
        INSPECTION_MODEL.property(NOTES, Text.class);
        INSPECTION_MODEL.property(INSPECTOR, Long.class);
        INSPECTION_MODEL.property(CREATOR);
        
        HULL_INSPECTION_MODEL.property(BOAT, Key.class, true, true);
        HULL_INSPECTION_MODEL.property(ORDER, Long.class, true, false, 0L);
        HULL_INSPECTION_MODEL.property(INSPECTED, Boolean.class, false, false);
        HULL_INSPECTION_MODEL.property(LASTNAME, String.class, false, true);
        HULL_INSPECTION_MODEL.property(FIRSTNAME, String.class, false, true);
        HULL_INSPECTION_MODEL.property(BOATNAME, String.class, false);
        HULL_INSPECTION_MODEL.property(DOCKYARDPLACE, Long.class, false, true);
        HULL_INSPECTION_MODEL.property(MOBILEPHONE, String.class);
        HULL_INSPECTION_MODEL.property(EMAIL, String.class);
        HULL_INSPECTION_MODEL.property(BOATTYPE, String.class, false, true);
        HULL_INSPECTION_MODEL.property(NOTES, Text.class);
        HULL_INSPECTION_MODEL.property(INSPECTOR, Long.class);
        HULL_INSPECTION_MODEL.property(CREATOR);
    }

    private EventType eventType;
    
    public Reservation(Event event)
    {
        this(event.getEventType(), getModel(event.getEventType()));
        parent = event;
        this.eventType = event.getEventType();
    }

    public Reservation(Event event, Entity entity)
    {
        this(event.getEventType(), entity);
        parent = event;
        this.eventType = event.getEventType();
    }

    private Reservation(EventType eventType, DataObjectModel model)
    {
        super(model, new MapData(model));
        this.eventType = eventType;
    }

    private Reservation(EventType eventType, Entity entity)
    {
        super(getModel(eventType), entity);
        this.eventType = eventType;
    }

    private Reservation(EventType eventType, DataObjectModel model, DataObjectData data, DataObject parent)
    {
        super(model, data);
        this.eventType = eventType;
        this.parent = parent;
    }

    @Override
    public Reservation clone()
    {
        return new Reservation(eventType, model.clone(), data.clone(), parent.clone());
    }

    public Event getEvent()
    {
        return (Event) parent;
    }
    
    public EventType getEventType()
    {
        return eventType;
    }
    
    public static DataObjectModel getModel(EventType eventType)
    {
        switch (eventType)
        {
            case LAUNCH:
                return LAUNCH_MODEL;
            case LIFT:
                return LIFT_MODEL;
            case INSPECTION:
                return INSPECTION_MODEL;
            case HULL_INSPECTION:
                return HULL_INSPECTION_MODEL;
            case OTHER:
                return BASE_MODEL;
            default:
                throw new IllegalArgumentException(eventType+" unknown");
        }
    }

    @Override
    public Key createKey()
    {
        Entity entity = data.getEntity();
        if (entity != null)
        {
            return entity.getKey();
        }
        else
        {
            DataObject prn = getParent();
            if (prn == null)
            {
                throw new IllegalArgumentException("parent not found");
            }
            if (eventType != Event.EventType.OTHER)
            {
                Key boatKey = (Key) get(BOAT);
                if (boatKey == null)
                {
                    throw new IllegalArgumentException("boat not found");
                }
                return KeyFactory.createKey(prn.createKey(), KIND, boatKey.getId());
            }
            else
            {
                return KeyFactory.createKey(prn.createKey(), KIND, System.currentTimeMillis());
            }
        }
    }

    @Override
    public int compareTo(Reservation o)
    {
        Long order1 = (Long) get(ORDER);
        if (order1 == null)
        {
            order1 = new Long(Long.MAX_VALUE);
        }
        Long order2 = (Long) o.get(ORDER);
        if (order2 == null)
        {
            order2 = new Long(Long.MAX_VALUE);
        }
        return order1.compareTo(order2);
    }

    @Override
    public String toString()
    {
        return get(LASTNAME)+" "+get(FIRSTNAME)+" "+get(BOATNAME);
    }

}

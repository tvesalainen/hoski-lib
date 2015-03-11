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

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import fi.hoski.datastore.Repository;
import static fi.hoski.datastore.repository.Reservable.ClosingDate;
import static fi.hoski.datastore.repository.Reservable.EventDate;
import fi.hoski.util.Day;
import fi.hoski.util.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Timo Vesalainen
 */
public class RaceFleet extends DataObject implements Reservable
{
    public static final String Kind = Repository.RACEFLEET;
    
    public static final String Fleet ="Fleet";
    public static final String Class ="Class";
    public static final String RatingSystem ="RatingSystem";
    public static final String StartTime = "StartTime";
    public static final String Fee ="Fee";
    public static final String Fee2 ="Fee2";
    public static final String ClosingDate2 ="EventClosingDate2";
    public static final String Ranking ="Ranking";
    public static final String SailWaveId ="SailWaveId";
    
    public static final DataObjectModel Model = new DataObjectModel(Kind);
    
    static
    {
        Model.property(Fleet);
        Model.property(Class);
        Model.property(EventDate, Day.class, true);
        Model.property(StartTime, Time.class, false, false);
        Model.property(ClosingDate, Day.class, true);
        Model.property(ClosingDate2, Day.class);
        Model.property(RatingSystem);
        Model.property(Fee, Double.class, false, false, 0.0);
        Model.property(Fee2, Double.class, false, false, 0.0);
        Model.property(Ranking, Boolean.class, true, false, false);
        Model.property(SailWaveId, Long.class, false, true);
    }

    private RaceFleet copiedFrom;
    
    public RaceFleet(RaceSeries raceSeries)
    {
        super(new MapData(Model));
        this.parent = raceSeries;
    }

    public RaceFleet(RaceSeries raceSeries, Entity entity)
    {
        super(Model, entity);
        this.parent = raceSeries;
    }

    public RaceFleet(DataObjectData data)
    {
        super(data);
    }

    @SuppressWarnings("unchecked")
    public RaceFleet makeCopy(int newNumber)
    {
        RaceFleet copy = new RaceFleet(new MapData(data));
        copy.set(RaceFleet.SailWaveId, newNumber);
        copy.parent = parent;
        if (observers != null)
        {
            ArrayList<DataObjectObserver> al = (ArrayList<DataObjectObserver>) observers;
            copy.observers = (List<DataObjectObserver>) al.clone();
        }
        copy.copiedFrom = this;
        return copy;
    }

    public RaceFleet getCopiedFrom()
    {
        return copiedFrom;
    }

    @Override
    public Key createKey()
    {
        return KeyFactory.createKey(parent.createKey(), Kind, getKeyName());
    }
    
    public String getKeyName()
    {
        String fleet = (String)get(Fleet);
        Day eventDate = (Day) get(EventDate);
        return fleet+eventDate.getValue();
    }

    public RaceSeries getRaceSeries()
    {
        return (RaceSeries) parent;
    }

    public String getName()
    {
        return (String) get(Fleet);
    }

    public Double getFee()
    {
        return (Double) get(Fee);
    }
    public Double getFee2()
    {
        return (Double) get(Fee2);
    }
    public Boolean getRanking()
    {
        return (Boolean) get(Ranking);
    }
    public String getFleet()
    {
        return (String) get(Fleet);
    }
    public String getClazz()
    {
        return (String) get(Class);
    }
    public String getRatingSystem()
    {
        return (String) get(RatingSystem);
    }
    public Day getEventDate()
    {
        return (Day) get(EventDate);
    }
    public Time getStartTime()
    {
        return (Time) get(StartTime);
    }
    public Day getClosingDate()
    {
        return (Day) get(ClosingDate);
    }
    public Day getClosingDate2()
    {
        return (Day) get(ClosingDate2);
    }
    public int getSailWaveId()
    {
        Long l = (Long) get(SailWaveId);
        return l.intValue();
    }
}

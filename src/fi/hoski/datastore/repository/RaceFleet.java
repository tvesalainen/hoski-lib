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
import fi.hoski.util.Day;
import fi.hoski.util.Time;

/**
 * @author Timo Vesalainen
 */
public class RaceFleet extends DataObject implements Reservable
{
    public static final String KIND = Repository.RACEFLEET;
    
    public static final String FLEET ="Fleet";
    public static final String CLASS ="Class";
    public static final String RATINGSYSTEM ="RatingSystem";
    public static final String STARTTIME = "StartTime";
    public static final String FEE ="Fee";
    public static final String FEE2 ="Fee2";
    public static final String CLOSINGDATE2 ="ClosingDate2";
    public static final String RANKING ="Ranking";
    
    public static final DataObjectModel MODEL = new DataObjectModel(KIND);
    
    static
    {
        MODEL.property(FLEET);
        MODEL.property(CLASS);
        MODEL.property(EVENTDATE, Day.class, true);
        MODEL.property(STARTTIME, Time.class, false, false);
        MODEL.property(CLOSINGDATE, Day.class, true);
        MODEL.property(CLOSINGDATE2, Day.class);
        MODEL.property(RATINGSYSTEM);
        MODEL.property(FEE, Double.class, false, false, 0.0);
        MODEL.property(FEE2, Double.class, false, false, 0.0);
        MODEL.property(RANKING, Boolean.class, true, false, false);
    }

    public RaceFleet(RaceSeries raceSeries)
    {
        super(new MapData(MODEL));
        this.parent = raceSeries;
    }

    public RaceFleet(RaceSeries raceSeries, Entity entity)
    {
        super(MODEL, entity);
        this.parent = raceSeries;
    }

    @Override
    public Key createKey()
    {
        String fleet = (String)get(FLEET);
        Day eventDate = (Day) get(EVENTDATE);
        return KeyFactory.createKey(parent.createKey(), KIND, fleet+eventDate.getValue());
    }

    public RaceSeries getRaceSeries()
    {
        return (RaceSeries) parent;
    }

}

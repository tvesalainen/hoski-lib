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

import com.google.appengine.api.datastore.*;
import fi.hoski.datastore.Repository;
import fi.hoski.util.Day;
import fi.hoski.util.Time;
import java.util.List;

/**
 * @author Timo Vesalainen
 */
public class RaceSeries extends DataObject implements Reservable
{
    public static final String KIND = Repository.RACESERIES;
    
    public static final String ID = "Id";
    public static final String EVENT = "Event";
    public static final String TO = "To";
    public static final String STARTTIME = "StartTime";
    public static final String RACE_AREA = "RaceArea";
    public static final String NOTES = "Notes";
    public static final String SAILWAVEFILE = "SailWaveFile";
    public static final String CLUBDISCOUNT = "ClubDiscount";
    public static final String SPONSORSTYLE = "SponsorStyle";
    public static final String PAGE ="Page";
    
    public static final DataObjectModel MODEL = new DataObjectModel(KIND);
    
    static
    {
        MODEL.property(ID);
        MODEL.property(EVENT, String.class, false, true);
        MODEL.property(EVENTDATE, Day.class, true, true);
        MODEL.property(TO, Day.class);
        MODEL.property(CLOSINGDATE, Day.class, true, true);
        MODEL.property(STARTTIME, Time.class, false, false);
        MODEL.property(RACE_AREA, String.class);
        MODEL.property(NOTES, Text.class);
        MODEL.property(SAILWAVEFILE, Blob.class);
        MODEL.property(CLUBDISCOUNT, Boolean.class, false, true, true);
        MODEL.property(SPONSORSTYLE);
        MODEL.property(PAGE);   // entry page if not null
    }

    private List<RaceFleet> classes;
    
    public RaceSeries()
    {
        super(MODEL, new MapData(MODEL));
    }

    public RaceSeries(Entity entity)
    {
        super(MODEL, entity);
    }

    public List<RaceFleet> getClasses()
    {
        return classes;
    }

    public void setClasses(List<RaceFleet> classes)
    {
        this.classes = classes;
    }

    @Override
    public Key createKey()
    {
        String id = (String) get(ID);
        Day date = (Day) get(EVENTDATE);
        return KeyFactory.createKey(Keys.getYearKey(date), KIND, date.getValue()+id);
    }

}

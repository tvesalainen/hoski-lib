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
import fi.hoski.datastore.Repository;
import fi.hoski.util.Day;

/**
 * @author Timo Vesalainen
 */
public class PatrolShift extends DataObject
{
    public static final String KIND = Repository.VARTIOVUOROTIEDOT;

    public static final DataObjectModel MODEL = new DataObjectModel(KIND);
    static
    {
        MODEL.property(Repository.VUOROID, Long.class);
        MODEL.property(Repository.JASENNO, Key.class, true);
        MODEL.property(Repository.VUORONO, Key.class);
        MODEL.property(Repository.PAIVA, Day.class, true);
    }
    public PatrolShift(Entity entity)
    {
        super(MODEL, entity);
    }
    
    @Override
    public Key createKey()
    {
        Entity entity = getEntity();
        if (entity != null)
        {
            return entity.getKey();
        }
        else
        {
            throw new IllegalArgumentException("key doesn't exist");
        }
    }

}

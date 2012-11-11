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
public class Inspection extends DataObject 
{
    public static final String KIND = Repository.KATSASTUSTIEDOT;
    
    public static final DataObjectModel INSPECTION_MODEL = new DataObjectModel(KIND);
    
    static
    {
        INSPECTION_MODEL.property(Repository.ID, Long.class);
        INSPECTION_MODEL.property(Repository.VENEID, Key.class, true, true);
        INSPECTION_MODEL.property(Repository.KATSASTUSLUOKKA, Long.class, false, true);
        INSPECTION_MODEL.property(Repository.KATSASTUSTYYPPI, Key.class, true, true);
        INSPECTION_MODEL.property(Repository.KATSASTAJA, Long.class, false, false, 0L);
        INSPECTION_MODEL.property(Repository.KAASU, Boolean.class, false, false, false);
        INSPECTION_MODEL.property(Repository.PAIVA, Day.class, true, true);
    }

    public Inspection(Entity entity)
    {
        super(INSPECTION_MODEL, entity);
    }

    @Override
    public Key createKey()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Timo Vesalainen
 */
public class JoinData extends DataObjectData
{
    Map<String,DataObject> map;

    public JoinData(Map<String, DataObject> map, DataObjectModel model)
    {
        super(model);
        this.map = map;
    }
    
    @Override
    public Object get(String property)
    {
        String[] ss = property.split("\\.");
        if (ss.length == 1)
        {
            DataObject dob = map.get("");
            return dob.get(property);
        }
        else
        {
            if (ss.length == 2)
            {
                DataObject dob = map.get(ss[0]);
                return dob.get(ss[1]);
            }
            else
            {
                throw new IllegalArgumentException(property);
            }
        }
    }

    @Override
    public void set(String property, Object def)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void remove(String property)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Iterable<Entry<String, Object>> entrySet()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<String, Object> getAll()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Entity getEntity()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

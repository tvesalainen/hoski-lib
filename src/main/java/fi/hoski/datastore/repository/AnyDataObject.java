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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Timo Vesalainen
 */
public class AnyDataObject extends DataObject
{

    public AnyDataObject(DataObjectModel model, DataObjectData data)
    {
        super(model, data);
    }
    
    public AnyDataObject(Entity entity, String... properties)
    {
        super(createDataObjectData(entity, properties));
    }

    private static DataObjectData createDataObjectData(Entity entity, String... properties)
    {
        DataObjectModel dot = new DataObjectModel(entity.getKind());
        for (String property : properties)
        {
            Object value = entity.getProperty(property);
            if (value == null || (value instanceof String))
            {
                dot.property(property);
            }
            else
            {
                dot.property(property, value.getClass());
            }
        }
        return new EntityData(entity, dot);
    }
    public static List<AnyDataObject> create(List<Entity> list, String... properties)
    {
        List<AnyDataObject> l = new ArrayList<AnyDataObject>();
        for (Entity entity : list)
        {
            l.add(new AnyDataObject(entity, properties));
        }
        return l;
    }

    @Override
    public Key createKey()
    {
        return getEntity().getKey();
    }

}

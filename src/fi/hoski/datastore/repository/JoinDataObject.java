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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import fi.hoski.datastore.DSUtils;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Timo Vesalainen
 */
public class JoinDataObject extends DataObject
{
    
    public JoinDataObject(DatastoreService datastore, DSUtils entities, Entity entity) throws EntityNotFoundException
    {
        super(createDataObjectData(datastore, entities, entity));
    }

    private static DataObjectData createDataObjectData(DatastoreService datastore, DSUtils entities, Entity entity) throws EntityNotFoundException
    {
        DataObjectModel dot = new DataObjectModel(entity.getKind());
        Map<String,DataObject> map = new HashMap<String,DataObject>();
        map.put("", new AnyDataObject(entity));
        for (String property : entity.getProperties().keySet())
        {
            Object value = entity.getProperty(property);
            if (value instanceof Key)
            {
                Key refKey = (Key) value;
                Entity refEntity = datastore.get(refKey);
                DataObject dob = entities.newInstance(refEntity);
                dot.addAll(property, dob.getModel());
                map.put(refEntity.getKind(), dob);
            }
        }
        return new JoinData(map, dot);
    }

    @Override
    public Key createKey()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

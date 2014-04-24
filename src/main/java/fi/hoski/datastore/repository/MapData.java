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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * @author Timo Vesalainen
 */
public class MapData extends DataObjectData implements Cloneable
{
    private Map<String,Object> valueMap;

    public MapData(DataObjectModel model)
    {
        super(model);
        valueMap = new HashMap<>();
    }
    
    public MapData(DataObjectModel model, Properties properties)
    {
        super(model);
        valueMap = new HashMap<>();
        for (String property : properties.stringPropertyNames())
        {
            valueMap.put(property, properties.getProperty(property));
        }
    }

    private MapData(DataObjectModel model, Map<String,Object> map)
    {
        super(model);
        valueMap = map;
    }

    public MapData(DataObjectData data)
    {
        super(data.model);
        valueMap = new HashMap<>();
        for (String key : model.getPropertyList())
        {
            valueMap.put(key, data.get(key));
        }
    }
    
    @Override
    public MapData clone()
    {
        Map<String,Object> map = new HashMap<String,Object>();
        map.putAll(valueMap);
        return new MapData(model, map);
    }
    
    public Map<String, Object> getAll()
    {
        return Collections.unmodifiableMap(valueMap);
    }

    @Override
    public Object get(String property)
    {
        return valueMap.get(property);
    }

    @Override
    public void set(String property, Object ob)
    {
        valueMap.put(property, ob);
    }

    @Override
    public void remove(String property)
    {
        valueMap.remove(property);
    }

    @Override
    public Iterable<Map.Entry<String, Object>> entrySet()
    {
        return valueMap.entrySet();
    }

    @Override
    public Entity getEntity()
    {
        return null;
    }


}

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
import com.google.appengine.api.datastore.Text;
import fi.hoski.util.Day;
import fi.hoski.util.Time;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Timo Vesalainen
 */
public class EntityData extends DataObjectData implements Cloneable
{
    protected Entity entity;

    public EntityData(Entity entity, DataObjectModel model)
    {
        super(model);
        this.entity = entity;
        for (String property : model.defaultMap.keySet())
        {
            if (!entity.hasProperty(property))
            {
                entity.setProperty(property, model.defaultMap.get(property));
            }
        }
    }

    @Override
    public EntityData clone()
    {
        return new EntityData(entity.clone(), model);
    }

    @Override
    public Map<String, Object> getAll()
    {
        return entity.getProperties();
    }

    @Override
    public Entity getEntity()
    {
        return entity;
    }
    
    public String getKind()
    {
        return entity.getKind();
    }

    @Override
    public Object get(String property)
    {
        Object ob = entity.getProperty(property);
        if (ob == null)
        {
            return null;
        }
        if (Text.class.equals(model.getType(property)))
        {
            Text t = (Text) ob;
            return t.getValue();
        }
        else
        {
            if (Day.class.equals(model.getType(property)))
            {
                Long l = (Long) ob;
                return new Day(l);
            }
            else
            {
                if (Time.class.equals(model.getType(property)))
                {
                    Long l = (Long) ob;
                    return new Time(l);
                }
                else
                {
                    return ob;
                }
            }
        }
    }

    @Override
    public void set(String property, Object def)
    {
        if (def instanceof Day)
        {
            Day d = (Day) def;
            def = d.getValue();
        }
        if (def instanceof Time)
        {
            Time t = (Time) def;
            def = t.getValue();
        }
        if (model.indexSet.contains(property))
        {
            entity.setProperty(property, def);
        }
        else
        {
            entity.setUnindexedProperty(property, def);
        }
    }

    @Override
    public void remove(String property)
    {
        entity.removeProperty(property);
    }

    @Override
    public Iterable<Entry<String, Object>> entrySet()
    {
        return entity.getProperties().entrySet();
    }

}

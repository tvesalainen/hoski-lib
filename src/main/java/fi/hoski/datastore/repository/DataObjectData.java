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
import java.util.Map;

/**
 * @author Timo Vesalainen
 */
public abstract class DataObjectData implements Cloneable
{
    protected DataObjectModel model;

    public DataObjectData(DataObjectModel model)
    {
        this.model = model;
    }

    @Override
    protected DataObjectData clone()
    {
        throw new UnsupportedOperationException("override!");
    }

    public DataObjectModel getModel()
    {
        return model;
    }

    public abstract Object get(String property);

    public abstract void set(String property, Object def);

    public abstract void remove(String property);

    public abstract Iterable<Map.Entry<String, Object>> entrySet();

    public abstract Map<String, Object> getAll();
    
    public abstract Entity getEntity();
    
}

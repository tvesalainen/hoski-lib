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

package fi.hoski.datastore;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import fi.hoski.util.Persistence;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

/**
 * @author Timo Vesalainen
 */
public class DatastorePersistence implements Persistence
{
    public static final String PERSISTENCE = "Persistence";
    public static final String VALUE = "Value";
    
    protected DatastoreService datastore;

    public DatastorePersistence(DatastoreService datastore)
    {
        this.datastore = datastore;
    }

    @Override
    public Object get(Object property)
    {
        try
        {
            Key key = KeyFactory.createKey(PERSISTENCE, property.toString());
            Entity entity = datastore.get(key);
            Blob blob = (Blob) entity.getProperty(VALUE);
            if (blob == null)
            {
                return null;
            }
            else
            {
                ByteArrayInputStream bais = new ByteArrayInputStream(blob.getBytes());
                ObjectInputStream ois = new ObjectInputStream(bais);
                return ois.readObject();
            }
        }
        catch (ClassNotFoundException ex)
        {
            throw new IllegalArgumentException(ex);
        }
        catch (IOException ex)
        {
            throw new IllegalArgumentException(ex);
        }
        catch (EntityNotFoundException ex)
        {
            return null;
        }
    }

    @Override
    public void put(Object property, Object value)
    {
        try
        {
            Key key = KeyFactory.createKey(PERSISTENCE, property.toString());
            Entity entity = new Entity(key);
            if (value == null)
            {
                entity.setUnindexedProperty(VALUE, null);
            }
            else
            {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(value);
                oos.close();
                Blob blob = new Blob(baos.toByteArray());
                entity.setUnindexedProperty(VALUE, blob);
            }
            entity.setUnindexedProperty(Repository.TIMESTAMP, new Date());
            datastore.put(entity);
        }
        catch (IOException ex)
        {
            throw new IllegalArgumentException(ex);
        }
    }

}

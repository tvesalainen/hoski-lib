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

import com.google.appengine.api.datastore.DatastoreAttributes;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entities;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Index;
import com.google.appengine.api.datastore.Index.IndexState;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyRange;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.TransactionOptions;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Timo Vesalainen
 */
public class CachingDatastoreService implements DatastoreService
{
    private DatastoreService datastore;
    private Key root;
    private long version;
    private Map<Query,PreparedQuery> preparedQueryMap = new WeakHashMap<Query,PreparedQuery>();
    private Map<Key,Entity> entityMap = new WeakHashMap<Key,Entity>();
    private Map<Index, IndexState> indexes;

    public CachingDatastoreService(DatastoreService datastore, Key root)
    {
        this.datastore = datastore;
        this.root = Entities.createEntityGroupKey(root);
    }

    private void check()
    {
        try
        {
            long v = Entities.getVersionProperty(datastore.get(root));
            if (v > version)
            {
                // remove all cache
                entityMap.clear();
                preparedQueryMap.clear();
                indexes = null;
                version = v;
            }
        }
        catch (EntityNotFoundException ex)
        {
            throw new IllegalArgumentException(ex);
        }
    }
    
    public PreparedQuery prepare(Transaction t, Query query)
    {
        check();
        PreparedQuery pq = preparedQueryMap.get(query);
        if (pq == null)
        {
            pq = new CachingPreparedQuery(datastore.prepare(t, query));
            preparedQueryMap.put(query, pq);
        }
        return pq;
    }

    public PreparedQuery prepare(Query query)
    {
        check();
        PreparedQuery pq = preparedQueryMap.get(query);
        if (pq == null)
        {
            pq = new CachingPreparedQuery(datastore.prepare(query));
            preparedQueryMap.put(query, pq);
        }
        return pq;
    }

    public Transaction getCurrentTransaction(Transaction t)
    {
        return datastore.getCurrentTransaction(t);
    }

    public Transaction getCurrentTransaction()
    {
        return datastore.getCurrentTransaction();
    }

    public Collection<Transaction> getActiveTransactions()
    {
        return datastore.getActiveTransactions();
    }

    public List<Key> put(Transaction t, Iterable<Entity> itrbl)
    {
        return datastore.put(t, itrbl);
    }

    public List<Key> put(Iterable<Entity> itrbl)
    {
        return datastore.put(itrbl);
    }

    public Key put(Transaction t, Entity entity)
    {
        return datastore.put(t, entity);
    }

    public Key put(Entity entity)
    {
        return datastore.put(entity);
    }

    public Map<Index, IndexState> getIndexes()
    {
        check();
        if (indexes == null)
        {
            indexes = datastore.getIndexes();
        }
        return indexes;
    }

    public DatastoreAttributes getDatastoreAttributes()
    {
        return datastore.getDatastoreAttributes();
    }

    public Map<Key, Entity> get(Transaction t, Iterable<Key> itrbl)
    {
        return datastore.get(t, itrbl);
    }

    public Map<Key, Entity> get(Iterable<Key> itrbl)
    {
        return datastore.get(itrbl);
    }

    public Entity get(Transaction t, Key key) throws EntityNotFoundException
    {
        check();
        Entity e = entityMap.get(key);
        if (e == null)
        {
            e = datastore.get(t, key);
            entityMap.put(key, e);
        }
        return e;
    }

    public Entity get(Key key) throws EntityNotFoundException
    {
        check();
        Entity e = entityMap.get(key);
        if (e == null)
        {
            e = datastore.get(key);
            entityMap.put(key, e);
        }
        return e;
    }

    public void delete(Transaction t, Iterable<Key> itrbl)
    {
        datastore.delete(t, itrbl);
    }

    public void delete(Iterable<Key> itrbl)
    {
        datastore.delete(itrbl);
    }

    public void delete(Transaction t, Key... keys)
    {
        datastore.delete(t, keys);
    }

    public void delete(Key... keys)
    {
        datastore.delete(keys);
    }

    public Transaction beginTransaction(TransactionOptions to)
    {
        return datastore.beginTransaction(to);
    }

    public Transaction beginTransaction()
    {
        return datastore.beginTransaction();
    }

    public KeyRange allocateIds(Key key, String string, long l)
    {
        return datastore.allocateIds(key, string, l);
    }

    public KeyRange allocateIds(String string, long l)
    {
        return datastore.allocateIds(string, l);
    }

    public KeyRangeState allocateIdRange(KeyRange kr)
    {
        return datastore.allocateIdRange(kr);
    }
    
}

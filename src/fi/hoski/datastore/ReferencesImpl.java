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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import fi.hoski.datastore.repository.Keys;
import fi.hoski.util.Day;

/**
 * @author Timo Vesalainen
 */
public class ReferencesImpl implements References
{
    public static final String NEXT = "Next";
    public static final String REFER = "Refer";
    public static final String REFERENCE = "Reference";
    public static final String NEXTREFERENCE = "NextReference";
    
    protected DatastoreService datastore;

    public ReferencesImpl()
    {
        this.datastore = DatastoreServiceFactory.getDatastoreService();
    }
    
    public ReferencesImpl(DatastoreService datastore)
    {
        this.datastore = datastore;
    }

    @Override
    public long getNextReferenceFor(Key parent)
    {
        Key nextReferenceKey = KeyFactory.createKey(parent, NEXTREFERENCE, 1);
        Entity entity = null;
        long next = 1;
        try
        {
            entity = datastore.get(nextReferenceKey);
            Long n = (Long) entity.getProperty(NEXT);
            if (n == null)
            {
                throw new IllegalArgumentException(NEXT+" not found");
            }
            next = n;
        }
        catch (EntityNotFoundException ex)
        {
            entity = new Entity(nextReferenceKey);
        }
        entity.setUnindexedProperty(NEXT, next+1);
        datastore.put(entity);
        return next;
    }

    @Override
    public void addReferenceFor(Key key, long reference)
    {
        Key yearKey = Keys.getYearKey(new Day());
        if (getReferenceFor(key) != -1)
        {
            throw new IllegalArgumentException(reference+" to "+key+" exists");
        }
        Key nkey = KeyFactory.createKey(yearKey, REFERENCE, reference);
        Entity entity = new Entity(nkey);
        entity.setProperty(REFER, key);
        datastore.put(entity);
    }

    @Override
    public long getReferenceFor(Key key)
    {
        Key yearKey = Keys.getYearKey(new Day());
        Query query = new Query(REFERENCE);
        query.setAncestor(yearKey);
        query.addFilter(REFER, Query.FilterOperator.EQUAL, key);
        PreparedQuery prepared = datastore.prepare(query);
        Entity entity = prepared.asSingleEntity();
        if (entity != null)
        {
            return entity.getKey().getId();
        }
        else
        {
            return -1;
        }
    }

    @Override
    public Entity getEntityFor(long reference) throws EntityNotFoundException
    {
        Key yearKey = Keys.getYearKey(new Day());
        Key nkey = KeyFactory.createKey(yearKey, REFERENCE, reference);
        Entity entity = datastore.get(nkey);
        Key key = (Key) entity.getProperty(REFER);
        return datastore.get(key);
    }
}

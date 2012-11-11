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

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.QueryResultIterable;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.api.datastore.QueryResultList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Timo Vesalainen
 */
public class CachingPreparedQuery implements PreparedQuery
{
    private PreparedQuery preparedQuery;
    private int count = -1;
    private List<Entity> list;
    private Iterable<Entity> iterable;
    private Iterator<Entity> iterator;
    private QueryResultList<Entity> queryResultList;
    private QueryResultIterator<Entity> queryResultIterator;
    private QueryResultIterable<Entity> queryResultIterable;
    
    public CachingPreparedQuery(PreparedQuery preparedQuery)
    {
        this.preparedQuery = preparedQuery;
    }

    public int countEntities()
    {
        if (count == -1)
        {
            count = preparedQuery.countEntities();
        }
        return count;
    }

    public int countEntities(FetchOptions fo)
    {
        if (count == -1)
        {
            count = preparedQuery.countEntities(fo);
        }
        return count;
    }

    public Entity asSingleEntity() throws TooManyResultsException
    {
        if (list == null)
        {
            list = new ArrayList<Entity>();
            list.add(preparedQuery.asSingleEntity());
        }
        switch (list.size())
        {
            case 0:
                return null;
            case 1:
                return list.get(0);
            default:
                throw new TooManyResultsException();
        }
    }

    public QueryResultList<Entity> asQueryResultList(FetchOptions fo)
    {
        if (queryResultList == null)
        {
            queryResultList = preparedQuery.asQueryResultList(fo);
        }
        return queryResultList;
    }

    public QueryResultIterator<Entity> asQueryResultIterator()
    {
        if (queryResultIterator == null)
        {
            queryResultIterator = preparedQuery.asQueryResultIterator();
        }
        return queryResultIterator;
    }

    public QueryResultIterator<Entity> asQueryResultIterator(FetchOptions fo)
    {
        if (queryResultIterator == null)
        {
            queryResultIterator = preparedQuery.asQueryResultIterator(fo);
        }
        return queryResultIterator;
    }

    public QueryResultIterable<Entity> asQueryResultIterable()
    {
        if (queryResultIterable == null)
        {
            queryResultIterable = preparedQuery.asQueryResultIterable();
        }
        return queryResultIterable;
    }

    public QueryResultIterable<Entity> asQueryResultIterable(FetchOptions fo)
    {
        if (queryResultIterable == null)
        {
            queryResultIterable = preparedQuery.asQueryResultIterable(fo);
        }
        return queryResultIterable;
    }

    public List<Entity> asList(FetchOptions fo)
    {
        if (list == null)
        {
            list = preparedQuery.asList(fo);
        }
        return list;
    }

    public Iterator<Entity> asIterator()
    {
        if (iterator == null)
        {
            iterator = preparedQuery.asIterator();
        }
        return iterator;
    }

    public Iterator<Entity> asIterator(FetchOptions fo)
    {
        if (iterator == null)
        {
            iterator = preparedQuery.asIterator(fo);
        }
        return iterator;
    }

    public Iterable<Entity> asIterable()
    {
        if (iterable == null)
        {
            iterable = preparedQuery.asIterable();
        }
        return iterable;
    }

    public Iterable<Entity> asIterable(FetchOptions fo)
    {
        if (iterable == null)
        {
            iterable = preparedQuery.asIterable(fo);
        }
        return iterable;
    }
    
}

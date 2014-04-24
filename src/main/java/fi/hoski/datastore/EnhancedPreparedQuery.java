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

import com.google.appengine.api.datastore.DataTypeTranslator;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortPredicate;
import com.google.appengine.api.datastore.QueryResultIterable;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.api.datastore.QueryResultList;
import fi.hoski.util.ConvertUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Timo Vesalainen
 */
public class EnhancedPreparedQuery implements PreparedQuery 
{
    private PreparedQuery preparedQuery;
    private List<FilterPredicate> filter;
    private List<SortPredicate> sort;

    public EnhancedPreparedQuery(PreparedQuery preparedQuery, List<FilterPredicate> filter, List<SortPredicate> sort)
    {
        this.preparedQuery = preparedQuery;
        this.filter = filter;
        this.sort = sort;
    }

    @Override
    public List<Entity> asList(FetchOptions fo)
    {
        List<Entity> list = new ArrayList<Entity>();
        for (Entity e : preparedQuery.asIterable(fo))
        {
            if (matches(e))
            {
                list.add(e);
            }
        }
        if (sort != null)
        {
            EntityComparator ec = new EntityComparator(sort);
            Collections.sort(list, ec);
        }
        return list;
    }

    @Override
    public QueryResultList<Entity> asQueryResultList(FetchOptions fo)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Iterable<Entity> asIterable(FetchOptions fo)
    {
        List<Entity> list = asList(fo);
        return list;
    }

    @Override
    public QueryResultIterable<Entity> asQueryResultIterable(FetchOptions fo)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Iterable<Entity> asIterable()
    {
        List<Entity> list = asList(FetchOptions.Builder.withDefaults());
        return list;
    }

    @Override
    public QueryResultIterable<Entity> asQueryResultIterable()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Iterator<Entity> asIterator(FetchOptions fo)
    {
        List<Entity> list = asList(fo);
        return list.iterator();
    }

    @Override
    public Iterator<Entity> asIterator()
    {
        List<Entity> list = asList(FetchOptions.Builder.withDefaults());
        return list.iterator();
    }

    @Override
    public QueryResultIterator<Entity> asQueryResultIterator(FetchOptions fo)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public QueryResultIterator<Entity> asQueryResultIterator()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Entity asSingleEntity() throws TooManyResultsException
    {
        List<Entity> list = asList(FetchOptions.Builder.withDefaults());
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

    @Override
    public int countEntities(FetchOptions fo)
    {
        List<Entity> list = asList(fo);
        return list.size();
    }

    @Override
    public int countEntities()
    {
        List<Entity> list = asList(FetchOptions.Builder.withDefaults());
        return list.size();
    }

    private boolean matches(Entity entity)
    {
        for (FilterPredicate fp : filter)
        {
            Object ev = entity.getProperty(fp.getPropertyName());
            Object v = fp.getValue();
            if (ev == null || v == null)
            {
                throw new IllegalArgumentException("null");
            }
            v = ConvertUtil.convertTo(v, ev.getClass());
            Comparable comp = (Comparable) ev;
            if (ev.getClass().equals(v.getClass()))
            {
                @SuppressWarnings("unchecked")
                int compRes = comp.compareTo(v);
                switch (fp.getOperator())
                {
                    case EQUAL:
                        if (compRes != 0)
                        {
                            return false;
                        }
                        break;
                    case NOT_EQUAL:
                        if (compRes == 0)
                        {
                            return false;
                        }
                        break;
                    case GREATER_THAN:
                        if (compRes <= 0)
                        {
                            return false;
                        }
                        break;
                    case GREATER_THAN_OR_EQUAL:
                        if (compRes < 0)
                        {
                            return false;
                        }
                        break;
                    case LESS_THAN:
                        if (compRes >= 0)
                        {
                            return false;
                        }
                        break;
                    case LESS_THAN_OR_EQUAL:
                        if (compRes > 0)
                        {
                            return false;
                        }
                        break;
                    default:
                        throw new IllegalArgumentException(fp.getOperator()+" not supported");
                }
            }
            else
            {
                throw new IllegalArgumentException("operands must be of the same type "+fp);
            }
        }
        return true;
    }
}

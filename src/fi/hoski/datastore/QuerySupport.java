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
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Index;
import com.google.appengine.api.datastore.Index.IndexState;
import com.google.appengine.api.datastore.Index.Property;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Projection;
import com.google.appengine.api.datastore.PropertyProjection;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Query.SortPredicate;
import com.google.appengine.api.search.SortExpression;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * QuerySupport allows all queries. If datastore indexes doesn't support a query
 * that support is added.
 * @author Timo Vesalainen
 */
public class QuerySupport 
{
    public static PreparedQuery prepare(DatastoreService datastore, Query oq)
    {
        Query nq = new Query(oq.getKind());
        Key ancestor = oq.getAncestor();
        if (ancestor != null)
        {
            nq.setAncestor(ancestor);
        }
        if (oq.isKeysOnly())
        {
            nq.setKeysOnly();
        }
        else
        {
            for (Projection p : oq.getProjections())
            {
                nq.addProjection(p);
            }
        }
        List<FilterPredicate> ofp = oq.getFilterPredicates();
        List<FilterPredicate> lfp = new ArrayList<FilterPredicate>();
        if (ofp.size() > 0)
        {
            if (ofp.size() == 1)
            {
                FilterPredicate predicate = ofp.get(0);
                Query pnq = new Query("__Stat_PropertyName_Kind__");
                //pnq.addProjection(new PropertyProjection("builtin_index_count", Long.class));
                pnq.addFilter("kind_name", Query.FilterOperator.EQUAL, oq.getKind());
                pnq.addFilter("property_name", Query.FilterOperator.EQUAL, predicate.getPropertyName());
                PreparedQuery pnqp = datastore.prepare(pnq);
                Entity propertyEntity = pnqp.asSingleEntity();
                if (propertyEntity == null)
                {
                    throw new IllegalArgumentException(predicate.getPropertyName()+" doesn't exist in "+oq);
                }
                Long indexCount = (Long) propertyEntity.getProperty("builtin_index_count");
                if (indexCount == 0)
                {
                    lfp.add(predicate);
                }
                else
                {
                    nq.addFilter(predicate.getPropertyName(), predicate.getOperator(), predicate.getValue());
                }
                System.err.println(propertyEntity);
            }
            else
            {
                boolean indexFound = false;
                Map<Index, IndexState> indexes = datastore.getIndexes();
                for (Index index : indexes.keySet())
                {
                    if (    (oq.getKind() == null ? index.getKind() == null : oq.getKind().equals(index.getKind())) &&
                            oq.getAncestor() != null == index.isAncestor()
                            )
                    {
                        if (ofp.size() == index.getProperties().size())
                        {
                            boolean pe = true;
                            for (Property p : index.getProperties())
                            {
                                boolean f = false;
                                for (FilterPredicate fp : ofp)
                                {
                                    if (p.getName().equals(fp.getPropertyName()))
                                    {
                                        f = true;
                                        break;
                                    }
                                }
                                if (!f)
                                {
                                    pe = false;
                                    break;
                                }
                            }
                            if (pe)
                            {
                                indexFound = true;
                                break;
                            }
                        }
                    }
                }
                if (indexFound)
                {
                    for (FilterPredicate predicate : ofp)
                    {
                        nq.addFilter(predicate.getPropertyName(), predicate.getOperator(), predicate.getValue());
                    }
                }
                else
                {
                    boolean matched = false;
                    for (FilterPredicate predicate : ofp)
                    {
                        if (!matched)
                        {
                            Query pnq = new Query("__Stat_PropertyName_Kind__");
                            //pnq.addProjection(new PropertyProjection("builtin_index_count", Long.class));
                            pnq.addFilter("kind_name", Query.FilterOperator.EQUAL, oq.getKind());
                            pnq.addFilter("property_name", Query.FilterOperator.EQUAL, predicate.getPropertyName());
                            PreparedQuery pnqp = datastore.prepare(pnq);
                            Entity propertyEntity = pnqp.asSingleEntity();
                            if (propertyEntity == null)
                            {
                                throw new IllegalArgumentException(predicate.getPropertyName()+" doesn't exist in "+oq);
                            }
                            Long indexCount = (Long) propertyEntity.getProperty("builtin_index_count");
                            if (indexCount > 0)
                            {
                                nq.addFilter(predicate.getPropertyName(), predicate.getOperator(), predicate.getValue());
                                matched = true;
                                continue;
                            }
                        }
                        lfp.add(predicate);
                    }
                }
            }
        }
        // sorting
        List<SortPredicate> osp = oq.getSortPredicates();
        List<SortPredicate> lsp = null;
        List<FilterPredicate> nfp = nq.getFilterPredicates();
        boolean sortOk = true;
        if (osp.size() == nfp.size())
        {
            for (int ii=0;ii<osp.size();ii++)
            {
                SortPredicate sp = osp.get(ii);
                FilterPredicate fp = nfp.get(ii);
                if (
                        !sp.getDirection().equals(SortDirection.ASCENDING) ||
                        !sp.getPropertyName().equals(fp.getPropertyName())
                        )
                {
                    sortOk = false;
                    break;
                }
            }
        }
        else
        {
            sortOk = false;
        }
        if (!sortOk)
        {
            lsp = osp;
        }
        return new EnhancedPreparedQuery(datastore.prepare(nq), lfp, lsp);
    }
}

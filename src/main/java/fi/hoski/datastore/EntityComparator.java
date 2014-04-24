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
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortPredicate;
import java.util.Comparator;
import java.util.List;

/**
 * @author Timo Vesalainen
 */
public class EntityComparator implements Comparator<Entity>
{
    private List<SortPredicate> order;

    public EntityComparator(List<SortPredicate> order)
    {
        this.order = order;
    }
    
    
    @Override
    public int compare(Entity o1, Entity o2)
    {
        for (SortPredicate sp : order)
        {
            String property = sp.getPropertyName();
            int s = 1;
            if (sp.getDirection() == Query.SortDirection.DESCENDING)
            {
                s = -1;
            }
            Object v1 = o1.getProperty(property);
            Object v2 = o2.getProperty(property);
            if (v1 != null && v2 != null)
            {
                Comparable c1 = (Comparable) v1;
                @SuppressWarnings("unchecked")
                int comp = c1.compareTo(v2);
                if (comp != 0)
                {
                    return s*comp;
                }
            }
            else
            {
                if (v1 == null && v2 == null)
                {
                    continue;
                }
                else
                {
                    if (v1 == null)
                    {
                        return s*-1;
                    }
                    else
                    {
                        return s*1;
                    }
                }
            }
        }
        return 0;
    }

}

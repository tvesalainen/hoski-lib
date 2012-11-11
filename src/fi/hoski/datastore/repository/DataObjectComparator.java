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

import java.util.Comparator;

/**
 * @author Timo Vesalainen
 */
public class DataObjectComparator implements Comparator<DataObject>
{
    private String[] sortOrder;

    public DataObjectComparator(String... sortOrder)
    {
        this.sortOrder = sortOrder;
    }
    
    @Override
    public int compare(DataObject o1, DataObject o2)
    {
        for (String property : sortOrder)
        {
            Object v1 = o1.get(property);
            Object v2 = o2.get(property);
            if (v1 != null && v2 != null)
            {
                Comparable c1 = (Comparable) v1;
                int comp = c1.compareTo(v2);
                if (comp != 0)
                {
                    return comp;
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
                        return -1;
                    }
                    else
                    {
                        return 1;
                    }
                }
            }
        }
        return 0;
    }

}

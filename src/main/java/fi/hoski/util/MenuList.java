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

package fi.hoski.util;

import com.google.appengine.api.datastore.Key;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author Timo Vesalainen
 */
public class MenuList
{
    private Map<Key,Map> tree;
    private int depth;
    public MenuList(String kind, Map<Key,Map> tree, int depth)
    {
        while (tree.size() == 1 && depth > 2)
        {
            Set<Entry<Key, Map>> entrySet = tree.entrySet();
            Entry<Key, Map> entry = entrySet.iterator().next();
            @SuppressWarnings("unchecked")
            Map<Key,Map> value = entry.getValue();
            tree = value;
            depth--;
        }
        this.tree = tree;
        this.depth = depth;
    }

    public void html(PrintWriter out)
    {
        html(out, tree, depth);
    }
    private void html(PrintWriter out, Map<Key,Map> t, int d)
    {
        if (!t.isEmpty() && d > 1)
        {
            out.println("<ul>");
            for (Key key : t.keySet())
            {
                out.println("<li>"+key+"</li>");
                @SuppressWarnings("unchecked")
                Map<Key,Map> m = t.get(key);
                html(out, m, d-1);
            }
            out.println("</ul>");
        }
    }
}

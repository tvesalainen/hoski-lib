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

import java.io.Serializable;
import java.util.*;

/**
 * @author Timo Vesalainen
 */
public class Options<T> implements Serializable, Iterable<String>
{
    private static final long serialVersionUID = 1L;
    
    private T selection;
    private List<String> items = new ArrayList<String>();
    private Map<String,T> map = new HashMap<String,T>();

    public int size()
    {
        return items.size();
    }
    public Object[] getLabels()
    {
        return items.toArray();
    }
    public void addItem(String label, T item)
    {
        items.add(label);
        map.put(label, item);
    }
    
    public T getItem(String label)
    {
        return map.get(label);
    }
    
    public T getSelection()
    {
        return selection;
    }

    public void setSelection(T selection)
    {
        this.selection = selection;
    }

    public Map<String, T> getMap()
    {
        return map;
    }

    @Override
    public Iterator<String> iterator()
    {
        return items.iterator();
    }

    public String html(String name)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("<select name='"+name+"'>");
        sb.append(html());
        sb.append("</select>");
        return sb.toString();
    }
    public String html()
    {
        StringBuilder sb = new StringBuilder();
        for (String label : items)
        {
            T item = map.get(label);
            if (item.equals(selection))
            {
                sb.append("<option selected='true' value='"+item+"'>"+label+"</option>");
            }
            else
            {
                sb.append("<option value='"+item+"'>"+label+"</option>");
            }
        }
        return sb.toString();
    }

}

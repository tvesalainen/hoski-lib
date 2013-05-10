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

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Link;
import fi.hoski.util.EntityReferences;

/**
 * @author Timo Vesalainen
 */
public class Attachment extends DataObject implements Comparable<Attachment>
{
    public static final String KIND = "Attachment";

    public enum Type {NOR, SI, RESULT, OTHER, PICS, NOTICE };   // don't change order. If needed add new entries to the end!
    
    public static final String TYPE = "Type";
    public static final String TITLE = "Title";
    public static final String URL = "URL";

    public static final DataObjectModel MODEL = new DataObjectModel(KIND);
    static
    {
        MODEL.property(TYPE, Long.class, true, true);
        MODEL.property(TITLE, String.class, false, false, "");
        MODEL.property(URL, Link.class);
    }

    public Attachment(DataObject parent)
    {
        super(new MapData(MODEL));
        this.parent = parent;
    }

    public Attachment(Entity entity)
    {
        super(new EntityData(entity, MODEL));
    }

    
    @Override
    public Key createKey()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Entity createEntity()
    {
        return new Entity(KIND, parent.createKey());
    }
    
    public String getAnchor()
    {
        String title = (String) get(TITLE);
        Link link = (Link) get(URL);
        return "<a href='"+link.getValue()+"'>"+EntityReferences.encode(title)+"</a>";
    }
    @Override
    public int compareTo(Attachment o)
    {
        Long t1 = (Long) get(TYPE);
        Long t2 = (Long) o.get(TYPE);
        int rc = t1.compareTo(t2);
        if (rc == 0)
        {
            String title1 = (String) get(TITLE);
            String title2 = (String) o.get(TITLE);
            return title1.compareToIgnoreCase(title2);
        }
        else
        {
            return rc;
        }
    }

}

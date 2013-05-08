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
import com.google.appengine.api.datastore.KeyFactory;
import fi.hoski.datastore.Repository;
import fi.hoski.util.Day;
import java.util.Collection;

/**
 * @author Timo Vesalainen
 */
public class SwapRequest extends DataObject
{
    public static final String KIND = "SwapRequest";
    
    public static final String EXCLUDE = "Exclude";

    public static final DataObjectModel SWAP_REQUEST_MODEL = new DataObjectModel(KIND);
    static
    {
        SWAP_REQUEST_MODEL.property(Repository.JASENNO, Key.class, true, true);
        SWAP_REQUEST_MODEL.property(Repository.VUOROID, Key.class);
        SWAP_REQUEST_MODEL.property(Repository.PAIVA, Day.class, true, true);
        SWAP_REQUEST_MODEL.property(EXCLUDE, Collection.class); // Collection<Long=Day>
        SWAP_REQUEST_MODEL.property(Repository.CREATOR);
    }

    public SwapRequest()
    {
        super(SWAP_REQUEST_MODEL, new MapData(SWAP_REQUEST_MODEL));
    }

    public SwapRequest(Entity entity)
    {
        super(SWAP_REQUEST_MODEL, entity);
    }

    @Override
    public Key createKey()
    {
        Key key = (Key) get(Repository.VUOROID);
        return KeyFactory.createKey(Keys.getRootKey(), KIND, key.getId());
    }

}

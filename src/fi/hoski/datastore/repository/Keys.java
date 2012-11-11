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

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import fi.hoski.datastore.Repository;
import fi.hoski.util.Day;

/**
 * @author Timo Vesalainen
 */
public class Keys 
{
    public static Key getRootKey()
    {
        return KeyFactory.createKey(Repository.ROOT_KIND, Repository.ROOT_ID);
    }
    public static Key getTypeKey(Day date, Event.EventType type)
    {
        Key yearKey = getYearKey(date);
        Key typeKey = KeyFactory.createKey(yearKey, Repository.EVENTTYPE, type.ordinal()+1); // id can't be 0
        return typeKey;
    }
    public static Key getYearKey(Day date)
    {
        Key root = getRootKey();
        return KeyFactory.createKey(root, Repository.YEAR, date.getYear());
    }
    public static Key getYearKey(long year)
    {
        Key root = getRootKey();
        return KeyFactory.createKey(root, Repository.YEAR, year);
    }

    public static Key getAttachmentRootKey(String title)
    {
        Key root = getRootKey();
        return KeyFactory.createKey(root, Title.KIND, title);
    }
}

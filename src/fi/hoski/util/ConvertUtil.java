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
import com.google.appengine.api.datastore.KeyFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Timo Vesalainen
 */
public class ConvertUtil 
{
    public static Object convertTo(Object value, Class<?> type)
    {
        if (type.isAssignableFrom(value.getClass()))
        {
            return value;
        }
        if (Key.class.equals(type) && (value instanceof String))
        {
            return KeyFactory.stringToKey(value.toString());
        }
        if (Long.class.equals(type) && (value instanceof Number))
        {
            Number number = (Number) value;
            return number.longValue();
        }
        if (Double.class.equals(type) && (value instanceof Number))
        {
            Number number = (Number) value;
            return number.doubleValue();
        }
        if (Long.class.equals(type) && (value instanceof Day))
        {
            Day day = (Day) value;
            return day.getValue();
        }
        if (Long.class.equals(type) && (value instanceof Time))
        {
            Time time = (Time) value;
            return time.getValue();
        }
        Constructor constructor = null;
        try
        {
            constructor = type.getConstructor(value.getClass());
        }
        catch (NoSuchMethodException e)
        {
        }
        try
        {
            if (constructor == null)
            {
                constructor = type.getConstructor(String.class);
                value = value.toString();
            }
            return constructor.newInstance(value);
        }
        catch (InstantiationException ex)
        {
            throw new IllegalArgumentException("value " + value + " not convertable to "+type);
        }
        catch (IllegalAccessException ex)
        {
            throw new IllegalArgumentException("value " + value + " not convertable to "+type);
        }
        catch (IllegalArgumentException ex)
        {
            throw new IllegalArgumentException("value " + value + " not convertable to "+type);
        }
        catch (InvocationTargetException ex)
        {
            throw new IllegalArgumentException("value " + value + " not convertable to "+type);
        }
        catch (NoSuchMethodException ex)
        {
            throw new IllegalArgumentException("value " + value + " not convertable to "+type);
        }
        catch (SecurityException ex)
        {
            throw new IllegalArgumentException("value " + value + " not convertable to "+type);
        }
    }
}

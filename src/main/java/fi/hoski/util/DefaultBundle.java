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

import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * @author Timo Vesalainen
 */
public class DefaultBundle extends ResourceBundle
{
    private static final Enum ENUM = new Enum();
    
    @Override
    protected Object handleGetObject(String key)
    {
        return key;
    }

    @Override
    public Enumeration<String> getKeys()
    {
        return ENUM;
    }

    private static class Enum implements Enumeration<String> 
    {

        @Override
        public boolean hasMoreElements()
        {
            return false;
        }

        @Override
        public String nextElement()
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
}

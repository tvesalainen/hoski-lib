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
package fi.hoski.util.orc;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author tkv
 */
public class RMSHeader implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Map<RMSField,Integer> map = new HashMap<RMSField,Integer>();

    public RMSHeader(String header)
    {
        String[] ss = header.split("[ ]+");
        int offset = -1;
        for (int ii=0;ii<ss.length;ii++)
        {
            if ("LOA".equals(ss[ii]))
            {
                offset = ii;
                break;
            }
        }
        for (int ii=offset;ii<ss.length;ii++)
        {
            try
            {
                RMSField fld = RMSField.correctedValueOf(ss[ii]);
                map.put(fld, ii-offset);
            }
            catch (Exception ex)
            {
            }
        }
    }

    public int indexOf(RMSField fld)
    {
        return map.get(fld);
    }
}

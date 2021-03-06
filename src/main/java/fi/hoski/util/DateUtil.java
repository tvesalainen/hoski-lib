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

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

/**
 * @author Timo Vesalainen
 */
public class DateUtil extends ThreadLocal<DateFormat> 
{
    public static String format(Date date)
    {
        DateUtil du = new DateUtil();
        return du.get().format(date);
    }
    public static Date parse(String str) throws ParseException
    {
        DateUtil du = new DateUtil();
        return du.get().parse(str);
    }
    @Override
    protected DateFormat initialValue()
    {
        Locale fi = new Locale("fi", "FI");
        return DateFormat.getDateInstance(DateFormat.SHORT, fi);
    }

}

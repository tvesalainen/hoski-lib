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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Timo Vesalainen
 */
public class Utils
{

    private static final Pattern ROMAN = Pattern.compile("\\b(I|II|III|IV|V|VI|VII|VIII|IX|X)\\b", Pattern.CASE_INSENSITIVE);

    public static String convertName(String name)
    {
        StringBuilder sb = new StringBuilder();
        name = name.trim();
        boolean upper = true;
        for (int ii = 0; ii < name.length(); ii++)
        {
            char cc = name.charAt(ii);
            if (upper)
            {
                sb.append(Character.toUpperCase(cc));
            }
            else
            {
                sb.append(Character.toLowerCase(cc));
            }
            switch (cc)
            {
                case ' ':
                case '-':
                    upper = true;
                    break;
                default:
                    upper = false;
                    break;
            }
        }
        return romans(sb.toString());
    }

    private static String romans(String str)
    {
        Matcher m = ROMAN.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (m.find())
        {
            m.appendReplacement(sb, m.group().toUpperCase());
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public static void main(String... args)
    {
        try
        {
            System.err.println(Utils.convertName("Meri III"));
            System.err.println(Utils.convertName("Meri VIIVI"));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}

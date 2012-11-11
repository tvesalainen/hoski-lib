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

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author Timo Vesalainen
 */
public final class MonthTable 
{
    private Object[][] table;
    private EasterCalendar cal;
    private DateFormatSymbols symbols;
    private int firstDayOfWeek;
    private int wdOffset;
    private String monthName;

    public MonthTable(Locale locale, Date date)
    {
        table = new Object[7][7];
        for (int x=0;x<7;x++)
        {
            for (int y=0;y<7;y++)
            {
                table[x][y] = "";
            }
        }
        cal = new EasterCalendar();
        Date save = cal.getTime();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        firstDayOfWeek = cal.getFirstDayOfWeek();
        int fwd = cal.get(Calendar.DAY_OF_WEEK);
        wdOffset = (7 + (fwd - firstDayOfWeek)) % 7;
        symbols = DateFormatSymbols.getInstance(locale);
        monthName = symbols.getMonths()[cal.get(Calendar.MONTH)];
        for (int ii=0;ii<7;ii++)
        {
            int wd = dayOfWeek(ii);
            set(ii, 0, symbols.getShortWeekdays()[1+wd]);
        }
        cal.setTime(save);
    }

    private int dayOfWeek()
    {
        return dayOfWeek(cal.get(Calendar.DAY_OF_WEEK));
    }
    private int dayOfWeek(int weekday)
    {
        return (7 + weekday + firstDayOfWeek- 1) % 7;
    }
    public void set(int monthDay, Object o)
    {
        int d = monthDay + wdOffset - 1;
        set(d % 7, (d / 7)+1, o);
    }
    public void set(int x, int y, Object o)
    {
        table[x][y] = o;
    }
    
    public void print(PrintWriter out)
    {
        out.print("<tr><th colspan='7'>"+monthName+"</th></tr>");
        for (int y=0;y<7;y++)
        {
            if (!isEmptyLine(y))
            {
                out.print("<tr>");
                for (int x=0;x<7;x++)
                {
                    out.print("<td>"+table[x][y]+"</td>");
                }
                out.println("</tr>");
            }
        }
    }
    private boolean isEmptyLine(int y)
    {
        for (int x=0;x<7;x++)
        {
            if (!table[x][y].toString().isEmpty())
            {
                return false;
            }
        }
        return true;
    }
    public static void main(String[] args) 
    {
        try
        {
            MonthTable mt = new MonthTable(Locale.getDefault(), new Date());
            FileOutputStream fos = new FileOutputStream("c:\\temp\\t.html");
            PrintWriter pw = new PrintWriter(fos);
            pw.append("<html><head/><body><table>");
            mt.print(pw);
            pw.append("</table></body></html>");
            pw.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}

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
import java.util.*;

/**
 * @author Timo Vesalainen
 */
public class CalPrinter
{
    private Locale locale;
    private EasterCalendar cal;
    private DateFormatSymbols symbols;
    private int firstDayOfWeek;
    private String[] weekdays;

    public CalPrinter(Locale locale)
    {
        this.locale = locale;
        cal = new EasterCalendar(locale);
    }
    
    public void print(PrintWriter out, int margin, Day s, Day e)
    {
        Day now = new Day();
        now.addDays(margin);
        if (now.after(s))
        {
            s = now;
        }
        Date start = s.getDate();
        Date end = e.getDate();
        List<MonthTable> months = new ArrayList<MonthTable>();
        cal.setTime(start);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        
        while (end.after(cal.getTime()))
        {
            MonthTable mt = new MonthTable(locale, cal.getTime());
            int lastDayOfMonth = lastDayOfMonth(cal);
            for (int d=1;d<=lastDayOfMonth;d++)
            {
                cal.set(Calendar.DAY_OF_MONTH, d);
                Date dd = cal.getTime();
                Day day = new Day(dd);
                if (!dd.before(start) && !dd.after(end))
                {
                    String cls ="workday";
                    if (cal.isHolyday())
                    {
                        cls ="holyday";
                    }
                    mt.set(d, new CheckBox(String.valueOf(d), cls, "exclude", day.toString(), false, false));
                }
            }
            months.add(mt);
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        for (MonthTable mt : months)
        {
            mt.print(out);
        }
        out.close();
    }
    private int lastDayOfMonth(Calendar cal)
    {
        Date s = cal.getTime();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        int ldm = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(s);
        return ldm;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        try
        {
            CalPrinter cp = new CalPrinter(Locale.getDefault());
            FileOutputStream fos = new FileOutputStream("c:\\temp\\t.html");
            PrintWriter pw = new PrintWriter(fos);
            pw.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
            pw.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
            pw.append("<head/><body><table>");
            Set<Long> set = new HashSet<Long>();
            set.add(20120830L);
            cp.print(
                    pw, 
                    5,
                    new Day("16.4."), 
                    new Day("22.10.")
                    );
            pw.append("</table></body></html>");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}

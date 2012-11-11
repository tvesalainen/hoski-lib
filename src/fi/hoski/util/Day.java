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

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * @author Timo Vesalainen
 */
public class Day implements Comparable<Day>
{
    private static final String ERRORMSG = "Date not in form (dd.mm.yyyy | dd.mm) ";
    private int value;

    public Day()
    {
        this(new Date());
    }

    public Day(Day other)
    {
        this.value = other.value;
    }

    public Day(String str)
    {
        if (str == null || str.isEmpty())
        {
            this.value = (int) getValue(new Date());
        }
        else
        {
            Calendar cal = Calendar.getInstance();
            long year = cal.get(Calendar.YEAR);
            checkYear(year);
            long month = 0;
            long day = 0;
            String[] ss = str.split("\\.");
            switch( ss.length)
            {
                case 1:
                    init(Integer.parseInt(ss[0]));
                    break;
                case 2:
                    month = Long.parseLong(ss[1]);
                    checkMonth(month);
                    day = Long.parseLong(ss[0]);
                    checkDay(day);
                    this.value = (int) (year*10000+month*100+day);
                    check();
                    break;
                case 3:
                    if (!ss[2].isEmpty())
                    {
                        year = Long.parseLong(ss[2]);
                    }
                    month = Long.parseLong(ss[1]);
                    checkMonth(month);
                    day = Long.parseLong(ss[0]);
                    checkDay(day);
                    this.value = (int) (year*10000+month*100+day);
                    check();
                    break;
                default:
                    throw new IllegalArgumentException(ERRORMSG+str);
            }
        }
    }

    public Day(long value)
    {
        init(value);
        check();
    }

    public Day(Date date)
    {
        this.value = (int) getValue(date);
        check();
    }

    public Day(int year, int month, int day)
    {
        this.value = year*10000+month*100+day;
        check();
    }
    
    private void check()
    {
        long year = value/10000;
        long month = ((value/100) % 100);
        long day = value % 100;
        checkYear(year);
        checkMonth(month);
        checkDay(day);
    }
    private void checkYear(long year)
    {
        if (year < 1 || year > 2100)
        {
            throw new IllegalArgumentException(ERRORMSG+value);
        }
    }
    private void checkMonth(long month)
    {
        if (month < 1 || month > 12)
        {
            throw new IllegalArgumentException(ERRORMSG+value);
        }
    }
    private void checkDay(long day)
    {
        if (day < 1 || day > 31)
        {
            throw new IllegalArgumentException(ERRORMSG+value);
        }
    }
    private void init(long value)
    {
        this.value = (int)value;
        check();
    }
    
    public void addDays(int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDate());
        cal.add(Calendar.DAY_OF_MONTH, days);
        this.value = (int) getValue(cal.getTime());
        check();
    }

    public int getYear()
    {
        return value/10000;
    }
    public int getMonth()
    {
        return ((value/100) % 100);
    }
    public int getDay()
    {
        return value % 100;
    }
    public boolean after(Day other)
    {
        return value > other.value;
    }
    public boolean before(Day other)
    {
        return value < other.value;
    }
    public Date getDate()
    {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        int year = value/10000;
        int month = ((value/100) % 100)-1;
        int day = value % 100;
        cal.set(year, month, day);
        return cal.getTime();
    }
    public long getValue()
    {
        return value;
    }

    public static long getValue(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR)*10000+(cal.get(Calendar.MONTH)+1)*100+cal.get(Calendar.DAY_OF_MONTH);
    }

    public static Day getDay(Object ob)
    {
        if (ob == null)
        {
            return null;
        }
        else
        {
            if (ob instanceof Day)
            {
                return (Day) ob;
            }
            else
            {
                if (ob instanceof Long)
                {
                    return new Day((Long)ob);
                }
                else
                {
                    throw new IllegalArgumentException("cannot convert "+ob+" to Day");
                }
            }
        }
    }
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Day other = (Day) obj;
        if (this.value != other.value)
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 13 * hash + this.value;
        return hash;
    }

    @Override
    public String toString()
    {
        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        int year = value/10000;
        int month = ((value/100) % 100);
        int day = value % 100;
        if (yearNow == year)
        {
            return day+"."+month+".";
        }
        else
        {
            return day+"."+month+"."+year;
        }
    }
    
    @Override
    public int compareTo(Day o)
    {
        return value - o.value;
    }
    
    public String getShort()
    {
        int month = ((value/100) % 100);
        int day = value % 100;
        return day+"."+month;
    }

    public static void main(String... args)
    {
        try
        {
            long now = System.currentTimeMillis();
            Random rand = new Random(12345678);
            for (int ii=0;ii<1000;ii++)
            {
                long r = Math.abs(rand.nextLong()) % now;
                Date date = new Date(r);
                Day d1 = new Day(date);
                System.err.println(d1);
                Day d2 = new Day(d1.value);
                if (!d1.equals(d2))
                {
                    throw new IllegalArgumentException(d1+" != "+d2);
                }
                d2 = new Day(d1.toString());
                if (!d1.equals(d2))
                {
                    throw new IllegalArgumentException(d1+" != "+d2);
                }
                d2 = new Day(d1.getYear(), d1.getMonth(), d1.getDay());
                if (!d1.equals(d2))
                {
                    throw new IllegalArgumentException(d1+" != "+d2);
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}

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

/**
 * @author Timo Vesalainen
 */
public class Time 
{
    public static final String SEPARATOR = ":";
    private int time;   // hhmmss

    public Time()
    {
        this(new Date());
    }

    public Time(long time)
    {
        this.time = (int) time;
        check();
    }
    
    public Time(String str)
    {
        String[] ss = str.split(SEPARATOR);
        switch (ss.length)
        {
            case 1:
                if (ss[0].length() > 4)
                {
                    time = Integer.parseInt(ss[0]);
                }
                else
                {
                    time = Integer.parseInt(ss[0])*100;
                }
                break;
            case 2:
                time = 10000*Integer.parseInt(ss[0])+100*Integer.parseInt(ss[1]);
                break;
            case 3:
                time = 10000*Integer.parseInt(ss[0])+100*Integer.parseInt(ss[1])+Integer.parseInt(ss[2]);
                break;
        }
        check();
    }

    public Time(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        init(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
    }

    public Time(int hour, int minute, int second)
    {
        init(hour, minute, second);
        check();
    }

    public static Time getTime(Object ob)
    {
        if (ob == null)
        {
            return null;
        }
        else
        {
            if (ob instanceof Time)
            {
                return (Time) ob;
            }
            else
            {
                if (ob instanceof Long)
                {
                    return new Time((Long)ob);
                }
                else
                {
                    throw new IllegalArgumentException("cannot convert "+ob+" to Time");
                }
            }
        }
    }
    
    public static long getValue(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return getValue(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
    }
    
    public static long getValue(int hour, int minute, int second)
    {
        return 10000*hour+100*minute+second;
    }
    
    public long getValue()
    {
        return time;
    }
    
    private void init(int hour, int minute, int second)
    {
        time = 10000*hour+100*minute+second;
    }
    public int getHour()
    {
        return time/10000;
    }
    public int getMinute()
    {
        return (time/100) % 100;
    }
    public int getSecond()
    {
        return (time) % 100;
    }

    private void check()
    {
        if (time/10000 > 23 || time/10000 < 0)
        {
            throw new IllegalArgumentException(time+" not valid time");
        }
        if ((time/100) % 100 > 59 || (time/100) % 100 < 0)
        {
            throw new IllegalArgumentException(time+" not valid time");
        }
        if ((time) % 100 > 59 || (time) % 100 < 0)
        {
            throw new IllegalArgumentException(time+" not valid time");
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
        final Time other = (Time) obj;
        if (this.time != other.time)
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 23 * hash + this.time;
        return hash;
    }

    @Override
    public String toString()
    {
        if (getSecond() > 0)
        {
            return String.format("%02d%s%02d%s%02d", getHour(), SEPARATOR, getMinute(), SEPARATOR, getSecond());
        }
        else
        {
            return String.format("%02d%s%02d", getHour(), SEPARATOR, getMinute());
        }
    }

    public static void main(String... args)
    {
        try
        {
            Time t1 = new Time("00:00");
            Time t2 = new Time(t1.toString());
            if (!t1.equals(t2))
            {
                System.err.println(t1+" != "+t2);
            }
            System.err.println(new Time());
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public Date gateDate()
    {   // TODO
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

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

package fi.hoski.util.irc;

import au.com.bytecode.opencsv.CSVReader;
import fi.hoski.util.BoatInfo;
import fi.hoski.util.Persistence;
import fi.hoski.util.URLResource;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.*;

/**
 * @author Timo Vesalainen
 */
public class IRCCSVFile extends URLResource implements BoatInfo
{
    private static final Map<String, Object> EMPTY_MAP = new HashMap<String, Object>();
    static
    {
        EMPTY_MAP.put(BoatInfo.RATING, "");
        EMPTY_MAP.put(BoatInfo.LYS, "");
        EMPTY_MAP.put(BoatInfo.LYSVAR, "");
        EMPTY_MAP.put(BoatInfo.TCC, "");
        EMPTY_MAP.put(BoatInfo.GPH, "");
    }
    private String[] header;
    private Map<String,String[]> boatMap;
    private String csv;
    
    public IRCCSVFile(String url) throws MalformedURLException
    {
        super(url);
    }

    public IRCCSVFile(String url, Persistence persistence) throws MalformedURLException
    {
        super(url, persistence);
    }

    @Override
    protected boolean restore()
    {
        boatMap = new HashMap<String,String[]>();
        if (persistence != null)
        {
            try
            {
                csv = (String) persistence.get(url+".csv");
                if (csv != null)
                {
                    populate();
                    return true;
                }
                else
                {
                    return false;
                }
            }
            catch (IOException ex)
            {
                throw new IllegalArgumentException(ex);
            }
        }
        return false;
   }

    private void populate() throws IOException
    {
        StringReader isr = new StringReader(csv);
        CSVReader reader = new CSVReader(isr);
        header = reader.readNext();
        String[] entry = reader.readNext();
        while (entry != null)
        {
            String sailNo = get("Sail No", entry);
            if (sailNo != null)
            {
                boatMap.put(sailNo, entry);
            }
            entry = reader.readNext();
        }
        reader.close();
    }
    @Override
    protected void store()
    {
        persistence.put(url+".csv", csv);
    }

    @Override
    protected void update(InputStream in) throws IOException
    {
        boatMap.clear();
        BufferedInputStream bis = new BufferedInputStream(in);
        InputStreamReader isr = new InputStreamReader(bis, "ASCII");
        StringBuffer sb = new StringBuffer();
        char[] buf = new char[4096];
        int rc = isr.read(buf);
        while (rc > 0)
        {
            sb.append(buf, 0, rc);
            rc = isr.read(buf);
        }
        isr.close();
        csv = sb.toString();
        populate();
    }

    private String get(String field, String[] arr)
    {
        int index = getIndex(field);
        if (index != -1)
        {
            return arr[index];
        }
        return null;
    }
    private int getIndex(String field)
    {
        int index = 0;
        for (String hdr : header)
        {
            if (field.equals(hdr))
            {
                return index;
            }
            index++;
        }
        return -1;
    }
    @Override
    public Map<String, Object> getInfo(String nationality, int sailNumber)
    {
        try
        {
            refresh();
            String[] entry = boatMap.get(nationality+sailNumber);
            if (entry != null)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                for (int ii=0;ii<header.length;ii++)
                {
                    map.put(header[ii], entry[ii]);
                }
                String tcc = get("TCC", entry);
                if (tcc != null)
                {
                    map.put(BoatInfo.RATING, tcc);
                }
                String boatName = get("Boat Name", entry);
                if (boatName != null)
                {
                    map.put(BoatInfo.BOAT, boatName);
                }
                return map;
            }
            else
            {
                return EMPTY_MAP;
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return EMPTY_MAP;
        }
    }

    @Override
    public Map<String, Object> getInfo(String boatType)
    {
        return EMPTY_MAP;
    }

    @Override
    public List<String> getBoatTypes()
    {
        return EMPTY_LIST;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        try
        {
            IRCCSVFile irc = new IRCCSVFile("http://www.topyacht.com.au/rorc/data/ClubListing.csv");
            System.err.println(new Date());
            System.err.println(irc.getInfo("USA", 30));
            System.err.println(new Date());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}

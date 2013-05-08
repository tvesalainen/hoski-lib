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

package fi.hoski.util.lys;

import au.com.bytecode.opencsv.CSVReader;
import fi.hoski.util.BoatInfo;
import fi.hoski.util.URLResource;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Timo Vesalainen
 */
public class LYSBoatCSVInfo extends URLResource implements BoatInfo
{
    private static final Map<String, Object> EMPTY_MAP = new HashMap<String, Object>();
    static
    {
        EMPTY_MAP.put(BoatInfo.RATING, "");
        EMPTY_MAP.put(BoatInfo.LYS, "");
        EMPTY_MAP.put(BoatInfo.LYSVAR, "");
    }
    private String[] header;
    private Map<String,String[]> boatMap;
    
    public LYSBoatCSVInfo(String url) throws MalformedURLException
    {
        super(url);
    }

    @Override
    protected boolean restore()
    {
        if (persistence != null)
        {
            header = (String[]) persistence.get(url+".header");
            @SuppressWarnings("unchecked")
            Map<String, String[]> bm = (Map<String, String[]>) persistence.get(url+".boatMap");
            boatMap = bm;
            if (header != null && boatMap != null)
            {
                return true;
            }
        }
        boatMap = new HashMap<String,String[]>();
        return false;
    }

    @Override
    protected void store()
    {
        persistence.put(url+".header", header);
        persistence.put(url+".boatMap", boatMap);
    }

    @Override
    protected void update(InputStream in) throws IOException
    {
        boatMap.clear();
        BufferedInputStream bis = new BufferedInputStream(in);
        InputStreamReader isr = new InputStreamReader(bis, "ISO-8859-1");
        CSVReader reader = new CSVReader(isr);
        header = reader.readNext();
        String[] entry = reader.readNext();
        while (entry != null)
        {
            String nat = get("Nat", entry);
            String sailNo = get("SailNo", entry);
            if (nat != null && sailNo != null)
            {
                boatMap.put(nat+sailNo, entry);
            }
            entry = reader.readNext();
        }
        reader.close();
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
                String rating = get("Rating", entry);
                if (rating != null)
                {
                    map.put(fi.hoski.util.BoatInfo.LYS, rating);
                    map.put(fi.hoski.util.BoatInfo.RATING, rating.replace(',', '.'));
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
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        try
        {
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public Map<String, Object> getInfo(String boatType)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<String> getBoatTypes()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

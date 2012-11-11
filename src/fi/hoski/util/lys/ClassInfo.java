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
import fi.hoski.util.Persistence;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Timo Vesalainen
 */
public class ClassInfo extends URLResource implements BoatInfo
{
    private static final Map<String, Object> EMPTY_MAP = new HashMap<String, Object>();
    static
    {
        EMPTY_MAP.put(BoatInfo.RATING, "");
        EMPTY_MAP.put(BoatInfo.LYS, "");
        EMPTY_MAP.put(BoatInfo.LYSVAR, "");
    }
    private Map<String,Float> classMap;
    
    public ClassInfo(String url) throws MalformedURLException
    {
        super(url);
    }

    public ClassInfo(String url, Persistence persistence) throws MalformedURLException
    {
        super(url, persistence);
    }

    @Override
    protected boolean restore()
    {
        if (persistence != null)
        {
            @SuppressWarnings("unchecked")
            Map<String, Float> cm = (Map<String, Float>) persistence.get(url+".classMap");
            classMap = cm;
            if (classMap != null)
            {
                return true;
            }
        }
        classMap = new HashMap<String,Float>();
        return false;
    }

    @Override
    protected void store()
    {
        persistence.put(url+".classMap", classMap);
    }

    @Override
    protected void update(InputStream in) throws IOException
    {
        classMap.clear();
        BufferedInputStream bis = new BufferedInputStream(in);
        InputStreamReader isr = new InputStreamReader(bis, "ISO-8859-1");
        CSVReader reader = new CSVReader(isr);
        String[] entry = reader.readNext();
        while (entry != null)
        {
            if (entry.length == 3)
            {
                try
                {
                    classMap.put(entry[1], Float.parseFloat(entry[2]));
                }
                catch (NumberFormatException ex)
                {
                    
                }
            }
            entry = reader.readNext();
        }
        reader.close();
    }

    @Override
    public Map<String, Object> getInfo(String boatType)
    {
        try
        {
            refresh();
        }
        catch (IOException ex)
        {
            Logger.getLogger(ClassInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        Float lys = classMap.get(boatType);
        if (lys != null)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(BoatInfo.CLASS, boatType);
            map.put(BoatInfo.LYS, lys);
            map.put(BoatInfo.RATING, lys);
            return map;
        }
        else
        {
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
            ClassInfo ci = new ClassInfo("http://www.sailwave.com/download/ratings/lys.csv");
            System.err.println(ci.getInfo("Inferno 31"));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public Map<String, Object> getInfo(String nationality, int sailNumber)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<String> getBoatTypes()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

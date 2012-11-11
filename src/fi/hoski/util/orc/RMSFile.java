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

import fi.hoski.util.BoatInfo;
import fi.hoski.util.Persistence;
import fi.hoski.util.URLResource;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.CharacterCodingException;
import java.text.ParseException;
import java.util.*;

/**
 *
 * @author tkv
 */
public class RMSFile extends URLResource implements BoatInfo
{
    private static final Map<String, Object> EMPTY_MAP = new HashMap<String, Object>();
    static
    {
        EMPTY_MAP.put(BoatInfo.RATING, "");
        EMPTY_MAP.put(BoatInfo.GPH, "");
    }
    private Map<String,RMSEntry> map = new HashMap<String,RMSEntry>();
    private List<RMSEntry> list = new ArrayList<RMSEntry>();

    public RMSFile(String url) throws MalformedURLException
    {
        super(url);
    }

    public RMSFile(URL url, Persistence persistence)
    {
        super(url, persistence);
    }

    public RMSFile(String url, Persistence persistence) throws MalformedURLException
    {
        super(url, persistence);
    }

    /**
     * 
     * @param content
     * @throws IOException
     * @throws ParseException
     */
    public RMSFile(URL content) throws IOException, ParseException
    {
        super(content);
    }

    @Override
    protected boolean restore()
    {
        if (persistence != null)
        {
            @SuppressWarnings("unchecked")
            Map<String, RMSEntry> bm = (Map<String, RMSEntry>) persistence.get(url+".map");
            map = bm;
            @SuppressWarnings("unchecked")
            List<RMSEntry> bl = (List<RMSEntry>) persistence.get(url+".list");
            list = bl;
            if (map != null && list != null)
            {
                return true;
            }
        }
        map = new HashMap<String,RMSEntry>();
        list = new ArrayList<RMSEntry>();
        return false;
    }

    @Override
    protected void store()
    {
        persistence.put(url+".map", map);
        persistence.put(url+".list", list);
    }

    @Override
    protected void update(InputStream in) throws IOException
    {
        list.clear();
        map.clear();
        RMSLine header = RMSLine.readLine(in);
        RMSHeader hdr = new RMSHeader(header.toString());
        RMSLine line = RMSLine.readLine(in);
        while (!line.isEmpty())
        {
            try
            {
                RMSEntry entry = new RMSEntry(line, hdr);
                list.add(entry);
                map.put(entry.getSailNumber().toUpperCase(), entry);
                map.put(entry.getName().toUpperCase(), entry);
                line = RMSLine.readLine(in);
            }
            catch (ParseException ex)
            {
                throw new IOException(ex);
            }
            catch (CharacterCodingException ex)
            {
                throw new IOException(ex);
            }
        }
    }
    
    public RMSEntry get(String str) throws IOException
    {
        refresh();
        return map.get(str.toUpperCase());
    }

    public List<RMSEntry> getAll() throws IOException
    {
        refresh();
        return list;
    }

    @Override
    public Map<String, Object> getInfo(String nationality, int sailNumber)
    {
        RMSEntry entry;
        try
        {
            entry = get(nationality.toUpperCase()+"-"+sailNumber);
            if (entry != null)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put(BoatInfo.RATING, entry.getGPH());
                map.put(BoatInfo.HELMADDRESS, entry.getAddress1()+" "+entry.getAddress2());
                map.put(BoatInfo.CLUB, entry.getClub());
                map.put(BoatInfo.DESIGNER, entry.getDesigner());
                map.put(BoatInfo.BOAT, entry.getName());
                map.put(BoatInfo.NAT, entry.getNationality());
                map.put(BoatInfo.HELMNAME, entry.getOwner());
                map.put(BoatInfo.SAILNO, entry.getNumber());
                map.put(BoatInfo.CLASS, entry.getType());
                for (RMSField f : RMSField.values())
                {
                    map.put(f.name(), entry.get(f));
                }
                return map;
            }
            else
            {
                return EMPTY_MAP;
            }
        }
        catch (Exception ex)
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
            RMSFile rms = new RMSFile(new URL("http://www.avomeripurjehtijat.fi/certlist/FIN.rms"));
            System.err.println(rms.getInfo("FIN", 2847));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}

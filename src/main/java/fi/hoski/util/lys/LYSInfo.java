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

import fi.hoski.util.BoatInfo;
import fi.hoski.util.Persistence;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 * @author Timo Vesalainen
 */
public class LYSInfo implements BoatInfo
{
    private static final Map<String, Object> EMPTY_MAP = new HashMap<String, Object>();
    static
    {
        EMPTY_MAP.put(BoatInfo.RATING, "");
        EMPTY_MAP.put(BoatInfo.LYS, "");
        EMPTY_MAP.put(BoatInfo.LYSVAR, "");
    }
    private LYSClassHTMLInfo classInfo;
    private LYSBoatHTMLInfo boatInfo;

    public LYSInfo(String boatInfoUrl, String classInfoUrl, Persistence persistence) throws MalformedURLException, ParserConfigurationException, SAXException
    {
        if (boatInfoUrl != null)
        {
            boatInfo = new LYSBoatHTMLInfo(boatInfoUrl, persistence);
        }
        if (classInfoUrl != null)
        {
            classInfo = new LYSClassHTMLInfo(classInfoUrl, persistence);
        }
    }

    @Override
    public Map<String, Object> getInfo(String nationality, int sailNumber)
    {
        if (boatInfo != null)
        {
            return boatInfo.getInfo(nationality, sailNumber);
        }
        else
        {
            return EMPTY_MAP;
        }
    }

    @Override
    public Map<String, Object> getInfo(String boatType)
    {
        if (classInfo != null)
        {
            return classInfo.getInfo(boatType);
        }
        else
        {
            return EMPTY_MAP;
        }
    }

    @Override
    public List<String> getBoatTypes()
    {
        if (classInfo != null)
        {
            return classInfo.getBoatTypes();
        }
        else
        {
            return EMPTY_LIST;
        }
    }
    
    public static void main(String[] args) 
    {
        try
        {
            LYSInfo info = new LYSInfo("http://www.avomeripurjehtijat.fi/lysmittakirjat/?old", null, null);
            System.err.println(info.getInfo("FIN", 7937));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void refresh() throws IOException
    {
        classInfo.refresh();
        boatInfo.refresh();
    }

}

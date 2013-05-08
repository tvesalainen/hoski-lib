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

import fi.hoski.util.irc.IRCCSVFile;
import fi.hoski.util.lys.LYSInfo;
import fi.hoski.util.orc.RMSFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Timo Vesalainen
 */
public class BoatInfoFactory
{
    public static final String LYS = "LYS";
    public static final String IRC = "IRC";
    public static final String ORC = "ORC";
    public static final String UNKNOWN = "UNKNOWN";
    
    private static final Map<String, Object> EMPTY_MAP = new HashMap<String, Object>();
    static
    {
        EMPTY_MAP.put(BoatInfo.RATING, "");
        EMPTY_MAP.put(BoatInfo.LYS, "");
        EMPTY_MAP.put(BoatInfo.LYSVAR, "");
        EMPTY_MAP.put(BoatInfo.TCC, "");
        EMPTY_MAP.put(BoatInfo.GPH, "");
        EMPTY_MAP.put(BoatInfo.RATINGSYSTEM, UNKNOWN);
    }
    private static final BoatInfo EMPTYINFO = new EmptyInfo();
    
    private Map<String,BoatInfo> boatInfoMap = new HashMap<String,BoatInfo>();
    private Persistence persistence;
    private LogWrapper log;

    public BoatInfoFactory(LogWrapper log, Persistence persistence)
    {
        this.persistence = persistence;
        this.log = log;
    }
    
    public void setURL(String ratingSystem, String boatInfoUrl, String classInfoUrl)
    {
        try
        {
            if (LYS.equals(ratingSystem))
            {
                boatInfoMap.put(LYS, new LYSInfo(boatInfoUrl, classInfoUrl, persistence));
            }
            if (IRC.equals(ratingSystem))
            {
                if (boatInfoUrl != null)
                {
                    boatInfoMap.put(IRC, new IRCCSVFile(boatInfoUrl, persistence));
                }
            }
            if (ORC.equals(ratingSystem))
            {
                if (boatInfoUrl != null)
                {
                    boatInfoMap.put(ORC, new RMSFile(boatInfoUrl, persistence));
                }
            }
        }
        catch (Exception ex)
        {
            log.log(ratingSystem, ex);
        }
    }
    public void refresh(String ratingSystem) throws IOException
    {
        BoatInfo bi = boatInfoMap.get(ratingSystem);
        if (bi != null)
        {
            bi.refresh();
        }
    }
    public Map<String, Object> getMap(Map<String,String[]> map)
    {
        String ratingSystem = getParam(map, BoatInfo.RATINGSYSTEM);
        String nat = getParam(map, BoatInfo.NAT);
        String sailNo = getParam(map, BoatInfo.SAILNO);
        String type = getParam(map, BoatInfo.CLASS);
        return getMap(ratingSystem, nat, sailNo, type);
    }
    public Map<String, Object> getMap(String ratingSystem, String nat, String sailNo, String boatType)
    {
        if (ratingSystem != null)
        {
            BoatInfo bi = boatInfoMap.get(ratingSystem);
            if (bi != null)
            {
                if (nat != null && sailNo != null)
                {
                    try
                    {
                        int sailNumber = Integer.parseInt(sailNo);
                        Map<String, Object> map = bi.getInfo(nat, sailNumber);
                        if (map != null && !isEmpty(map))
                        {
                            return map;
                        }
                    }
                    catch (NumberFormatException ex)
                    {
                    }
                }
                if (boatType != null)
                {
                    Map<String, Object> map = bi.getInfo(boatType);
                    if (map != null && !map.isEmpty())
                    {
                        return map;
                    }
                }
                /*
                else
                {
                    for (String rs : boatInfoMap.keySet())
                    {
                        // find boat type from other rating system
                        if (!ratingSystem.equals(rs))
                        {
                            Map<String, Object> map2 = getMap(rs, nat, sailNo, boatType);
                            if (map2 != null && map2.containsKey(RaceEntry.CLASS))
                            {
                                Map<String, Object> map = bi.getInfo(map2.get(RaceEntry.CLASS).toString());
                                if (map != null && !map.isEmpty())
                                {
                                    return map;
                                }
                            }
                        }
                    }
                }
                */
            }
        }
        return EMPTY_MAP;
    }
    private static final boolean isEmpty(Map<String, Object> map)
    {
        for (Object o : map.values())
        {
            if (!o.toString().isEmpty())
            {
                return false;
            }
        }
        return true;
    }
    private String getParam(Map<String,String[]> map, String name)
    {
        String[] arr = map.get(name);
        if (arr != null && arr.length == 1)
        {
            return arr[0];
        }
        return null;
    }
    public BoatInfo getBoatInfoService(String ratingSystem)
    {
        BoatInfo bi = boatInfoMap.get(ratingSystem);
        if (bi != null)
        {
            return bi;
        }
        else
        {
            return EMPTYINFO;
        }
    }
    public static class EmptyInfo implements BoatInfo
    {

        @Override
        public Map<String, Object> getInfo(String nationality, int sailNumber)
        {
            return EMPTY_MAP;
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

        @Override
        public void refresh()
        {
        }
        
    }
}

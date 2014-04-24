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

import java.io.Serializable;
import java.nio.charset.CharacterCodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author tkv
 */
public class RMSEntry implements Serializable, PLSBoat
{
    public static final int KNOTS[] = {6, 8, 10, 12, 14, 16, 20};
    public static final int ANGLES[] = {52, 60, 75, 90, 110, 120, 135, 150};

    private static final long serialVersionUID = 1L;
    private static final Pattern SAILNUMBER = Pattern.compile("([a-zA-Z]+)[-]([0-9]+)");

    protected RMSHeader header;
    protected String _fileId;
    protected String _sailNumber;
    private String _nationality;
    private int _number;
    protected String _name;
    protected String _type;
    protected String _builder;
    protected String _designer;
    protected int _year;
    protected String _club;
    protected String _owner;
    protected String _address1;
    protected String _address2;
    protected String _cType;
    protected String _d;
    protected int _crewWeight;
    protected Date _date;
    protected double[] _data;
    
//    0, 17, 29, 53, 71, 89, 107, 112, 148, 184, 220, 256, 265, 267, 272, 283, 293, 299, 306, 313, 320, 326, 335, 340, 347, 354, 361, 367, 375, 382, 389, 396, 403, 410, 417, 424, 431, 438, 445, 452, 459, 466, 473, 480, 487, 494, 501, 508, 515, 522, 529, 536, 543, 550, 557, 564, 572, 579, 586, 593, 600, 607, 614, 620, 626, 632, 638, 644, 650, 656, 662, 668, 673, 679, 685, 691, 697, 704, 711, 718, 725, 732, 739, 746, 753, 760, 767, 774, 781, 788, 795, 802, 809, 816, 823, 830, 837, 844, 851, 858, 865, 872, 879, 886, 893, 900, 907, 914, 921, 928, 935, 942, 949, 956, 963, 970, 977, 984, 991, 998, 1005, 1012, 1019, 1026, 1033, 1040, 1047, 1054, 1060, 1067, 1074, 1081, 1088, 1096, 1103, 1109, 1116, 1123, 1130, 1137, 1147, 1154, 1161, 1168, 1175, 1182, 1189, 1193, 1201, 1209, 1217, 1225, 1233, 1240, 1247, 1255, 1261, 1270, 1279, 1286, 1293, 1303, 1310, 1314};
    
    protected RMSEntry()
    {

    }
    public RMSEntry(RMSLine line, RMSHeader header) throws ParseException, CharacterCodingException
    {
        this.header = header;
        _fileId = line.substring(0, 17).trim();
        _sailNumber = line.substring(17, 29).trim();
        Matcher mm = SAILNUMBER.matcher(_sailNumber);
        if (mm.matches())
        {
            _nationality = mm.group(1);
            _number = Integer.parseInt(mm.group(2));
        }
        _name = line.substring(29, 53).trim();
        _type = line.substring(53, 71).trim();
        _builder = line.substring(71, 89).trim();
        _designer = line.substring(89, 107).trim();
        _year = Integer.parseInt(line.substring(107, 112).trim());
        _club = line.substring(112, 148).trim();
        _owner = line.substring(148, 184).trim();
        byte[] bb = _owner.getBytes();
        _address1 = line.substring(184, 220).trim();
        _address2 = line.substring(220, 256).trim();
        _cType = line.substring(256, 265).trim();
        _d = line.substring(265, 267).trim();
        _crewWeight = Integer.parseInt(line.substring(267, 272).trim());
        SimpleDateFormat df = new SimpleDateFormat("dd MM yyyy HH:mm:ss");
        String date = line.substring(272, 291).trim();
        _date = df.parse(date);
        
        String data = line.substring(292).trim();
        String[] ss = data.split("[ ]+");
        _data = new double[ss.length];
        for (int ii=0;ii<ss.length;ii++)
        {
            try
            {
                _data[ii] = Double.parseDouble(ss[ii]);
            }
            catch (NumberFormatException ex)
            {
            }
        }
    }
    public RMSEntry(RMSEntry entry)
    {
        header = entry.header;
        _fileId = entry._fileId;
        _sailNumber = entry._sailNumber;
        _name = entry._name;
        _type = entry._type;
        _builder = entry._builder;
        _designer = entry._designer;
        _year = entry._year;
        _club = entry._club;
        _owner = entry._owner;
        _address1 = entry._address1;
        _address2 = entry._address2;
        _cType = entry._cType;
        _d = entry._d;
        _crewWeight = entry._crewWeight;
        _date = entry._date;
        _data = entry._data;
    }
    /**
     * General Purpose Handicap
     * @return
     */
    public double getGPH()
    {
        return get(RMSField.GPH);
    }
    /**
     * Hull Speed in Knots
     * @return
     */
    public double getHullSpeed()
    {
        double feet = getLoadWaterLine() / 0.3048;
        return 1.34*Math.sqrt(feet);
    }
    /**
     * Waterline length in meters
     * @return
     */
    public double getLoadWaterLine()
    {
        return get(RMSField.IMSL);
    }
    /**
     * Wetted surface area in square meters
     * @return
     */
    public double getWettedSurfaceArea()
    {
        return get(RMSField.WSS);
    }
    
    public double getBeatSpeed(int knots)
    {
        double beatAngle = getBeatAngle(knots);
        double beatVMG = getBeatVMG(knots);
        return beatVMG/Math.cos(Math.toRadians(beatAngle));
    }

    public double getGybeSpeed(int knots)
    {
        double gybeAngle = getGybeAngle(knots);
        double runVMG = getRunVMG(knots);
        return -runVMG/Math.cos(Math.toRadians(gybeAngle));
    }

    public double getBeatAngle(int knots)
    {
        return getKnotsValue("UA", knots);
    }
    
    public double getGybeAngle(int knots)
    {
        return getKnotsValue("DA", knots);
    }
    
    public double getBeatVMG(int knots)
    {
        return 3600 / getKnotsValue("UP", knots);
    }
    
    public double getRunVMG(int knots)
    {
        return 3600 / getKnotsValue("D", knots);
    }
    
    private double getKnotsValue(String prefix, int knots)
    {
        String id = prefix+knots;
        RMSField rmsId = RMSField.valueOf(id);
        return get(rmsId);
    }

    public double getSpeed(int degrees, int knots)
    {
        return 3600 / getTimeAllowance(degrees, knots);
    }

    public double getTimeAllowance(int degrees, int knots)
    {
        String id = "R"+degrees+knots;
        RMSField rmsId = RMSField.valueOf(id);
        return get(rmsId);
    }

    public double getCircularRandomTimeAllowance(int knots)
    {
        String id = "CR"+knots;
        RMSField rmsId = RMSField.valueOf(id);
        return get(rmsId);
    }

    public double getOlympicTimeAllowance(int knots)
    {
        String id = "OL"+knots;
        RMSField rmsId = RMSField.valueOf(id);
        return get(rmsId);
    }

    public double getWindwardLeewardTimeAllowance(int knots)
    {
        String id = "WL"+knots;
        RMSField rmsId = RMSField.valueOf(id);
        return get(rmsId);
    }

    public double getPLTOffshore()
    {
        return get(RMSField.PLTO);
    }

    public double getPLDOffshore()
    {
        return get(RMSField.PLDO);
    }

    public double getPLTInshore()
    {
        return get(RMSField.PLTI);
    }

    public double getPLDInshore()
    {
        return get(RMSField.PLDI);
    }
    /**
     * Performance Line
     * Corrected time is calculated by two coefficients as follows:
     * Corrected time = (PLT * Elapsed time) - (PLD * Distance)
     * With the time coefficient PLT and distance coefficient PLD, two boats may
     * be rated differently in light or heavy wind conditions. While the length
     * of the course is a fixed value, it is possible that one boat is giving a
     * handicap to another in light wind conditions, while the opposite may be
     * true in heavy wind conditions.
     * @param elapsedTime in seconds
     * @param distance in NM
      * @return Corrected time in seconds
     */
    public double getPerformanceLineCorrectedTimeOffshore(
            double elapsedTime,
            double distance
            )
    {
        return getPLTOffshore()*elapsedTime - getPLDOffshore()*distance;
    }
    /**
     * Performance Line
     * Corrected time is calculated by two coefficients as follows:
     * Corrected time = (PLT * Elapsed time) - (PLD * Distance)
     * With the time coefficient PLT and distance coefficient PLD, two boats may
     * be rated differently in light or heavy wind conditions. While the length
     * of the course is a fixed value, it is possible that one boat is giving a
     * handicap to another in light wind conditions, while the opposite may be
     * true in heavy wind conditions.
     * @param elapsedTime in seconds
     * @param distance in NM
      * @return Corrected time in seconds
     */
    public final double getPerformanceLineCorrectedTimeInshore(
            double elapsedTime,
            double distance
            )
    {
        return getPLTInshore()*elapsedTime - getPLDInshore()*distance;
    }
    /**
     *
     * @return
     * @see <a href="http://forum.velumng.com/viewtopic.php?f=8&t=7&p=7">ORC-Club: How PLD and PLT values are calculated</a>
     */
    public final double getPLTOffshoreCalculated()
    {
        double handicap8 = get(RMSField.OC8);
        double handicap16 = get(RMSField.OC16);
        return (510-300)/(handicap8 - handicap16);
    }
    /**
     *
     * @return
     * @see <a href="http://forum.velumng.com/viewtopic.php?f=8&t=7&p=7">ORC-Club: How PLD and PLT values are calculated</a>
     */
    public final double getPLDOffshoreCalculated()
    {
        double handicap16 = get(RMSField.OC16);
        return getPLTOffshoreCalculated()*handicap16 - 300;
    }
    /**
     *
     * @return
     * @see <a href="http://forum.velumng.com/viewtopic.php?f=8&t=7&p=7">ORC-Club: How PLD and PLT values are calculated</a>
     */
    public final double getPLTInshoreCalculated()
    {
        double handicap8 = get(RMSField.OL8);
        double handicap16 = get(RMSField.OL16);
        return (510-300)/(handicap8 - handicap16);
    }
    /**
     *
     * @return
     * @see <a href="http://forum.velumng.com/viewtopic.php?f=8&t=7&p=7">ORC-Club: How PLD and PLT values are calculated</a>
     */
    public final double getPLDInshoreCalculated()
    {
        double handicap16 = get(RMSField.OL16);
        return getPLTInshoreCalculated()*handicap16 - 300;
    }

    public final double getTimeOnTimeOffshore()
    {
        return get(RMSField.TMFOF);
    }

    public double get(RMSField idx)
    {
        return _data[header.indexOf(idx)];
    }

    public String getFileId()
    {
        return _fileId;
    }

    public String getSailNumber()
    {
        return _sailNumber;
    }

    public String getName()
    {
        return decorate(_name);
    }

    public String getType()
    {
        return _type;
    }

    public String getBuilder()
    {
        return _builder;
    }

    public String getDesigner()
    {
        return _designer;
    }

    public int getYear()
    {
        return _year;
    }

    public String getClub()
    {
        return _club;
    }

    public String getOwner()
    {
        return decorate(_owner);
    }

    public String getAddress1()
    {
        return _address1;
    }

    public String getAddress2()
    {
        return _address2;
    }

    public String getCType()
    {
        return _cType;
    }

    public String getD()
    {
        return _d;
    }

    public int getCrewWeight()
    {
        return _crewWeight;
    }

    public Date getDate()
    {
        return _date;
    }

    /**
     * @return the _nationality
     */
    public String getNationality()
    {
        return _nationality;
    }

    /**
     * @return the _number
     */
    public int getNumber()
    {
        return _number;
    }

    @Override
    public String toString()
    {
        return getSailNumber()+" "+getName();
    }

    private String decorate(String text)
    {
        StringBuilder sb = new StringBuilder();
        String[] ss = text.split(" ");
        for (String s : ss)
        {
            if (sb.length() > 0)
            {
                sb.append(" ");
            }
            if (s.length() > 2)
            {
                sb.append(s.substring(0, 1).toUpperCase()+s.substring(1).toLowerCase());
            }
            else
            {
                sb.append(s);
            }
        }
        return sb.toString();
    }
}

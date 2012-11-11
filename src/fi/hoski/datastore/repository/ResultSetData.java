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

package fi.hoski.datastore.repository;

import fi.hoski.util.Day;
import fi.hoski.util.Time;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * @author Timo Vesalainen
 */
public class ResultSetData extends MapData
{

    public ResultSetData(ResultSetModel model, ResultSet rs) throws SQLException
    {
        super(model);
        for (int col=1;col<=model.types.length;col++)
        {
            int type = model.types[col-1];
            String name = model.getProperty(col-1);
            switch (type)
            {
                case Types.BIGINT:
                    set(name, rs.getBigDecimal(col));
                    break;
                case Types.BOOLEAN:
                case Types.BIT:
                    set(name, rs.getBoolean(col));
                    break;
                case Types.DATE:
                    java.sql.Date d = rs.getDate(col);
                    if (d != null)
                    {
                        set(name, new Day(d));
                    }
                    break;
                case Types.DECIMAL:
                    set(name, rs.getFloat(col));
                    break;
                case Types.DOUBLE:
                    set(name, rs.getDouble(col));
                    break;
                case Types.FLOAT:
                    set(name, rs.getFloat(col));
                    break;
                case Types.INTEGER:
                    set(name, rs.getInt(col));
                    break;
                case Types.NUMERIC:
                    set(name, rs.getDouble(col));
                    break;
                case Types.REAL:
                    set(name, rs.getFloat(col));
                    break;
                case Types.SMALLINT:
                    set(name, rs.getInt(col));
                    break;
                case Types.TIME:
                    java.sql.Time t = rs.getTime(col);
                    if (t != null)
                    {
                        set(name, new Time(t));
                    }
                    break;
                case Types.TIMESTAMP:
                    java.sql.Timestamp ts = rs.getTimestamp(col);
                    if (ts != null)
                    {
                        set(name, new Day(ts));
                    }
                    break;
                case Types.TINYINT:
                    set(name, rs.getInt(col));
                    break;
                case Types.VARCHAR:
                case Types.LONGVARCHAR:
                    set(name, rs.getString(col));
                    break;
                default:
                    throw new IllegalArgumentException(name+" type="+type+" not supported");
            }
        }
    }

}

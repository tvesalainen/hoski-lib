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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;


/**
 * @author Timo Vesalainen
 */
public class ResultSetModel extends DataObjectModel
{
    int[] types;
    
    public ResultSetModel(ResultSet rs) throws SQLException
    {
        this(rs.getMetaData());
    }

    public ResultSetModel(ResultSetMetaData md) throws SQLException
    {
        int columnCount = md.getColumnCount();
        types = new int[columnCount];
        for (int col=1;col<=columnCount;col++)
        {
            int type = md.getColumnType(col);
            types[col-1] = type;
            String name = md.getColumnName(col);
            switch (type)
            {
                case Types.BIGINT:
                    property(name, BigDecimal.class);
                    break;
                case Types.BOOLEAN:
                case Types.BIT:
                    property(name, Boolean.class);
                    break;
                case Types.CHAR:
                    property(name, Character.class);
                    break;
                case Types.DATE:
                case Types.TIMESTAMP:
                    property(name, Day.class);
                    break;
                case Types.TIME:
                    property(name, Time.class);
                    break;
                case Types.DECIMAL:
                case Types.FLOAT:
                case Types.REAL:
                    property(name, Float.class);
                    break;
                case Types.DOUBLE:
                case Types.NUMERIC:
                    property(name, Double.class);
                    break;
                case Types.INTEGER:
                    property(name, Integer.class);
                    break;
                case Types.SMALLINT:
                case Types.TINYINT:
                    property(name, Short.class);
                    break;
                case Types.VARCHAR:
                case Types.LONGVARCHAR:
                    property(name);
                    break;
                default:
                    throw new IllegalArgumentException(name+" type="+type+" not supported");
            }
        }
    }

}

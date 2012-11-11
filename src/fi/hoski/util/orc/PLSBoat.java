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

/**
 *
 * @author tkv
 */
public interface PLSBoat
{

    String getClub();

    /**
     * General Purpose Handicap
     * @return
     */
    double getGPH();

    String getName();

    String getOwner();

    /**
     * @return the _nationality
     */
    String getNationality();

    /**
     * @return the _number
     */
    int getNumber();

    double getPLDOffshore();

    double getPLTOffshore();

    String getType();

    String getSailNumber();
}

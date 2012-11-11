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

import com.google.appengine.api.datastore.Key;

/**
 * @author Timo Vesalainen
 */
public class Boat extends DataObject
{
    public static final String KIND = "Veneet";		// String

    public static final String OMISTAJA = "Omistaja";		// String
    
    public static final String NIMI = "Nimi";		// String
    public static final String MALLI = "Malli";		// String
    public static final String MERKKI = "Merkki";		// String
    public static final String PURJETUNNUS = "Purjetunnus";		// String
    public static final String PURJENUMERO = "Purjenumero";		// int
    public static final String LEVEYS = "Leveys";		// double
    public static final String PAINO = "Paino";		// double
    public static final String PITUUS = "Pituus";		// double
    public static final String MVREKNUMERO = "MvRekNumero";		// int
    public static final String MVREKTUNNUS = "MvRekTunnus";		// String

    public static final DataObjectModel MODEL = new DataObjectModel(KIND);
    
    static
    {
        MODEL.property(NIMI);
        MODEL.property(MALLI);
        MODEL.property(MERKKI);
        MODEL.property(PURJETUNNUS);
        MODEL.property(PURJENUMERO, Long.class);
        MODEL.property(LEVEYS, Double.class);
        MODEL.property(PAINO, Double.class);
        MODEL.property(PITUUS, Double.class);
        MODEL.property(MVREKTUNNUS);
        MODEL.property(MVREKNUMERO, Long.class);
    }

    public Boat()
    {
        super(MODEL, new MapData(MODEL));
    }

    @Override
    public Key createKey()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

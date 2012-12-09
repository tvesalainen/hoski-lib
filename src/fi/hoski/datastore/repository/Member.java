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

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PhoneNumber;

/**
 * @author Timo Vesalainen
 */
public class Member extends DataObject
{
    public static final String KIND = "Jasenet";		// int

    public static final String JASENNO = "JasenNo";		// int
    public static final String SUKUNIMI = "Sukunimi";		// String
    public static final String ETUNIMI = "Etunimi";		// String
    public static final String OSOITE = "Osoite";		// String
    public static final String POSTINUMERO = "Postinumero";		// String
    public static final String POSTITOIMIPAIKKA = "Postitoimipaikka";		// String
    public static final String MOBILE = "Mobile";		// PhoneNumber
    public static final String EMAIL = "Email";		// Email

    public static final DataObjectModel MODEL = new DataObjectModel(KIND);
    
    static
    {
        MODEL.property(JASENNO, Long.class);
        MODEL.property(SUKUNIMI);
        MODEL.property(ETUNIMI);
        MODEL.property(OSOITE);
        MODEL.property(POSTINUMERO);
        MODEL.property(POSTITOIMIPAIKKA);
        MODEL.property(MOBILE, PhoneNumber.class);
        MODEL.property(EMAIL, Email.class);
    }

    public Member()
    {
        super(MODEL, new MapData(MODEL));
    }

    @Override
    public Key createKey()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

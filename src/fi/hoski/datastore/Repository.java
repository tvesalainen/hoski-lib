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
package fi.hoski.datastore;

/**
 *
 * @author Timo Vesalainen
 */
public interface Repository
{
    /**
     * Root is the kind to keep all entities in the same entity group
     */
    public static final String ROOT_KIND = "Root";
    public static final long ROOT_ID = 1;
    
    public static final String TIMESTAMP = "Timestamp";
    public static final String CREATOR = "Creator";

    public static final String TITLE = "Title";
    
    /*
     * Kind names. ResourceBundle fi/hoski/datastore/repository/fields 
     */
    public static final String MESSAGES = "Messages";
    public static final String JASENET = "Jasenet";
    public static final String JASENKOODIT = "Jasenkoodit";
    public static final String VENEET = "Veneet";
    public static final String VENETYYPPIT = "Venetyyppit";
    public static final String LAITURIPAIKKATIEDOT = "Laituripaikkatiedot";
    public static final String LAITURIPAIKAT = "Laituripaikat";
    public static final String LAITURIT = "Laiturit";
    public static final String VARTIOVUOROTIEDOT = "Vartiovuorotiedot";
    public static final String VARTIOVUOROT = "Vartiovuorot";
    public static final String KATSASTUSTIEDOT = "Katsastustiedot";
    public static final String KATSASTUSTYYPIT = "Katsastustyypit";
    public static final String CREDENTIALS = "Credentials";
    public static final String YEAR = "Year";
    public static final String EVENTTYPE = "EventType";
    public static final String EVENT = "Event";
    public static final String RESERVATION = "Reservation";
    public static final String RACESERIES = "RaceSeries";
    public static final String RACEFLEET = "RaceFleet";
    public static final String RACESTART = "RaceStart";
    
    public static final String KEYSUFFIX = ".key"; // suffix for key

    public static final String MESSAGES_KEY = MESSAGES+KEYSUFFIX;
    public static final String JASENET_KEY = JASENET+KEYSUFFIX;
    public static final String JASENKOODIT_KEY = JASENKOODIT+KEYSUFFIX;
    public static final String VENEET_KEY = VENEET+KEYSUFFIX;
    public static final String LAITURIPAIKKATIEDOT_KEY = LAITURIPAIKKATIEDOT+KEYSUFFIX;
    public static final String LAITURIPAIKAT_KEY = LAITURIPAIKAT+KEYSUFFIX;
    public static final String LAITURIT_KEY = LAITURIT+KEYSUFFIX;
    public static final String VARTIOVUOROTIEDOT_KEY = VARTIOVUOROTIEDOT+KEYSUFFIX;
    public static final String VARTIOVUOROT_KEY = VARTIOVUOROT+KEYSUFFIX;
    public static final String KATSASTUSTIEDOT_KEY = KATSASTUSTIEDOT+KEYSUFFIX;
    public static final String KATSASTUSTYYPIT_KEY = KATSASTUSTYYPIT+KEYSUFFIX;
    public static final String CREDENTIALS_KEY = CREDENTIALS+KEYSUFFIX;
    public static final String YEAR_KEY = YEAR+KEYSUFFIX;
    public static final String EVENTTYPE_KEY = EVENTTYPE+KEYSUFFIX;
    public static final String EVENT_KEY = EVENT+KEYSUFFIX;
    public static final String RESERVATION_KEY = RESERVATION+KEYSUFFIX;
    
    public static final String BOATS = "Boats";
    
    /*
     * Following fields are available from user Map. Display names for these
     * fields are available in ResourceBundle fi/hoski/datastore/repository/fields with the
     * same name.
     */
    /**
     * Object is Options<String> where labels are boat names while items are
     * keys of Veneet encoded to String
     */
    public static final String BOAT_SELECTION = "BoatSelection";
    /**
     * Object is Options<String> where labels are boat names while items are
     * keys of Veneet encoded to String
     */
    public static final String PATROL_SHIFT_SELECTION = "PatrolShiftSelection";

    public static final String FIRSTNAME = "Firstname";		// String
    public static final String INSPECTIONYEAR = "InspectionYear";		// Long
    public static final String BASICINSPECTIONYEAR = "BasicInspectionYear";		// Long
    public static final String BASICINSPECTION = "BasicInspection";             // Boolean
    public static final String NEXTBASICINSPECTION = "NextBasicInspection";     // Boolean
    
    public static final String AISA = "Aisa";		// Boolean
    public static final String ALKAA = "Alkaa";		// Date
    public static final String AMMATTI = "Ammatti";		// String
    public static final String BOKSINO = "BoksiNo";		// Double
    public static final String EMAIL = "Email";		// Email
    public static final String ETUNIMI = "Etunimi";		// String
    public static final String HENKILOT = "Henkilot";		// Long
    public static final String ID = "ID";		// Long
    public static final String JASENNO = "JasenNo";		// Long
    public static final String JASENET_AMMATTI = "Jasenet.Ammatti";		// String
    public static final String JASENET_BOKSINO = "Jasenet.BoksiNo";		// Double
    public static final String JASENET_EMAIL = "Jasenet.Email";		// Email
    public static final String JASENET_ETUNIMI = "Jasenet.Etunimi";		// String
    public static final String JASENET_JASENNO = "Jasenet.JasenNo";		// Long
    public static final String JASENET_JASENKOODI = "Jasenet.Jasenkoodi";		// Long
    public static final String JASENET_MAA = "Jasenet.Maa";		// String
    public static final String JASENET_MAATUNNUS = "Jasenet.Maatunnus";		// String
    public static final String JASENET_MOBILE = "Jasenet.Mobile";		// String
    public static final String JASENET_OSOITE = "Jasenet.Osoite";		// String
    public static final String JASENET_PELTKAAPPINO = "Jasenet.PeltkaappiNo";		// Long
    public static final String JASENET_POSTINUMERO = "Jasenet.Postinumero";		// String
    public static final String JASENET_POSTITOIMIPAIKKA = "Jasenet.Postitoimipaikka";		// String
    public static final String JASENET_PUHELIN = "Jasenet.Puhelin";		// Phonenumber
    public static final String JASENET_PUHELINTYO = "Jasenet.Puhelintyo";		// Phonenumber
    public static final String JASENET_SUKUNIMI = "Jasenet.Sukunimi";		// String
    public static final String JASENET_SUKUPUOLI = "Jasenet.Sukupuoli";		// String
    public static final String JASENET_YRITYS = "Jasenet.Yritys";		// String
    public static final String JASENKOODI = "Jasenkoodi";		// Long
    public static final String JASENKOODIT_JASENKOODI = "Jasenkoodit.Jasenkoodi";		// Long
    public static final String JASENKOODIT_NIMI = "Jasenkoodit.Nimi";		// String
    public static final String KAAPINNO = "KaapinNo";		// Double
    public static final String KAASU = "Kaasu";		// Boolean
    public static final String KANNENVARI = "Kannenvari";		// Long
    public static final String KATSASTAJA = "Katsastaja";		// Long
    public static final String KATSASTUSLUOKKA = "Katsastusluokka";		// Long
    public static final String KATSASTUSTIEDOT_ID = "Katsastustiedot.ID";		// Long
    public static final String KATSASTUSTIEDOT_KAASU = "Katsastustiedot.Kaasu";		// Boolean
    public static final String KATSASTUSTIEDOT_KATSASTAJA = "Katsastustiedot.Katsastaja";		// Long
    public static final String KATSASTUSTIEDOT_KATSASTUSLUOKKA = "Katsastustiedot.Katsastusluokka";		// Long
    public static final String KATSASTUSTIEDOT_KATSASTUSTYYPPI = "Katsastustiedot.Katsastustyyppi";		// Long
    public static final String KATSASTUSTIEDOT_PAIVA = "Katsastustiedot.Paiva";		// Date
    public static final String KATSASTUSTIEDOT_VENEID = "Katsastustiedot.VeneID";		// Long
    public static final String KATSASTUSTYYPIT_KATSASTUSTYYPPI = "Katsastustyypit.Katsastustyyppi";		// String
    public static final String KATSASTUSTYYPIT_KATSASTUSTYYPPIID = "Katsastustyypit.KatsastustyyppiID";		// Long
    public static final String KATSASTUSTYYPPI = "Katsastustyyppi";		// String
    public static final String KATSASTUSTYYPPIID = "KatsastustyyppiID";		// Long
    public static final String KORKEUS = "Korkeus";		// Double
    public static final String LAITURI = "Laituri";		// Long
    public static final String LAITURIID = "LaituriID";		// Long
    public static final String LAITURIPAIKAT_AISA = "Laituripaikat.Aisa";		// Boolean
    public static final String LAITURIPAIKAT_ID = "Laituripaikat.ID";		// Long
    public static final String LAITURIPAIKAT_LAITURI = "Laituripaikat.Laituri";		// Long
    public static final String LAITURIPAIKAT_LEVEYS = "Laituripaikat.Leveys";		// Double
    public static final String LAITURIPAIKAT_PAIKKA = "Laituripaikat.Paikka";		// Long
    public static final String LAITURIPAIKAT_PITUUS = "Laituripaikat.Pituus";		// Double
    public static final String LAITURIPAIKAT_SYVAYS = "Laituripaikat.Syvays";		// Double
    public static final String LAITURIPAIKKATIEDOT_ID = "Laituripaikkatiedot.ID";		// Long
    public static final String LAITURIPAIKKATIEDOT_LAITURIID = "Laituripaikkatiedot.LaituriID";		// Long
    public static final String LAITURIPAIKKATIEDOT_VAPAAALKU = "Laituripaikkatiedot.VapaaAlku";		// Date
    public static final String LAITURIPAIKKATIEDOT_VAPAALOPPU = "Laituripaikkatiedot.VapaaLoppu";		// Date
    public static final String LAITURIPAIKKATIEDOT_VENEID = "Laituripaikkatiedot.VeneID";		// Long
    public static final String LAITURIT_ID = "Laiturit.ID";		// Long
    public static final String LAITURIT_TUNNUS = "Laiturit.Tunnus";		// String
    public static final String LEVEYS = "Leveys";		// Double
    public static final String LOPPUU = "Loppuu";		// Date
    public static final String LUOKKA = "Luokka";		// String
    public static final String LUOKKAID = "LuokkaID";		// Long
    public static final String MAA = "Maa";		// String
    public static final String MAATUNNUS = "Maatunnus";		// String
    public static final String MAKUUPAIKAT = "Makuupaikat";		// Long
    public static final String MALLI = "Malli";		// String
    public static final String MERKKI = "Merkki";		// String
    public static final String MOBILE = "Mobile";		// String
    public static final String MVREKNUMERO = "MvRekNumero";		// Long
    public static final String MVREKTUNNUS = "MvRekTunnus";		// String
    public static final String NIMI = "Nimi";		// String
    public static final String OMISTAJA = "Omistaja";		// Long
    public static final String OSOITE = "Osoite";		// String
    public static final String PALKAA = "PAlkaa";		// Date
    public static final String PLOPPUU = "PLoppuu";		// Date
    public static final String PAIKKA = "Paikka";		// Long
    public static final String PAINO = "Paino";		// Double
    public static final String PAIVA = "Paiva";		// Date
    public static final String PELTKAAPPINO = "PeltkaappiNo";		// Long
    public static final String PITUUS = "Pituus";		// Double
    public static final String POSTINUMERO = "Postinumero";		// String
    public static final String POSTITOIMIPAIKKA = "Postitoimipaikka";		// String
    public static final String PUHELIN = "Puhelin";		// Phonenumber
    public static final String PUHELINTYO = "Puhelintyo";		// Phonenumber
    public static final String PURJENUMERO = "Purjenumero";		// Long
    public static final String PURJETUNNUS = "Purjetunnus";		// String
    public static final String RAKAINE = "RakAine";		// Long
    public static final String RAKVUOSI = "RakVuosi";		// Long
    public static final String RAKPAIKKA = "Rakpaikka";		// String
    public static final String RUNGONNUMERO = "Rungonnumero";		// String
    public static final String RUNGONVARI = "Rungonvari";		// Long
    public static final String SEPTI = "Septi";		// Boolean
    public static final String SUKUNIMI = "Sukunimi";		// String
    public static final String SUKUPUOLI = "Sukupuoli";		// String
    public static final String SUUNNITTELIJA = "Suunnittelija";		// String
    public static final String SYVAYS = "Syvays";		// Double
    public static final String TALVIPAIKKANO = "TalvipaikkaNo";		// Long
    public static final String TRAILERI = "Traileri";		// String
    public static final String TUNNUS = "Tunnus";		// String
    public static final String TYYPPI = "Tyyppi";		// String
    public static final String TYYPPIID = "TyyppiID";		// String
    public static final String VALMISTAJA = "Valmistaja";		// String
    public static final String VALMISTENUMERO = "Valmistenumero";		// String
    public static final String VAPAAALKU = "VapaaAlku";		// Date
    public static final String VAPAALOPPU = "VapaaLoppu";		// Date
    public static final String VARTIOVUOROT_ALKAA = "Vartiovuorot.Alkaa";		// Date
    public static final String VARTIOVUOROT_LOPPUU = "Vartiovuorot.Loppuu";		// Date
    public static final String VARTIOVUOROT_PALKAA = "Vartiovuorot.PAlkaa";		// Date
    public static final String VARTIOVUOROT_PLOPPUU = "Vartiovuorot.PLoppuu";		// Date
    public static final String VARTIOVUOROT_VUORONO = "Vartiovuorot.VuoroNo";		// Long
    public static final String VARTIOVUOROTIEDOT_JASENNO = "Vartiovuorotiedot.JasenNo";		// Long
    public static final String VARTIOVUOROTIEDOT_PAIVA = "Vartiovuorotiedot.Paiva";		// Date
    public static final String VARTIOVUOROTIEDOT_VUOROID = "Vartiovuorotiedot.VuoroID";		// Long
    public static final String VARTIOVUOROTIEDOT_VUORONO = "Vartiovuorotiedot.VuoroNo";		// Long
    public static final String VENEID = "VeneID";		// Long
    public static final String VENEPUKKI = "VenePukki";		// Long
    public static final String VENEET_BOKSINO = "Veneet.BoksiNo";		// Double
    public static final String VENEET_HENKILOT = "Veneet.Henkilot";		// Long
    public static final String VENEET_KAAPINNO = "Veneet.KaapinNo";		// Double
    public static final String VENEET_KAASU = "Veneet.Kaasu";		// Boolean
    public static final String VENEET_KANNENVARI = "Veneet.Kannenvari";		// Long
    public static final String VENEET_KORKEUS = "Veneet.Korkeus";		// Double
    public static final String VENEET_LEVEYS = "Veneet.Leveys";		// Double
    public static final String VENEET_LUOKKA = "Veneet.Luokka";		// Long
    public static final String VENEET_MAKUUPAIKAT = "Veneet.Makuupaikat";		// Long
    public static final String VENEET_MALLI = "Veneet.Malli";		// String
    public static final String VENEET_MERKKI = "Veneet.Merkki";		// String
    public static final String VENEET_MVREKNUMERO = "Veneet.MvRekNumero";		// Long
    public static final String VENEET_MVREKTUNNUS = "Veneet.MvRekTunnus";		// String
    public static final String VENEET_NIMI = "Veneet.Nimi";		// String
    public static final String VENEET_OMISTAJA = "Veneet.Omistaja";		// Long
    public static final String VENEET_PAINO = "Veneet.Paino";		// Double
    public static final String VENEET_PITUUS = "Veneet.Pituus";		// Double
    public static final String VENEET_PURJENUMERO = "Veneet.Purjenumero";		// Long
    public static final String VENEET_PURJETUNNUS = "Veneet.Purjetunnus";		// String
    public static final String VENEET_RAKAINE = "Veneet.RakAine";		// Long
    public static final String VENEET_RAKVUOSI = "Veneet.RakVuosi";		// Long
    public static final String VENEET_RAKPAIKKA = "Veneet.Rakpaikka";		// String
    public static final String VENEET_RUNGONNUMERO = "Veneet.Rungonnumero";		// String
    public static final String VENEET_RUNGONVARI = "Veneet.Rungonvari";		// Long
    public static final String VENEET_SEPTI = "Veneet.Septi";		// Boolean
    public static final String VENEET_SUUNNITTELIJA = "Veneet.Suunnittelija";		// String
    public static final String VENEET_TALVIPAIKKANO = "Veneet.TalvipaikkaNo";		// Long
    public static final String VENEET_TRAILERI = "Veneet.Traileri";		// String
    public static final String VENEET_TYYPPI = "Veneet.Tyyppi";		// String
    public static final String VENEET_VALMISTAJA = "Veneet.Valmistaja";		// String
    public static final String VENEET_VALMISTENUMERO = "Veneet.Valmistenumero";		// String
    public static final String VENEET_VENEID = "Veneet.VeneID";		// Long
    public static final String VENEET_VENEPUKKI = "Veneet.VenePukki";		// Long
    public static final String VENEET_VENEPAIKANNO = "Veneet.VenepaikanNo";		// Long
    public static final String VENEET_VESILINJA = "Veneet.Vesilinja";		// Double
    public static final String VENELUOKAT_LUOKKA = "Veneluokat.Luokka";		// String
    public static final String VENELUOKAT_LUOKKAID = "Veneluokat.LuokkaID";		// Long
    public static final String VENEPAIKANNO = "VenepaikanNo";		// Long
    public static final String VENETYYPPIT_TYYPPI = "Venetyyppit.Tyyppi";		// String
    public static final String VENETYYPPIT_TYYPPIID = "Venetyyppit.TyyppiID";		// String
    public static final String VESILINJA = "Vesilinja";		// Double
    public static final String VUOROID = "VuoroID";		// Long
    public static final String VUORONO = "VuoroNo";		// Long
    public static final String YRITYS = "Yritys";		// String
    public static final String[] FIELDS = new String[]
    {
        JASENET_AMMATTI,
        JASENET_BOKSINO,
        JASENET_EMAIL,
        JASENET_ETUNIMI,
        JASENET_JASENNO,
        JASENET_JASENKOODI,
        JASENET_MAA,
        JASENET_MAATUNNUS,
        JASENET_MOBILE,
        JASENET_OSOITE,
        JASENET_PELTKAAPPINO,
        JASENET_POSTINUMERO,
        JASENET_POSTITOIMIPAIKKA,
        JASENET_PUHELIN,
        JASENET_PUHELINTYO,
        JASENET_SUKUNIMI,
        JASENET_SUKUPUOLI,
        JASENET_YRITYS,
        JASENKOODIT_JASENKOODI,
        JASENKOODIT_NIMI,
        KATSASTUSTIEDOT_ID,
        KATSASTUSTIEDOT_KAASU,
        KATSASTUSTIEDOT_KATSASTAJA,
        KATSASTUSTIEDOT_KATSASTUSLUOKKA,
        KATSASTUSTIEDOT_KATSASTUSTYYPPI,
        KATSASTUSTIEDOT_PAIVA,
        KATSASTUSTIEDOT_VENEID,
        KATSASTUSTYYPIT_KATSASTUSTYYPPI,
        KATSASTUSTYYPIT_KATSASTUSTYYPPIID,
        LAITURIPAIKAT_AISA,
        LAITURIPAIKAT_ID,
        LAITURIPAIKAT_LAITURI,
        LAITURIPAIKAT_LEVEYS,
        LAITURIPAIKAT_PAIKKA,
        LAITURIPAIKAT_PITUUS,
        LAITURIPAIKAT_SYVAYS,
        LAITURIPAIKKATIEDOT_ID,
        LAITURIPAIKKATIEDOT_LAITURIID,
        LAITURIPAIKKATIEDOT_VAPAAALKU,
        LAITURIPAIKKATIEDOT_VAPAALOPPU,
        LAITURIPAIKKATIEDOT_VENEID,
        LAITURIT_ID,
        LAITURIT_TUNNUS,
        VARTIOVUOROT_ALKAA,
        VARTIOVUOROT_LOPPUU,
        VARTIOVUOROT_PALKAA,
        VARTIOVUOROT_PLOPPUU,
        VARTIOVUOROT_VUORONO,
        VARTIOVUOROTIEDOT_JASENNO,
        VARTIOVUOROTIEDOT_PAIVA,
        VARTIOVUOROTIEDOT_VUOROID,
        VARTIOVUOROTIEDOT_VUORONO,
        VENEET_BOKSINO,
        VENEET_HENKILOT,
        VENEET_KAAPINNO,
        VENEET_KAASU,
        VENEET_KANNENVARI,
        VENEET_KORKEUS,
        VENEET_LEVEYS,
        VENEET_LUOKKA,
        VENEET_MAKUUPAIKAT,
        VENEET_MALLI,
        VENEET_MERKKI,
        VENEET_MVREKNUMERO,
        VENEET_MVREKTUNNUS,
        VENEET_NIMI,
        VENEET_OMISTAJA,
        VENEET_PAINO,
        VENEET_PITUUS,
        VENEET_PURJENUMERO,
        VENEET_PURJETUNNUS,
        VENEET_RAKAINE,
        VENEET_RAKVUOSI,
        VENEET_RAKPAIKKA,
        VENEET_RUNGONNUMERO,
        VENEET_RUNGONVARI,
        VENEET_SEPTI,
        VENEET_SUUNNITTELIJA,
        VENEET_TALVIPAIKKANO,
        VENEET_TRAILERI,
        VENEET_TYYPPI,
        VENEET_VALMISTAJA,
        VENEET_VALMISTENUMERO,
        VENEET_VENEID,
        VENEET_VENEPUKKI,
        VENEET_VENEPAIKANNO,
        VENEET_VESILINJA,
        VENELUOKAT_LUOKKA,
        VENELUOKAT_LUOKKAID,
        VENETYYPPIT_TYYPPI,
        VENETYYPPIT_TYYPPIID
    };

}

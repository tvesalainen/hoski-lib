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

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import fi.hoski.datastore.Repository;
import fi.hoski.util.orc.PLSBoat;
import java.util.Date;

/**
 * @author Timo Vesalainen
 */
public class RaceEntry extends DataObject implements PLSBoat
{

    public static final String KIND = "RaceEntry";

    public static final String TIMESTAMP = Repository.TIMESTAMP;
    
    // together these fields will form HelmName
    public static final String FIRSTNAME = "Etunimi";
    public static final String LASTNAME = "Sukunimi";
    
    // together these fields will form HelmAddress
    public static final String STREETADDRESS = "Katuosoite";
    public static final String ZIPCODE = "Postinumero";
    public static final String CITY = "Postitoimipaikka";
    public static final String COUNTRY = "Maa";
    
    // ORC
    public static final String PLTO = "PLTO";
    public static final String PLDO = "PLDO";
    public static final String PLTI = "PLTI";
    public static final String PLDI = "PLDI";
    
    public static final String BOAT = "Boat";
    public static final String BOWNUMBER = "BowNumber";
    public static final String CARRIEDFWD = "CarriedFwd";
    public static final String CLASS = "Class";
    public static final String CLUB = "Club";
    public static final String CREWNAME = "CrewName";
    public static final String DIVISION = "Division";
    public static final String EXCLUDE = "Exclude";
    public static final String FEE = "Fee";
    public static final String FLEET = "Fleet";
    public static final String HELMNAME = "HelmName";
    public static final String MEDICAL = "Medical";
    public static final String MEDICALFLAG = "MedicalFlag";
    public static final String NAT = "Nat";
    public static final String NETT = "Nett";
    public static final String NOTES = "Notes";
    public static final String PAID = "Paid";
    public static final String PENALTIES = "Penalties";
    public static final String PRIVATENOTES = "PrivateNotes";
    public static final String RANK = "Rank";
    public static final String RATING = "Rating";
    public static final String SAILNO = "SailNo";
    public static final String STATUS = "Status";
    public static final String TALLY = "Tally";
    public static final String TOTAL = "Total";
    public static final String ALIAS = "Alias";
    public static final String FLIGHT = "Flight";
    public static final String SEEDING = "Seeding";
    public static final String WINDRATS = "WindRats";
    public static final String FORMULA = "Formula";
    public static final String PLATFORM = "Platform";
    public static final String RIG = "Rig";
    public static final String SAILMAKER = "Sailmaker";
    public static final String FOILS = "Foils";
    public static final String HELMID = "HelmId";
    public static final String HELMAGEGROUP = "HelmAgeGroup";
    public static final String HELMEMAIL = "HelmEmail";
    public static final String HELMWEBSITE = "HelmWebsite";
    public static final String HELMSEX = "HelmSex";
    public static final String HELMMEMBERNO = "HelmMemberNo";
    public static final String HELMPHONE = "HelmPhone";
    public static final String HELMPHOTO = "HelmPhoto";
    public static final String HELMADDRESS = "HelmAddress";
    public static final String HELMNOTES = "HelmNotes";
    public static final String CREWID = "CrewId";
    public static final String CREWAGEGROUP = "CrewAgeGroup";
    public static final String CREWEMAIL = "CrewEmail";
    public static final String CREWWEBSITE = "CrewWebsite";
    public static final String CREWSEX = "CrewSex";
    public static final String CREWMEMBERNO = "CrewMemberNo";
    public static final String CREWPHONE = "CrewPhone";
    public static final String CREWPHOTO = "CrewPhoto";
    public static final String CREWADDRESS = "CrewAddress";
    public static final String CREWNOTES = "CrewNotes";
    public static final String SKIPPERNAME = "SkipperName";
    public static final String SKIPPERID = "SkipperId";
    public static final String SKIPPERAGEGROUP = "SkipperAgeGroup";
    public static final String SKIPPEREMAIL = "SkipperEmail";
    public static final String SKIPPERWEBSITE = "SkipperWebsite";
    public static final String SKIPPERSEX = "SkipperSex";
    public static final String SKIPPERMEMBERNO = "SkipperMemberNo";
    public static final String SKIPPERPHONE = "SkipperPhone";
    public static final String SKIPPERPHOTO = "SkipperPhoto";
    public static final String SKIPPERADDRESS = "SkipperAddress";
    public static final String SKIPPERNOTES = "SkipperNotes";
    public static final String TACTICIANNAME = "TacticianName";
    public static final String TACTICIANID = "TacticianId";
    public static final String TACTICIANAGEGROUP = "TacticianAgeGroup";
    public static final String TACTICIANEMAIL = "TacticianEmail";
    public static final String TACTICIANWEBSITE = "TacticianWebsite";
    public static final String TACTICIANSEX = "TacticianSex";
    public static final String TACTICIANMEMBERNO = "TacticianMemberNo";
    public static final String TACTICIANPHONE = "TacticianPhone";
    public static final String TACTICIANPHOTO = "TacticianPhoto";
    public static final String TACTICIANADDRESS = "TacticianAddress";
    public static final String TACTICIANNOTES = "TacticianNotes";
    public static final String NAVIGATORNAME = "NavigatorName";
    public static final String NAVIGATORID = "NavigatorId";
    public static final String NAVIGATORAGEGROUP = "NavigatorAgeGroup";
    public static final String NAVIGATOREMAIL = "NavigatorEmail";
    public static final String NAVIGATORWEBSITE = "NavigatorWebsite";
    public static final String NAVIGATORSEX = "NavigatorSex";
    public static final String NAVIGATORMEMBERNO = "NavigatorMemberNo";
    public static final String NAVIGATORPHONE = "NavigatorPhone";
    public static final String NAVIGATORPHOTO = "NavigatorPhoto";
    public static final String NAVIGATORADDRESS = "NavigatorAddress";
    public static final String NAVIGATORNOTES = "NavigatorNotes";
    public static final String BOWMANNAME = "BowmanName";
    public static final String BOWMANID = "BowmanId";
    public static final String BOWMANAGEGROUP = "BowmanAgeGroup";
    public static final String BOWMANEMAIL = "BowmanEmail";
    public static final String BOWMANWEBSITE = "BowmanWebsite";
    public static final String BOWMANSEX = "BowmanSex";
    public static final String BOWMANMEMBERNO = "BowmanMemberNo";
    public static final String BOWMANPHONE = "BowmanPhone";
    public static final String BOWMANPHOTO = "BowmanPhoto";
    public static final String BOWMANADDRESS = "BowmanAddress";
    public static final String BOWMANNOTES = "BowmanNotes";
    public static final String MASTNAME = "MastName";
    public static final String MASTID = "MastId";
    public static final String MASTAGEGROUP = "MastAgeGroup";
    public static final String MASTEMAIL = "MastEmail";
    public static final String MASTWEBSITE = "MastWebsite";
    public static final String MASTSEX = "MastSex";
    public static final String MASTMEMBERNO = "MastMemberNo";
    public static final String MASTPHONE = "MastPhone";
    public static final String MASTPHOTO = "MastPhoto";
    public static final String MASTADDRESS = "MastAddress";
    public static final String MASTNOTES = "MastNotes";
    public static final String PITNAME = "PitName";
    public static final String PITID = "PitId";
    public static final String PITAGEGROUP = "PitAgeGroup";
    public static final String PITEMAIL = "PitEmail";
    public static final String PITWEBSITE = "PitWebsite";
    public static final String PITSEX = "PitSex";
    public static final String PITMEMBERNO = "PitMemberNo";
    public static final String PITPHONE = "PitPhone";
    public static final String PITPHOTO = "PitPhoto";
    public static final String PITADDRESS = "PitAddress";
    public static final String PITNOTES = "PitNotes";
    public static final String MAINTRIMNAME = "MaintrimName";
    public static final String MAINTRIMID = "MaintrimId";
    public static final String MAINTRIMAGEGROUP = "MaintrimAgeGroup";
    public static final String MAINTRIMEMAIL = "MaintrimEmail";
    public static final String MAINTRIMWEBSITE = "MaintrimWebsite";
    public static final String MAINTRIMSEX = "MaintrimSex";
    public static final String MAINTRIMMEMBERNO = "MaintrimMemberNo";
    public static final String MAINTRIMPHONE = "MaintrimPhone";
    public static final String MAINTRIMPHOTO = "MaintrimPhoto";
    public static final String MAINTRIMADDRESS = "MaintrimAddress";
    public static final String MAINTRIMNOTES = "MaintrimNotes";
    public static final String PORTTRIMNAME = "PorttrimName";
    public static final String PORTTRIMID = "PorttrimId";
    public static final String PORTTRIMAGEGROUP = "PorttrimAgeGroup";
    public static final String PORTTRIMEMAIL = "PorttrimEmail";
    public static final String PORTTRIMWEBSITE = "PorttrimWebsite";
    public static final String PORTTRIMSEX = "PorttrimSex";
    public static final String PORTTRIMMEMBERNO = "PorttrimMemberNo";
    public static final String PORTTRIMPHONE = "PorttrimPhone";
    public static final String PORTTRIMPHOTO = "PorttrimPhoto";
    public static final String PORTTRIMADDRESS = "PorttrimAddress";
    public static final String PORTTRIMNOTES = "PorttrimNotes";
    public static final String STARTRIMNAME = "StartrimName";
    public static final String STARTRIMID = "StartrimId";
    public static final String STARTRIMAGEGROUP = "StartrimAgeGroup";
    public static final String STARTRIMEMAIL = "StartrimEmail";
    public static final String STARTRIMWEBSITE = "StartrimWebsite";
    public static final String STARTRIMSEX = "StartrimSex";
    public static final String STARTRIMMEMBERNO = "StartrimMemberNo";
    public static final String STARTRIMPHONE = "StartrimPhone";
    public static final String STARTRIMPHOTO = "StartrimPhoto";
    public static final String STARTRIMADDRESS = "StartrimAddress";
    public static final String STARTRIMNOTES = "StartrimNotes";
    public static final String PORTGRINDNAME = "PortgrindName";
    public static final String PORTGRINDID = "PortgrindId";
    public static final String PORTGRINDAGEGROUP = "PortgrindAgeGroup";
    public static final String PORTGRINDEMAIL = "PortgrindEmail";
    public static final String PORTGRINDWEBSITE = "PortgrindWebsite";
    public static final String PORTGRINDSEX = "PortgrindSex";
    public static final String PORTGRINDMEMBERNO = "PortgrindMemberNo";
    public static final String PORTGRINDPHONE = "PortgrindPhone";
    public static final String PORTGRINDPHOTO = "PortgrindPhoto";
    public static final String PORTGRINDADDRESS = "PortgrindAddress";
    public static final String PORTGRINDNOTES = "PortgrindNotes";
    public static final String STARGRINDNAME = "StargrindName";
    public static final String STARGRINDID = "StargrindId";
    public static final String STARGRINDAGEGROUP = "StargrindAgeGroup";
    public static final String STARGRINDEMAIL = "StargrindEmail";
    public static final String STARGRINDWEBSITE = "StargrindWebsite";
    public static final String STARGRINDSEX = "StargrindSex";
    public static final String STARGRINDMEMBERNO = "StargrindMemberNo";
    public static final String STARGRINDPHONE = "StargrindPhone";
    public static final String STARGRINDPHOTO = "StargrindPhoto";
    public static final String STARGRINDADDRESS = "StargrindAddress";
    public static final String STARGRINDNOTES = "StargrindNotes";
    public static final String OWNERNAME = "OwnerName";
    public static final String OWNERID = "OwnerId";
    public static final String OWNERAGEGROUP = "OwnerAgeGroup";
    public static final String OWNEREMAIL = "OwnerEmail";
    public static final String OWNERWEBSITE = "OwnerWebsite";
    public static final String OWNERSEX = "OwnerSex";
    public static final String OWNERMEMBERNO = "OwnerMemberNo";
    public static final String OWNERPHONE = "OwnerPhone";
    public static final String OWNERPHOTO = "OwnerPhoto";
    public static final String OWNERADDRESS = "OwnerAddress";
    public static final String OWNERNOTES = "OwnerNotes";
    public static final String GUESTNAME = "GuestName";
    public static final String GUESTID = "GuestId";
    public static final String GUESTAGEGROUP = "GuestAgeGroup";
    public static final String GUESTEMAIL = "GuestEmail";
    public static final String GUESTWEBSITE = "GuestWebsite";
    public static final String GUESTSEX = "GuestSex";
    public static final String GUESTMEMBERNO = "GuestMemberNo";
    public static final String GUESTPHONE = "GuestPhone";
    public static final String GUESTPHOTO = "GuestPhoto";
    public static final String GUESTADDRESS = "GuestAddress";
    public static final String GUESTNOTES = "GuestNotes";
    public static final String DISTRICT = "District";
    public static final String SQUAD = "Squad";
    public static final String AREA = "Area";
    public static final String REGION = "Region";
    public static final String GROUP = "Group";
    public static final String MEDALIST = "Medalist";
    public static final String SPONSOR = "Sponsor";
    public static final String SPONSORWEBSITE = "SponsorWebsite";
    public static final String SPONSOREMAIL = "SponsorEmail";
    public static final String TEAM = "Team";
    public static final String TEAMWEBSITE = "TeamWebsite";
    public static final String TEAMEMAIL = "TeamEmail";
    
    public static final DataObjectModel MODEL = new DataObjectModel(KIND);

    static
    {
        MODEL.property(NAT);
        MODEL.property(SAILNO, Integer.class, true, false, 0);
        MODEL.property(BOAT);
        MODEL.property(CLASS);
        MODEL.property(CLUB);
        MODEL.property(HELMNAME);
        MODEL.property(HELMEMAIL);
        MODEL.property(HELMPHONE);
        MODEL.property(HELMADDRESS);
        
        MODEL.property(FEE, Double.class, true, false, 0.0);
        MODEL.property(PAID, Double.class, true, false, 0.0);
        
        MODEL.property(TIMESTAMP, Date.class);
        
        MODEL.property(BOWNUMBER);
        MODEL.property(CARRIEDFWD);
        MODEL.property(CREWNAME);
        MODEL.property(DIVISION);
        MODEL.property(EXCLUDE);
        MODEL.property(FLEET);
        MODEL.property(MEDICAL);
        MODEL.property(MEDICALFLAG);
        MODEL.property(NETT);
        MODEL.property(NOTES);
        MODEL.property(PENALTIES);
        MODEL.property(PRIVATENOTES);
        MODEL.property(RANK);
        MODEL.property(RATING);
        MODEL.property(STATUS);
        MODEL.property(TALLY);
        MODEL.property(TOTAL);
        MODEL.property(ALIAS);
        MODEL.property(FLIGHT);
        MODEL.property(SEEDING);
        MODEL.property(WINDRATS);
        MODEL.property(FORMULA);
        MODEL.property(PLATFORM);
        MODEL.property(RIG);
        MODEL.property(SAILMAKER);
        MODEL.property(FOILS);
        MODEL.property(HELMID);
        MODEL.property(HELMAGEGROUP);
        MODEL.property(HELMWEBSITE);
        MODEL.property(HELMSEX);
        MODEL.property(HELMMEMBERNO);
        MODEL.property(HELMPHOTO);
        MODEL.property(HELMNOTES);
        MODEL.property(CREWID);
        MODEL.property(CREWAGEGROUP);
        MODEL.property(CREWEMAIL);
        MODEL.property(CREWWEBSITE);
        MODEL.property(CREWSEX);
        MODEL.property(CREWMEMBERNO);
        MODEL.property(CREWPHONE);
        MODEL.property(CREWPHOTO);
        MODEL.property(CREWADDRESS);
        MODEL.property(CREWNOTES);
        MODEL.property(SKIPPERNAME);
        MODEL.property(SKIPPERID);
        MODEL.property(SKIPPERAGEGROUP);
        MODEL.property(SKIPPEREMAIL);
        MODEL.property(SKIPPERWEBSITE);
        MODEL.property(SKIPPERSEX);
        MODEL.property(SKIPPERMEMBERNO);
        MODEL.property(SKIPPERPHONE);
        MODEL.property(SKIPPERPHOTO);
        MODEL.property(SKIPPERADDRESS);
        MODEL.property(SKIPPERNOTES);
        MODEL.property(TACTICIANNAME);
        MODEL.property(TACTICIANID);
        MODEL.property(TACTICIANAGEGROUP);
        MODEL.property(TACTICIANEMAIL);
        MODEL.property(TACTICIANWEBSITE);
        MODEL.property(TACTICIANSEX);
        MODEL.property(TACTICIANMEMBERNO);
        MODEL.property(TACTICIANPHONE);
        MODEL.property(TACTICIANPHOTO);
        MODEL.property(TACTICIANADDRESS);
        MODEL.property(TACTICIANNOTES);
        MODEL.property(NAVIGATORNAME);
        MODEL.property(NAVIGATORID);
        MODEL.property(NAVIGATORAGEGROUP);
        MODEL.property(NAVIGATOREMAIL);
        MODEL.property(NAVIGATORWEBSITE);
        MODEL.property(NAVIGATORSEX);
        MODEL.property(NAVIGATORMEMBERNO);
        MODEL.property(NAVIGATORPHONE);
        MODEL.property(NAVIGATORPHOTO);
        MODEL.property(NAVIGATORADDRESS);
        MODEL.property(NAVIGATORNOTES);
        MODEL.property(BOWMANNAME);
        MODEL.property(BOWMANID);
        MODEL.property(BOWMANAGEGROUP);
        MODEL.property(BOWMANEMAIL);
        MODEL.property(BOWMANWEBSITE);
        MODEL.property(BOWMANSEX);
        MODEL.property(BOWMANMEMBERNO);
        MODEL.property(BOWMANPHONE);
        MODEL.property(BOWMANPHOTO);
        MODEL.property(BOWMANADDRESS);
        MODEL.property(BOWMANNOTES);
        MODEL.property(MASTNAME);
        MODEL.property(MASTID);
        MODEL.property(MASTAGEGROUP);
        MODEL.property(MASTEMAIL);
        MODEL.property(MASTWEBSITE);
        MODEL.property(MASTSEX);
        MODEL.property(MASTMEMBERNO);
        MODEL.property(MASTPHONE);
        MODEL.property(MASTPHOTO);
        MODEL.property(MASTADDRESS);
        MODEL.property(MASTNOTES);
        MODEL.property(PITNAME);
        MODEL.property(PITID);
        MODEL.property(PITAGEGROUP);
        MODEL.property(PITEMAIL);
        MODEL.property(PITWEBSITE);
        MODEL.property(PITSEX);
        MODEL.property(PITMEMBERNO);
        MODEL.property(PITPHONE);
        MODEL.property(PITPHOTO);
        MODEL.property(PITADDRESS);
        MODEL.property(PITNOTES);
        MODEL.property(MAINTRIMNAME);
        MODEL.property(MAINTRIMID);
        MODEL.property(MAINTRIMAGEGROUP);
        MODEL.property(MAINTRIMEMAIL);
        MODEL.property(MAINTRIMWEBSITE);
        MODEL.property(MAINTRIMSEX);
        MODEL.property(MAINTRIMMEMBERNO);
        MODEL.property(MAINTRIMPHONE);
        MODEL.property(MAINTRIMPHOTO);
        MODEL.property(MAINTRIMADDRESS);
        MODEL.property(MAINTRIMNOTES);
        MODEL.property(PORTTRIMNAME);
        MODEL.property(PORTTRIMID);
        MODEL.property(PORTTRIMAGEGROUP);
        MODEL.property(PORTTRIMEMAIL);
        MODEL.property(PORTTRIMWEBSITE);
        MODEL.property(PORTTRIMSEX);
        MODEL.property(PORTTRIMMEMBERNO);
        MODEL.property(PORTTRIMPHONE);
        MODEL.property(PORTTRIMPHOTO);
        MODEL.property(PORTTRIMADDRESS);
        MODEL.property(PORTTRIMNOTES);
        MODEL.property(STARTRIMNAME);
        MODEL.property(STARTRIMID);
        MODEL.property(STARTRIMAGEGROUP);
        MODEL.property(STARTRIMEMAIL);
        MODEL.property(STARTRIMWEBSITE);
        MODEL.property(STARTRIMSEX);
        MODEL.property(STARTRIMMEMBERNO);
        MODEL.property(STARTRIMPHONE);
        MODEL.property(STARTRIMPHOTO);
        MODEL.property(STARTRIMADDRESS);
        MODEL.property(STARTRIMNOTES);
        MODEL.property(PORTGRINDNAME);
        MODEL.property(PORTGRINDID);
        MODEL.property(PORTGRINDAGEGROUP);
        MODEL.property(PORTGRINDEMAIL);
        MODEL.property(PORTGRINDWEBSITE);
        MODEL.property(PORTGRINDSEX);
        MODEL.property(PORTGRINDMEMBERNO);
        MODEL.property(PORTGRINDPHONE);
        MODEL.property(PORTGRINDPHOTO);
        MODEL.property(PORTGRINDADDRESS);
        MODEL.property(PORTGRINDNOTES);
        MODEL.property(STARGRINDNAME);
        MODEL.property(STARGRINDID);
        MODEL.property(STARGRINDAGEGROUP);
        MODEL.property(STARGRINDEMAIL);
        MODEL.property(STARGRINDWEBSITE);
        MODEL.property(STARGRINDSEX);
        MODEL.property(STARGRINDMEMBERNO);
        MODEL.property(STARGRINDPHONE);
        MODEL.property(STARGRINDPHOTO);
        MODEL.property(STARGRINDADDRESS);
        MODEL.property(STARGRINDNOTES);
        MODEL.property(OWNERNAME);
        MODEL.property(OWNERID);
        MODEL.property(OWNERAGEGROUP);
        MODEL.property(OWNEREMAIL);
        MODEL.property(OWNERWEBSITE);
        MODEL.property(OWNERSEX);
        MODEL.property(OWNERMEMBERNO);
        MODEL.property(OWNERPHONE);
        MODEL.property(OWNERPHOTO);
        MODEL.property(OWNERADDRESS);
        MODEL.property(OWNERNOTES);
        MODEL.property(GUESTNAME);
        MODEL.property(GUESTID);
        MODEL.property(GUESTAGEGROUP);
        MODEL.property(GUESTEMAIL);
        MODEL.property(GUESTWEBSITE);
        MODEL.property(GUESTSEX);
        MODEL.property(GUESTMEMBERNO);
        MODEL.property(GUESTPHONE);
        MODEL.property(GUESTPHOTO);
        MODEL.property(GUESTADDRESS);
        MODEL.property(GUESTNOTES);
        MODEL.property(DISTRICT);
        MODEL.property(SQUAD);
        MODEL.property(AREA);
        MODEL.property(REGION);
        MODEL.property(GROUP);
        MODEL.property(MEDALIST);
        MODEL.property(SPONSOR);
        MODEL.property(SPONSORWEBSITE);
        MODEL.property(SPONSOREMAIL);
        MODEL.property(TEAM);
        MODEL.property(TEAMWEBSITE);
        MODEL.property(TEAMEMAIL);
        MODEL.property(PLTO);
        MODEL.property(PLDO);
        MODEL.property(PLTI);
        MODEL.property(PLDI);
    }

    public RaceEntry(DataObject parent)
    {
        super(new MapData(MODEL));
        this.parent = parent;
    }

    public RaceEntry(RaceFleet raceFleet, Entity entity)
    {
        super(new EntityData(entity, MODEL));
        this.parent = raceFleet;
    }

    
    @Override
    public Key createKey()
    {
        Entity entity = getEntity();
        if (entity != null)
        {
            return entity.getKey();
        }
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Entity createEntity()
    {
        return new Entity(KIND, parent.createKey());
    }

    @Override
    public String getClub()
    {
        return (String) get(RaceEntry.CLUB);
    }

    @Override
    public double getGPH()
    {
        return Double.parseDouble((String) get(RaceEntry.RATING));
    }

    @Override
    public String getName()
    {
        return (String) get(RaceEntry.BOAT);
    }

    @Override
    public String getOwner()
    {
        return (String) get(RaceEntry.HELMNAME);
    }

    @Override
    public String getNationality()
    {
        return (String) get(RaceEntry.NAT);
    }

    @Override
    public int getNumber()
    {
        Long l = (Long) get(RaceEntry.SAILNO);
        return l.intValue();
    }

    @Override
    public double getPLDOffshore()
    {
        return Double.parseDouble((String) get(RaceEntry.PLDO));
    }

    @Override
    public double getPLTOffshore()
    {
        return Double.parseDouble((String) get(RaceEntry.PLTO));
    }

    @Override
    public String getType()
    {
        return (String) get(RaceEntry.CLASS);
    }

    @Override
    public String getSailNumber()
    {
        return (String) get(RaceEntry.SAILNO);
    }

    public RaceFleet getRaceFleet()
    {
        return (RaceFleet) parent;
    }
    
    public RaceSeries getRaceSeries()
    {
        return getRaceFleet().getRaceSeries();
    }
}

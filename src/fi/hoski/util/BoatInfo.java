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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Timo Vesalainen
 */
public interface BoatInfo
{
    public static final String RATINGSYSTEM = "RatingSystem";
    // Key field names (compatible with sailwave)
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
    // extra
    public static final String TCC = "TCC";
    public static final String GPH = "GPH";
    public static final String DESIGNER = "Designer";
    public static final String LYS = "LYS";
    public static final String LYSVAR = "LYSVAR";   // LYS varustus
    
    public static final List<String> EMPTY_LIST = new ArrayList<String>();
    
    /**
     * Returns boat info for sailnumber. 
     * @param nationality
     * @param sailNumber
     * @return 
     */
    Map<String,Object> getInfo(String nationality, int sailNumber);
    /**
     * Returns boat info for boat type. 
     * @param boatType
     * @return 
     */
    Map<String,Object> getInfo(String boatType);
    /*
     * Returns a list of known boat types
     */
    List<String> getBoatTypes();
    
    void refresh() throws IOException;
}

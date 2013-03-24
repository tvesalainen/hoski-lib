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
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import fi.hoski.datastore.Repository;
import fi.hoski.datastore.repository.Event.EventType;

/**
 * TODO reloading after changes has to be solved!!!
 * @author Timo Vesalainen
 */
public class Messages  extends DataObject
{
    public static final String KIND = Repository.MESSAGES;
    
    public static final String NAME = "fi.hoski.web.Messages";
    
    public static final String PASSWORDFROMADDRESS = "passwordFromAddress";
    public static final String PASSWORDMESSAGEBODY = "passwordMessageBody";
    public static final String PASSWORDMESSAGESUBJECT = "passwordMessageSubject";
    
    public static final String RESERVATIONMESSAGESUBJECT = "reservationMessageSubject";
    public static final String RESERVATIONMESSAGEBODY = "reservationMessageBody";
    
    public static final String PATROLSHIFTSWAPSUBJECT = "PatrolShiftSwapSubject";
    public static final String PATROLSHIFTSWAPSUCCESS = "PatrolShiftSwapSuccess";
    public static final String PATROLSHIFTSWAPQUEUED = "PatrolShiftSwapQueued";
    public static final String PATROLSHIFTSWAPREMOVED = "PatrolShiftSwapRemoved";
    public static final String PATROLSHIFTSWAPEXPIREDSUBJECT = "PatrolShiftSwapExpiredSubject";
    public static final String PATROLSHIFTSWAPEXPIRED = "PatrolShiftSwapExpired";
    
    public static final String RACEENTRYCONFIRMATION = "RaceEntryConfirmation";
    public static final String RACEENTRYPAYING = "RaceEntryPaying";
    public static final String RACEENTRYSUBJECT = "RaceEntrySubject";
    public static final String RACEENTRYFROMADDRESS = "RaceEntryFromAddress";
    public static final String RACEBANKACCOUNT = "RaceBankAccount";
    public static final String RACEBIC = "RaceBIC";
    
    public static final String IRCINFOURL = "IRCInfoURL";
    public static final String LYSINFOURL = "LYSInfoURL";
    public static final String LYSCLASSINFOURL = "LYSClassInfoURL";
    public static final String ORCINFOURL = "ORCInfoURL";
    public static final String SMSUSERNAME = "smsUsername";
    public static final String SMSPASSWORD = "smsPassword";
    
    public static final String TEXT = "Text";

    public static final DataObjectModel MODEL = new DataObjectModel(KIND);
    
    static
    {
        MODEL.property(PASSWORDFROMADDRESS);
        MODEL.property(PASSWORDMESSAGEBODY, Text.class);
        MODEL.property(PASSWORDMESSAGESUBJECT);
        
        MODEL.property(RESERVATIONMESSAGESUBJECT);
        MODEL.property(RESERVATIONMESSAGEBODY, Text.class);
        
        MODEL.property(PATROLSHIFTSWAPSUBJECT);
        MODEL.property(PATROLSHIFTSWAPSUCCESS, Text.class);
        MODEL.property(PATROLSHIFTSWAPQUEUED, Text.class);
        MODEL.property(PATROLSHIFTSWAPREMOVED, Text.class);
        MODEL.property(PATROLSHIFTSWAPEXPIREDSUBJECT);
        MODEL.property(PATROLSHIFTSWAPEXPIRED, Text.class);
        
        MODEL.property(RACEENTRYCONFIRMATION, Text.class);
        MODEL.property(RACEENTRYPAYING, Text.class);
        MODEL.property(RACEENTRYSUBJECT);
        MODEL.property(RACEENTRYFROMADDRESS);
        MODEL.property(RACEBANKACCOUNT);
        MODEL.property(RACEBIC);
        
        MODEL.property(LYSINFOURL);
        MODEL.property(LYSCLASSINFOURL);
        MODEL.property(IRCINFOURL);
        MODEL.property(ORCINFOURL);
        MODEL.property(SMSUSERNAME);
        MODEL.property(SMSPASSWORD);
        for (EventType eventType : EventType.values())
        {
            MODEL.property(eventType.name(), Text.class);
        }
        for (int ii=0;ii<10;ii++)
        {
            MODEL.property(TEXT+ii, Text.class);
        }
    }

    public Messages()
    {
        super(new MapData(MODEL));
    }

    

    public Messages(Entity entity)
    {
        super(MODEL, entity);
    }

    @Override
    public Key createKey()
    {
        return KeyFactory.createKey(Keys.getRootKey(), Repository.MESSAGES, NAME);
    }

}

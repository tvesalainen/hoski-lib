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

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import fi.hoski.datastore.repository.DataObject;
import fi.hoski.datastore.repository.Options;
import fi.hoski.datastore.repository.PatrolShift;
import fi.hoski.datastore.repository.SwapRequest;
import fi.hoski.sms.SMSException;
import fi.hoski.util.Day;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.mail.internet.AddressException;

/**
 *
 * @author Timo Vesalainen
 */
public interface PatrolShifts
{
    Options<String> getShiftOptions(String memberKeyString);
    
    List<PatrolShift> getShifts(Key memberKey);

    Day[] firstAndLastShift()  throws EntityNotFoundException;
    
    boolean swapShift(SwapRequest req) throws EntityNotFoundException, IOException, SMSException, AddressException;
    
    boolean swapShift(Map<String, Object> user, String shift, String... excl) throws EntityNotFoundException, IOException, SMSException, AddressException, TooLateException;
    
    void removeSwapShift(Map<String, Object> user, String shift) throws EntityNotFoundException, IOException;
    
    List<SwapRequest> pendingSwapRequests(Map<String, Object> user) throws EntityNotFoundException, IOException;
    
    String getShiftString(Entity patrolShift, String format) throws EntityNotFoundException;
    
    void deleteSwaps(int memberNumber);
    
    List<DataObject> getSwapLog() throws EntityNotFoundException;
    
    void changeShiftExecutor(PatrolShift shift, DataObject member);
    
    void handleExpiredRequests(int margin) throws EntityNotFoundException;
}

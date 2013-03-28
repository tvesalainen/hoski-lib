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

import com.google.appengine.api.datastore.*;
import fi.hoski.datastore.repository.*;
import fi.hoski.mail.MailService;
import fi.hoski.mail.MailServiceImpl;
import fi.hoski.sms.SMSException;
import fi.hoski.sms.SMSService;
import fi.hoski.sms.SMSStatus;
import fi.hoski.sms.zoner.ZonerSMSService;
import fi.hoski.util.Day;
import fi.hoski.util.LogWrapper;
import fi.hoski.util.Time;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * @author Timo Vesalainen
 */
public class PatrolShiftsImpl implements PatrolShifts 
{
    private LogWrapper log;
    private DatastoreService datastore;
    private DSUtils entities;
    private MailService mailService;
    private SMSService smsService;
    private int margin = 5;
    
    public PatrolShiftsImpl(LogWrapper log, int margin) throws EntityNotFoundException
    {
        this.log = log;
        this.margin = margin;
        this.datastore = DatastoreServiceFactory.getDatastoreService();
        this.entities = new DSUtilsImpl(datastore);
        this.mailService = new MailServiceImpl();
        this.smsService = new ZonerSMSService(datastore);
    }

    public PatrolShiftsImpl(LogWrapper log, int margin, DatastoreService datastore, DSUtils entities, MailService mailService, SMSService smsService)
    {
        this.log = log;
        this.datastore = datastore;
        this.entities = entities;
        this.mailService = mailService;
        this.smsService = smsService;
    }

    @Override
    public Options<String> getShiftOptions(String memberKeyString)
    {
        List<PatrolShift> patrolShifts = getShifts(KeyFactory.stringToKey(memberKeyString));
        Options<String> allShifts = new Options<String>();
        if (!patrolShifts.isEmpty())
        {
            allShifts.setSelection((patrolShifts.get(0).createKeyString()));
            for (PatrolShift patrolShift : patrolShifts)
            {
                Day date = Day.getDay(patrolShift.get(Repository.PAIVA));
                allShifts.addItem(date.toString(), patrolShift.createKeyString());
            }
        }
        return allShifts;
    }

    @Override
    public List<PatrolShift> getShifts(Key memberKey)
    {
        List<PatrolShift> list = new ArrayList<PatrolShift>();
        Query query = new Query(Repository.VARTIOVUOROTIEDOT);
        query.addFilter(Repository.JASENNO, Query.FilterOperator.EQUAL, memberKey);
        query.addFilter(Repository.PAIVA, Query.FilterOperator.GREATER_THAN_OR_EQUAL, new Day().getValue());
        log.log(query.toString());
        PreparedQuery preparedQuery = datastore.prepare(query);
        for (Entity entity : preparedQuery.asIterable())
        {
            try
            {
                list.add((PatrolShift)entities.newInstance(entity));
            }
            catch (EntityNotFoundException ex)
            {
                log.log(ex.getMessage(), ex);
            }
        }
        return list;
    }
    private Entity createSwapLog()
    {
        Date currentTime = new Date();
        Key swapLogKey = KeyFactory.createKey(Keys.getYearKey(new Day()), "SwapLog", currentTime.getTime());
        Entity swapLog = new Entity(swapLogKey);
        swapLog.setUnindexedProperty("Timestamp", currentTime);
        return swapLog;
    }
    @Override
    public boolean swapShift(Map<String, Object> user, String shift, String... excl) throws EntityNotFoundException, IOException, SMSException, AddressException
    {
        Key shiftKey = KeyFactory.stringToKey(shift);
        Entity shiftEntity = datastore.get(shiftKey);
        SwapRequest activeSwapRequest = new SwapRequest();
        activeSwapRequest.set(Repository.JASENNO, user.get(Repository.JASENET+Repository.KEYSUFFIX));
        activeSwapRequest.set(Repository.VUOROID, shiftKey);
        activeSwapRequest.set(Repository.PAIVA, Day.getDay(shiftEntity.getProperty(Repository.PAIVA)));
        List<Long> excluded = new ArrayList<Long>();
        excluded.add((Long)shiftEntity.getProperty(Repository.PAIVA));
        if (excl != null)
        {
            for (String ex : excl)
            {
                excluded.add(new Day(ex).getValue());
            }
        }
        activeSwapRequest.set(SwapRequest.EXCLUDE, excluded);
        activeSwapRequest.set(Repository.CREATOR, user.get(Repository.JASENET_EMAIL));
        return swapShift(activeSwapRequest);
    }

    @Override
    public boolean swapShift(SwapRequest activeSwapRequest)  throws EntityNotFoundException, IOException, SMSException, AddressException
    {
        Date currentTime = new Date();
        Entity swapLog = createSwapLog();
        swapLog.setUnindexedProperty(Repository.CREATOR, activeSwapRequest.get(Repository.CREATOR));
        
        Key msgKey = KeyFactory.createKey(Keys.getRootKey(), Messages.KIND, Messages.NAME);
        Entity msgEntity = datastore.get(msgKey);
        
        Day activeSwapRequestDay = (Day) activeSwapRequest.get(Repository.PAIVA);
        
        @SuppressWarnings("unchecked")
        Collection<Long> activeSwapRequestExcluded = (Collection<Long>) activeSwapRequest.get(SwapRequest.EXCLUDE);

        Day now = new Day();
        now.addDays(margin+1);
        Query query = new Query(SwapRequest.KIND);
        query.addFilter(Repository.PAIVA, Query.FilterOperator.GREATER_THAN, now.getValue());
        query.addSort(Repository.PAIVA);
        PreparedQuery prepared = datastore.prepare(query);
        for (Entity swapCandidateEntity : prepared.asIterable())
        {
            SwapRequest swapRequestCandidate = new SwapRequest(swapCandidateEntity);
            Day swapRequestCandidateDay = (Day) swapRequestCandidate.get(Repository.PAIVA);
            
            @SuppressWarnings("unchecked")
            Collection<Long> swapRequestCandidateExcluded = (Collection<Long>) swapRequestCandidate.get(SwapRequest.EXCLUDE);
            if (
                    !swapRequestCandidateExcluded.contains(activeSwapRequestDay.getValue()) &&
                    !activeSwapRequestExcluded.contains(swapRequestCandidateDay.getValue())
                    )
            {
                Key activeShiftKey = (Key) activeSwapRequest.get(Repository.VUOROID);
                Entity activeEntity = datastore.get(activeShiftKey);
                Key activeMemberKey = (Key) activeEntity.getProperty(Repository.JASENNO);
                Key candidateShiftKey = (Key) swapRequestCandidate.get(Repository.VUOROID);
                Entity candidateShiftEntity = datastore.get(candidateShiftKey);
                Key candidateMemberKey = (Key) candidateShiftEntity.getProperty(Repository.JASENNO);
                if (
                        memberDoubleShift(swapRequestCandidateDay.getValue(), activeMemberKey) ||
                        memberDoubleShift(activeSwapRequestDay.getValue(), candidateMemberKey)
                        )
                {
                    log.log("rejected because otherwise would cause double shift");
                }
                else
                {
                    activeEntity.setProperty(Repository.JASENNO, candidateMemberKey);
                    activeEntity.setUnindexedProperty(Repository.TIMESTAMP, currentTime);
                    activeEntity.setUnindexedProperty(Repository.CREATOR, "web");
                    candidateShiftEntity.setProperty(Repository.JASENNO, activeMemberKey);
                    candidateShiftEntity.setUnindexedProperty(Repository.TIMESTAMP, currentTime);
                    candidateShiftEntity.setUnindexedProperty(Repository.CREATOR, "web");

                    Transaction tr = datastore.beginTransaction();
                    try
                    {
                        datastore.put(activeEntity);
                        datastore.put(candidateShiftEntity);
                        datastore.delete(swapCandidateEntity.getKey());
                        entities.delete(activeSwapRequest);   // there might be a pending request
                        swapLog.setUnindexedProperty("Status", "Success");
                        swapLog.setUnindexedProperty("ActiveRequestorShift", activeShiftKey);
                        swapLog.setUnindexedProperty("ActiveRequestor", activeMemberKey);
                        swapLog.setUnindexedProperty("QueuedRequestorShift", candidateShiftKey);
                        swapLog.setUnindexedProperty("QueuedRequestor", candidateMemberKey);
                        datastore.put(swapLog);
                        tr.commit();
                        try
                        {
                            String sender = (String) msgEntity.getProperty(Messages.PASSWORDFROMADDRESS);
                            String messageSubject = (String) msgEntity.getProperty(Messages.PATROLSHIFTSWAPSUBJECT);
                            Text messageText = (Text) msgEntity.getProperty(Messages.PATROLSHIFTSWAPSUCCESS);
                            sendSuccessMessage(sender, activeEntity, messageSubject, messageText.getValue());
                            sendSuccessMessage(sender, candidateShiftEntity, messageSubject, messageText.getValue());
                        }
                        catch (Exception ex)
                        {
                            log.log(ex.getMessage(), ex);
                        }
                    }
                    finally
                    {
                        if (tr.isActive())
                        {
                            tr.rollback();
                        }
                    }
                    return true;
                }
            }
        }
        // queue
        Entity reqEntity = entities.put(activeSwapRequest);
        swapLog.setUnindexedProperty("Status", "Queued");
        swapLog.setPropertiesFrom(reqEntity);
        datastore.put(swapLog);
        return false;
    }
    private String sendSuccessMessage(String sender, Entity newPatrolShift, String subject, String format) throws EntityNotFoundException, IOException, SMSException, AddressException
    {
        String message = getShiftString(newPatrolShift, format);
        log.log(message);
        
        Key memberKey = (Key) newPatrolShift.getProperty(Repository.JASENNO);
        Entity memberEntity = datastore.get(memberKey);
        String email = (String) memberEntity.getProperty(Repository.EMAIL);
        InternetAddress senderAddress = new InternetAddress(sender);
        InternetAddress internetAddress = new InternetAddress(email);
        mailService.sendMail(senderAddress, subject, message, null, internetAddress);
        log.log("send to="+internetAddress);
        String phone = (String) memberEntity.getProperty(Repository.MOBILE);
        SMSStatus sendStatus = smsService.send(sender, phone, message);
        log.log("send to="+phone);
        log.log("sms status="+sendStatus);
        return message;
    }
    public String getShiftString(Entity patrolShift, String format) throws EntityNotFoundException
    {
        Long ld = (Long) patrolShift.getProperty(Repository.PAIVA);
        Day date = new Day(ld);
        String dateStr = date.toString();
        Key shiftKey = (Key) patrolShift.getProperty(Repository.VUORONO);
        long shiftNumber = shiftKey.getId();
        Entity shiftEntity = datastore.get(shiftKey);
        Time shiftStarts = Time.getTime(shiftEntity.getProperty(Repository.ALKAA));
        Time shiftEnds = Time.getTime(shiftEntity.getProperty(Repository.LOPPUU));
        String shiftStr = shiftStarts+"-"+shiftEnds;
        return String.format(format, dateStr, shiftNumber, shiftStr);
    }

    @Override
    public void removeSwapShift(Map<String, Object> user, String shift) throws EntityNotFoundException, IOException
    {
        log.log("removeSwapShift");
        Entity swapLog = createSwapLog();
        swapLog.setUnindexedProperty(Repository.CREATOR, user.get(Repository.JASENET_EMAIL));
        
        Key shiftKey = KeyFactory.stringToKey(shift);
        Entity shiftEntity = datastore.get(shiftKey);
        SwapRequest req = new SwapRequest();
        Key memberKey = KeyFactory.stringToKey((String)user.get(Repository.JASENET_KEY));
        req.set(Repository.JASENNO, memberKey);
        req.set(Repository.VUOROID, shiftKey);
        req.set(Repository.PAIVA, Day.getDay(shiftEntity.getProperty(Repository.PAIVA)));
        
        swapLog.setUnindexedProperty(Repository.JASENNO, memberKey);
        swapLog.setUnindexedProperty(Repository.VUOROID, shiftKey);
        swapLog.setUnindexedProperty(Repository.PAIVA, shiftEntity.getProperty(Repository.PAIVA));
        swapLog.setUnindexedProperty("Status", "Removed");
        
        Transaction tr = datastore.beginTransaction();
        try
        {
            datastore.put(swapLog);
            entities.delete(req);
            tr.commit();
        }
        finally
        {
            if (tr.isActive())
            {
                tr.rollback();
            }
        }
    }
    
    @Override
    public void deleteSwaps(int memberNumber)
    {
        Key memberKey = KeyFactory.createKey(entities.getRootKey(), Repository.JASENET, memberNumber);
        Query query = new Query(SwapRequest.KIND);
        query.setKeysOnly();
        query.addFilter(Repository.JASENNO, Query.FilterOperator.EQUAL, memberKey);
        PreparedQuery prepared = datastore.prepare(query);
        List<Key> keyList = new ArrayList<Key>();
        Transaction tr = datastore.beginTransaction();
        try
        {
            for (Entity entity : prepared.asIterable())
            {
                Entity swapLog = createSwapLog();
                swapLog.setUnindexedProperty("Status", "deleteSwaps("+memberNumber+")");
                swapLog.setPropertiesFrom(entity);
                datastore.put(swapLog);

                keyList.add(entity.getKey());
            }
            datastore.delete(keyList);
            tr.commit();
        }
        finally
        {
            if (tr.isActive())
            {
                tr.rollback();
            }
        }
    }

    @Override
    public List<SwapRequest> pendingSwapRequests(Map<String, Object> user) throws EntityNotFoundException, IOException
    {
        String keyString = (String) user.get(Repository.JASENET_KEY);
        Key memberKey = KeyFactory.stringToKey(keyString);
        Query query = new Query(SwapRequest.KIND);
        query.addFilter(Repository.JASENNO, Query.FilterOperator.EQUAL, memberKey);
        PreparedQuery prepared = datastore.prepare(query);
        List<SwapRequest> swapList = new ArrayList<SwapRequest>();
        for (Entity entity : prepared.asIterable())
        {
            swapList.add(new SwapRequest(entity));
        }
        return swapList;
    }

    @Override
    public Day[] firstAndLastShift() throws EntityNotFoundException
    {
        Key key = KeyFactory.createKey(entities.getRootKey(), Repository.VARTIOVUOROT, 1);
        Entity entity = datastore.get(key);
        return new Day[] {
            Day.getDay(entity.getProperty(Repository.PALKAA)),
            Day.getDay(entity.getProperty(Repository.PLOPPUU))
        };
    }

    @Override
    public List<DataObject> getSwapLog() throws EntityNotFoundException
    {
        List<DataObject> list = new ArrayList<DataObject>();
        Query query = new Query("SwapLog");
        query.addSort(Entity.KEY_RESERVED_PROPERTY);
        PreparedQuery prepared = datastore.prepare(query);
        for (Entity entity : prepared.asIterable())
        {
            list.add(new JoinDataObject(datastore, entities, entity));
        }
        return list;
    }

    private boolean memberDoubleShift(long day, Key activeMemberKey)
    {
        Query query = new Query(Repository.VARTIOVUOROTIEDOT);
        query.addFilter(Repository.PAIVA, Query.FilterOperator.EQUAL, day);
        query.addFilter(Repository.JASENNO, Query.FilterOperator.EQUAL, activeMemberKey);
        PreparedQuery prepared = datastore.prepare(query);
        return prepared.countEntities(FetchOptions.Builder.withDefaults()) != 0;
    }

    @Override
    public void changeShiftExecutor(PatrolShift shift, DataObject member)
    {
        Entity swapLog = createSwapLog();
        swapLog.setUnindexedProperty(Repository.CREATOR, "Admin");
        
        swapLog.setUnindexedProperty("Status", "Change");
        swapLog.setUnindexedProperty("Shift", shift.createKey());
        swapLog.setUnindexedProperty("NewExecutor", member.createKey());
        
        shift.set(Repository.JASENNO, member.createKey());
        Transaction tr = datastore.beginTransaction();
        try
        {
            // find swap requests
            Key shiftKey = shift.createKey();
            Query query = new Query(SwapRequest.KIND);
            PreparedQuery prepared = datastore.prepare(query);
            for (Entity entity : prepared.asIterable())
            {
                try
                {
                    SwapRequest req = (SwapRequest) entities.newInstance(entity);
                    Key reqShiftKey = (Key) req.get(Repository.VUOROID);
                    if (shiftKey.equals(reqShiftKey))
                    {
                        entities.delete(req);
                    }
                }
                catch (EntityNotFoundException ex)
                {
                    log.log("", ex);
                }
            }
            entities.put(shift);
            datastore.put(swapLog);
            tr.commit();
        }
        finally
        {
            if (tr.isActive())
            {
                tr.rollback();
            }
        }
    }

    @Override
    public void handleExpiredRequests(int margin) throws EntityNotFoundException
    {
        Key msgKey = KeyFactory.createKey(Keys.getRootKey(), Messages.KIND, Messages.NAME);
        Entity msgEntity = datastore.get(msgKey);
        String sender = (String) msgEntity.getProperty(Messages.PASSWORDFROMADDRESS);
        String messageSubject = (String) msgEntity.getProperty(Messages.PATROLSHIFTSWAPEXPIREDSUBJECT);
        Text messageText = (Text) msgEntity.getProperty(Messages.PATROLSHIFTSWAPEXPIRED);
        
        Day now = new Day();
        now.addDays(margin);
        Query query = new Query(SwapRequest.KIND);
        query.addFilter(Repository.PAIVA, Query.FilterOperator.LESS_THAN, now.getValue());
        log.log(query.toString());
        PreparedQuery prepared = datastore.prepare(query);
        for (Entity entity : prepared.asIterable())
        {
            Entity swapLog = createSwapLog();
            swapLog.setPropertiesFrom(entity);
            swapLog.setUnindexedProperty(Repository.CREATOR, "Cron");
            swapLog.setUnindexedProperty("Status", "Remove expired");
            
            SwapRequest swapRequest = new SwapRequest(entity);
            Transaction tr = datastore.beginTransaction();
            try
            {
                Key shiftKey = (Key) swapRequest.get(Repository.VUOROID);
                Entity shiftEntity = datastore.get(shiftKey);
                sendExpiredMessage(sender, shiftEntity, messageSubject, messageText.getValue());
                datastore.delete(entity.getKey());
                datastore.put(swapLog);
                tr.commit();
            }
            catch (IOException ex)
            {
                log.log(ex.getMessage(), ex);
            }
            catch (SMSException ex)
            {
                log.log(ex.getMessage(), ex);
            }
            catch (AddressException ex)
            {
                log.log(ex.getMessage(), ex);
            }            
            finally
            {
                if (tr.isActive())
                {
                    tr.rollback();
                }
            }
        }
    }
    private String sendExpiredMessage(String sender, Entity patrolShift, String subject, String format) throws EntityNotFoundException, IOException, SMSException, AddressException
    {
        String message = getShiftString(patrolShift, format);
        log.log(message);
        
        Key memberKey = (Key) patrolShift.getProperty(Repository.JASENNO);
        Entity memberEntity = datastore.get(memberKey);
        String email = (String) memberEntity.getProperty(Repository.EMAIL);
        InternetAddress senderAddress = new InternetAddress(sender);
        InternetAddress internetAddress = new InternetAddress(email);
        mailService.sendMail(senderAddress, subject, message, null, internetAddress);
        log.log("send to="+internetAddress);
        /*
        String phone = (String) memberEntity.getProperty(Repository.MOBILE);
        SMSStatus sendStatus = smsService.send(sender, phone, message);
        log.log("send to="+phone);
        log.log("sms status="+sendStatus);
        */
        return message;
    }

}

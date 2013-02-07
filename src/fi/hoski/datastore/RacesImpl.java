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
import fi.hoski.datastore.repository.Attachment;
import fi.hoski.mail.MailService;
import fi.hoski.mail.MailServiceImpl;
import fi.hoski.util.BankingBarcode;
import fi.hoski.util.Day;
import fi.hoski.util.LogWrapper;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Timo Vesalainen
 */
public class RacesImpl implements Races
{
    private LogWrapper log;
    private DatastoreService datastore;
    private DSUtils entities;
    private MailService mailService;
    private References references;

    public RacesImpl(LogWrapper log) throws EntityNotFoundException
    {
        this.log = log;
        this.datastore = DatastoreServiceFactory.getDatastoreService();
        this.entities = new DSUtilsImpl(datastore);
        this.mailService = new MailServiceImpl();
        this.references = new ReferencesImpl();
    }

    public RacesImpl(LogWrapper log, DatastoreService datastore, DSUtils entities, MailService mailService)
    {
        this.log = log;
        this.datastore = datastore;
        this.entities = entities;
        this.mailService = mailService;
        this.references = new ReferencesImpl(datastore);
    }

    @Override
    public RaceSeries getExistingRace(RaceSeries raceSeries)
    {
        Key key = raceSeries.createKey();
        try
        {
            Entity entity = datastore.get(key);
            return (RaceSeries) entities.newInstance(entity);
        }
        catch (EntityNotFoundException ex)
        {
            return null;
        }
    }

    @Override
    public void putRace(RaceSeries raceSeries, List<RaceFleet> classList)
    {
        entities.put(raceSeries);
        entities.put(classList);
    }

    @Override
    public void removeRace(RaceSeries raceSeries)
    {
        entities.deleteWithChilds(raceSeries);
    }
    @Override
    public List<RaceSeries> getRaces() throws EntityNotFoundException
    {
        List<RaceSeries> list = new ArrayList<RaceSeries>();
        Query query = new Query(RaceSeries.KIND);
        query.setAncestor(entities.getYearKey());
        query.addSort(RaceSeries.EVENTDATE);
        PreparedQuery prepared = datastore.prepare(query);
        for (Entity entity : prepared.asIterable())
        {
            list.add((RaceSeries)entities.newInstance(entity));
        }
        return list;
    }

    @Override
    public List<RaceFleet> getFleets(RaceSeries raceSeries) throws EntityNotFoundException
    {
        List<RaceFleet> list = new ArrayList<RaceFleet>();
        Query query = new Query(RaceFleet.KIND);
        query.setAncestor(raceSeries.createKey());
        PreparedQuery prepared = datastore.prepare(query);
        for (Entity entity : prepared.asIterable())
        {
            list.add((RaceFleet)entities.newInstance(entity));
        }
        return list;
    }

    @Override
    public List<RaceEntry> getRaceEntriesFor(DataObject race) throws EntityNotFoundException
    {
        List<RaceEntry> list = new ArrayList<RaceEntry>();
        Key parent = race.createKey();
        Query query = new Query(RaceEntry.KIND);
        query.setAncestor(parent);
        PreparedQuery prepared = datastore.prepare(query);
        for (Entity entity : prepared.asIterable())
        {
            list.add((RaceEntry)entities.newInstance(entity));
        }
        return list;
    }

    @Override
    public List<RaceEntry> getUnpaidRaceEntries() throws EntityNotFoundException
    {
        List<RaceEntry> list = new ArrayList<RaceEntry>();
        Query query = new Query(RaceEntry.KIND);
        query.addFilter(RaceEntry.PAID, Query.FilterOperator.EQUAL, 0.0);
        //query.addFilter(RaceEntry.FEE, Query.FilterOperator.GREATER_THAN, 0.0);
        PreparedQuery prepared = datastore.prepare(query);
        for (Entity entity : prepared.asIterable())
        {
            Double fee = (Double) entity.getProperty(RaceEntry.FEE);
            if (fee != null && fee > 0.0)
            {
                list.add((RaceEntry)entities.newInstance(entity));
            }
        }
        return list;
    }

    @Override
    public BankingBarcode getBarcode(Key raceEntryKey) throws EntityNotFoundException
    {
        Entity entity = datastore.get(raceEntryKey);
        return getBarcode((RaceEntry) entities.newInstance(entity));
    }

    @Override
    public BankingBarcode getBarcode(RaceEntry raceEntry) throws EntityNotFoundException
    {
        RaceFleet raceFleet = (RaceFleet) raceEntry.getParent();
        Double fee = (Double) raceEntry.get(RaceEntry.FEE);
        if (fee != null)
        {
            Transaction tr = datastore.beginTransaction();
            try
            {
                RaceSeries raceSeries = (RaceSeries) raceFleet.getParent();
                Key raceSeriesKey = raceSeries.createKey();
                long seriesRef = references.getReferenceFor(raceSeriesKey);
                if (seriesRef == -1)
                {
                    seriesRef = references.getNextReferenceFor(raceSeriesKey.getParent());
                    log.log("seriesRef="+seriesRef+" "+raceSeriesKey);
                    references.addReferenceFor(raceSeriesKey, seriesRef);
                }
                if (seriesRef > 99)
                {
                    log.log("Warning: "+raceSeries.createKey()+" reference over 99");
                    return null;
                }
                log.log(raceEntry.createKey().toString());
                Key raceEntryKey = raceEntry.createKey();
                long reference = references.getReferenceFor(raceEntryKey);
                if (reference == -1)
                {
                    long entryRef = references.getNextReferenceFor(raceSeriesKey);
                    reference = seriesRef+100*entryRef;
                    references.addReferenceFor(raceEntryKey, reference);
                }
                tr.commit();
                
                Day date = (Day) raceFleet.get(RaceFleet.EVENTDATE);
                Day due = (Day) raceFleet.get(RaceFleet.CLOSINGDATE);
                String bankAccount = entities.getMessage(Messages.RACEBANKACCOUNT);
                String bic = entities.getMessage(Messages.RACEBIC);
                return new BankingBarcode(
                        '5',
                        bankAccount,
                        fee,
                        String.valueOf(reference),
                        due.getDate(),
                        bic
                        );
            }
            finally
            {
                if (tr.isActive())
                {
                    tr.rollback();
                }
            }

        }
        else
        {
            return null;
        }
    }

    @Override
    public RaceEntry raceEntryForReference(long reference) throws EntityNotFoundException
    {
        Entity entity = references.getEntityFor(reference);
        return (RaceEntry) entities.newInstance(entity);
    }

}

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
import fi.hoski.datastore.repository.*;
import fi.hoski.util.BankingBarcode;
import java.util.List;

/**
 *
 * @author Timo Vesalainen
 */
public interface Races
{
    RaceSeries getExistingRace(RaceSeries raceSeries);
    
    void putRace(RaceSeries raceSeries, List<RaceFleet> classList);
    
    List<RaceSeries> getRaces() throws EntityNotFoundException;
    
    List<RaceFleet> getFleets(RaceSeries raceSeries) throws EntityNotFoundException;
    
    List<RaceEntry> getRaceEntriesFor(DataObject race) throws EntityNotFoundException;
    
    List<RaceEntry> getUnpaidRaceEntries() throws EntityNotFoundException;
    
    BankingBarcode getBarcode(Key raceEntryKey) throws EntityNotFoundException;
    
    BankingBarcode getBarcode(RaceEntry raceEntry) throws EntityNotFoundException;
    
    RaceEntry raceEntryForReference(long reference) throws EntityNotFoundException;
}

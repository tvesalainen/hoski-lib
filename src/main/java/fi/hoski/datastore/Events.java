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

import com.google.appengine.api.datastore.EntityNotFoundException;
import fi.hoski.datastore.repository.*;
import fi.hoski.datastore.repository.Event.EventType;
import fi.hoski.util.Day;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Timo Vesalainen
 */
public interface Events
{
    /**
     * @deprecated 
     * @param reservations 
     */
    void updateInspection(List<Reservation> reservations);

    void deleteEvents(List<Event> events);

    int childCount(Event event);

    List<Event> getEvents(EventType type);
    /**
     * Return events. If from or count are null they are ignored
     * @param type
     * @param from
     * @param count
     * @return 
     */
    List<Event> getEvents(EventType type, Day from, Integer count);
    
    Event getEvent(EventType type, long id) throws EntityNotFoundException;

    public String getEventLabel(Event event);
            
    boolean hasChilds(Event event);
    /**
     * Returns a Options instance having all created events for given type
     * for current year
     * @param type
     * @return 
     */
    Options<Event> getEventSelection(EventType type);
    
    void createReservation(Reservation reservation, boolean replace) throws EntityNotFoundException, EventFullException, DoubleBookingException, BoatNotFoundException, MandatoryPropertyMissingException;
    void createReservation(Event event, Reservation reservation, boolean replace) throws EntityNotFoundException, EventFullException, DoubleBookingException, BoatNotFoundException, MandatoryPropertyMissingException;

    /**
     * Returns all reservations for given event
     * @param event
     * @return 
     */
    List<Reservation> getReservations(Event event);

    Event getEvent(String eventKey) throws EntityNotFoundException;
    
    void createMissingEventTypes();

}

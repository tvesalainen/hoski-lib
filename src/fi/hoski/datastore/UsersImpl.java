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
import fi.hoski.datastore.repository.Boat;
import fi.hoski.datastore.repository.Options;
import fi.hoski.util.Day;
import java.util.*;

/**
 * @author Timo Vesalainen
 */
public class UsersImpl extends DSUtilsImpl implements Repository, Users
{
    private DSUtils entities;
    
    public UsersImpl(DatastoreService datastore)
    {
        super(datastore);
        entities = new DSUtilsImpl(datastore);
    }

    @Override
    public Entity retrieveCredentials(String email) throws EmailNotUniqueException
    {
        Entity user = retrieveUser(email);
        if (user == null)
        {
            return null;
        }
        Key credKey = KeyFactory.createKey(CREDENTIALS, user.getKey().getId());
        try
        {
            return datastore.get(credKey);
        }
        catch (EntityNotFoundException e)
        {
            return null;
        }
    }

    @Override
    public Entity retrieveUser(String email) throws EmailNotUniqueException
    {
        Query query = new Query(JASENET);
        query.addFilter("Email", Query.FilterOperator.EQUAL, email);
        query.addSort(Entity.KEY_RESERVED_PROPERTY);
        PreparedQuery preparedQuery = datastore.prepare(query);
        Iterator<Entity> iterator = preparedQuery.asIterator();
        if (iterator.hasNext())
        {
            Entity entity = iterator.next();
            if (iterator.hasNext())
            {
                throw new EmailNotUniqueException(email);
            }
            return entity;
        }
        else
        {
            return null;
        }
    }

    private Entity entityFor(Key key, Collection<Entity> entities)
    {
        for (Entity entity : entities)
        {
            if (key.equals(entity.getKey()))
            {
                return entity;
            }
        }
        throw new IllegalArgumentException("entity for "+key+" not found");
    }

    @Override
    public Map<String, Object> getUserData(Entity user)
    {
        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        Map<String, Entity> entityMap = new HashMap<String, Entity>();
        entityMap.put(user.getKind(), user);
        addSideReferences(entityMap, user);
        
        Map<String, Object> ret = flat(entityMap);
        List<Map<String, Object>> boatList = new ArrayList<Map<String, Object>>();
        ret.put(Repository.BOATS, boatList);
        // Veneet
        Query q1 = new Query(Repository.VENEET);
        /*  listing now all boats
        List<Key> venetyyppit = new ArrayList<Key>();
        Key root = entities.getRootKey();
        venetyyppit.add(KeyFactory.createKey(root, Repository.VENETYYPPIT, "PV"));
        venetyyppit.add(KeyFactory.createKey(root, Repository.VENETYYPPIT, "MV"));
        q1.addFilter(Repository.TYYPPI, Query.FilterOperator.IN, venetyyppit);
        */
        q1.addFilter(Repository.OMISTAJA, Query.FilterOperator.EQUAL, user.getKey());
        q1.addSort(Repository.PAINO, Query.SortDirection.DESCENDING);
        PreparedQuery p1 = datastore.prepare(q1);
        List<Entity> boats = p1.asList(FetchOptions.Builder.withDefaults());
        for (Entity boat : boats)
        {
            Long inspectionYear = null;
            Long basicInspectionYear = null;
            Map<String, Entity> boatEntityMap = new HashMap<String, Entity>();
            boatEntityMap.put(boat.getKind(), boat);
            boatEntityMap.put(Repository.JASENET, user);    // to prevent fetching Jasenet again
            addSideReferences(boatEntityMap, boat);
            boatEntityMap.remove(Repository.JASENET);
            // Laituripaikkatiedot
            Query q2 = new Query(Repository.LAITURIPAIKKATIEDOT);
            q2.addFilter(Repository.VENEID, Query.FilterOperator.EQUAL, boat.getKey());
            PreparedQuery p2 = datastore.prepare(q2);
            Entity berth = getFirst(p2); // unlikely that boat will have more than one berth
            if (berth != null)
            {
                boatEntityMap.put(berth.getKind(), berth);
                addSideReferences(boatEntityMap, berth);
            }
            // Katsastustiedot
            Query q21 = new Query(Repository.KATSASTUSTIEDOT);
            q21.addFilter(Repository.VENEID, Query.FilterOperator.EQUAL, boat.getKey());
            q21.addSort(Repository.PAIVA, Query.SortDirection.DESCENDING);
            PreparedQuery p21 = datastore.prepare(q21);
            Long inspectionClass = null;
            for (Entity entity : p21.asIterable())
            {
                if (inspectionClass == null)
                {
                    inspectionClass = (Long) entity.getProperty(Repository.KATSASTUSLUOKKA);
                }
                Day date = Day.getDay(entity.getProperty(Repository.PAIVA));
                if (date != null)
                {
                    long year = date.getYear();
                    Key ktKey = (Key) entity.getProperty(Repository.KATSASTUSTYYPPI);
                    switch ((int)ktKey.getId())
                    {
                        case 1:
                            if (basicInspectionYear == null)
                            {
                                basicInspectionYear = new Long(year);
                            }
                            break;
                        case 2:
                            if (inspectionYear == null)
                            {
                                inspectionYear = new Long(year);
                            }
                            break;
                    }
                }
                if (basicInspectionYear != null && inspectionYear != null)
                {
                    break;
                }
            }
            Map<String, Object> flat = flat(boatEntityMap);
            // add last inspection class
            if (inspectionClass != null)
            {
                flat.put(Repository.KATSASTUSLUOKKA, inspectionClass);
            }
            // add year of inspection
            if (inspectionYear != null)
            {
                flat.put(Repository.INSPECTIONYEAR, inspectionYear);
            }
            // add year of basic inspection
            if (basicInspectionYear != null)
            {
                flat.put(Repository.BASICINSPECTIONYEAR, basicInspectionYear);
                flat.put(Repository.BASICINSPECTION, new Boolean(yearNow - basicInspectionYear >= 5));  // this is for launch
                flat.put(Repository.NEXTBASICINSPECTION, new Boolean(yearNow - basicInspectionYear >= 4));  // this is for lift
            }
            else
            {
                flat.put(Repository.BASICINSPECTION, new Boolean(true));
            }
            boatList.add(flat);
        }
        // Vartiovuorotiedot
        Query q3 = new Query(Repository.VARTIOVUOROTIEDOT);
        q3.addFilter(Repository.JASENNO, Query.FilterOperator.EQUAL, user.getKey());
        q3.addFilter(Repository.PAIVA, Query.FilterOperator.GREATER_THAN_OR_EQUAL, new Day().getValue());
        PreparedQuery p3 = datastore.prepare(q3);
        List<Entity> patrolShifts = p3.asList(FetchOptions.Builder.withDefaults());
        if (!patrolShifts.isEmpty())
        {
            Entity patrolShift = null;
            patrolShift = patrolShifts.get(0);
            entityMap.put(patrolShift.getKind(), patrolShift);
            addSideReferences(entityMap, patrolShift);
        }
        String firstname = (String) ret.get(JASENET_ETUNIMI);
        if (firstname != null)
        {
            String[] fna = firstname.split(" ");
            firstname = fna[0];
            ret.put(Repository.FIRSTNAME, firstname);
        }
        // add other boats
        Options<String> allBoats = new Options<String>();
        if (!boats.isEmpty())
        {
            allBoats.setSelection(KeyFactory.keyToString(boats.get(0).getKey()));
            for (Entity boat : boats)
            {
                allBoats.addItem((String)boat.getProperty(Boat.NIMI), KeyFactory.keyToString(boat.getKey()));
            }
        }
        ret.put(Repository.BOAT_SELECTION, allBoats);
        // add other patrol shifts. (because of several boats for one member)
        Options<String> allShifts = new Options<String>();
        if (!patrolShifts.isEmpty())
        {
            allShifts.setSelection(KeyFactory.keyToString(patrolShifts.get(0).getKey()));
            for (Entity patrolShift : patrolShifts)
            {
                Day date = Day.getDay(patrolShift.getProperty(Repository.PAIVA));
                allShifts.addItem(date.toString(), KeyFactory.keyToString(patrolShift.getKey()));
            }
        }
        ret.put(Repository.PATROL_SHIFT_SELECTION, allShifts);
        return ret;
    }

    private Entity getFirst(PreparedQuery pq)
    {
        Iterator<Entity> it = pq.asIterator();
        if (it.hasNext())
        {
            return it.next();
        }
        else
        {
            return null;
        }

    }

    @Override
    public void addSideReferences(Map<String, Entity> entityMap, Entity entity)
    {
        for (Object prop : entity.getProperties().values())
        {
            if (prop instanceof Key)
            {
                Key key = (Key) prop;
                if (!entityMap.containsKey(key.getKind()))
                {
                    Entity e;
                    try
                    {
                        e = datastore.get(key);
                        entityMap.put(key.getKind(), e);
                        addSideReferences(entityMap, e);
                    }
                    catch (EntityNotFoundException ex)
                    {
                    }
                }
            }
        }
    }

    private Map<String, Object> flat(Map<String, Entity> entityMap)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        for (Map.Entry<String, Entity> entry : entityMap.entrySet())
        {
            String kind = entry.getKey();
            Entity entity = entry.getValue();
            result.put(kind + Repository.KEYSUFFIX, KeyFactory.keyToString(entity.getKey()));
            for (Map.Entry<String, Object> property : entity.getProperties().entrySet())
            {
                result.put(kind + "." + property.getKey(), property.getValue());
            }
        }
        return result;
    }
}

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

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.*;
import fi.hoski.datastore.repository.*;
import fi.hoski.util.Day;
import fi.hoski.util.FormPoster;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Timo Vesalainen
 */
public class DSUtilsImpl implements DSUtils 
{
    public static final int CHUNKSIZE = 500;
    
    public static final String EVENTCLOSINGDATE = "EventClosingDate";
    public static final String EVENTDATE = "EventDate";
    public static final String EVENTMAXENTRIES = "EventMaxEntries";
    public static final String EVENTNOTES = "EventNotes";
    public static final String ID = "_Id";
    public static final String KEY = "_Key";
    public static final String KIND = "_Kind";
    public static final String NAME = "_Name";
    public static final String PARENT = "_Parent";

    private static final String[] RESERVED = new String[] {KIND, PARENT, KEY, NAME, ID};
    private static final String BLOB_KEY = "blob-key=";
    
    protected DatastoreService datastore;

    public DSUtilsImpl()
    {
        this.datastore = DatastoreServiceFactory.getDatastoreService();
    }
    
    public DSUtilsImpl(DatastoreService datastore)
    {
        this.datastore = datastore;
    }

    @Override
    public Entity get(Key key) throws EntityNotFoundException
    {
        return datastore.get(key);
    }

    @Override
    public List<Entity> getChilds(Key parent, String... kinds)
    {
        if (kinds.length == 0)
        {
            return getChildsForKind(parent, null);
        }
        else
        {
            List<Entity> list = new ArrayList<>();
            for (String kind : kinds)
            {
                list.addAll(getChildsForKind(parent, kind));
            }
            return list;
        }
    }

    private List<Entity> getChildsForKind(Key parent, String kind)
    {
        Query query = new Query(kind);
        query.setAncestor(parent);
        query.addFilter(Entity.KEY_RESERVED_PROPERTY, Query.FilterOperator.NOT_EQUAL, parent);
        PreparedQuery preparedQuery = datastore.prepare(query);
        return preparedQuery.asList(FetchOptions.Builder.withDefaults());
    }

    @Override
    public DataObject newInstance(Key key) throws EntityNotFoundException
    {
        Entity entity = datastore.get(key);
        return newInstance(entity);
    }

    public DataObject newInstance(Entity entity) throws EntityNotFoundException
    {
        if (Event.KIND.equals(entity.getKind()))
        {
            return new Event(entity);
        }
        if (Inspection.KIND.equals(entity.getKind()))
        {
            return new Inspection(entity);
        }
        if (Messages.KIND.equals(entity.getKind()))
        {
            return new Messages(entity);
        }
        if (PatrolShift.KIND.equals(entity.getKind()))
        {
            return new PatrolShift(entity);
        }
        if (RaceEntry.KIND.equals(entity.getKind()))
        {
            Entity rfEntity = datastore.get(entity.getParent());
            RaceFleet raceFleet = (RaceFleet) newInstance(rfEntity);
            return new RaceEntry(raceFleet, entity);
        }
        if (RaceFleet.Kind.equals(entity.getKind()))
        {
            Entity rsEntity = datastore.get(entity.getParent());
            RaceSeries raceSeries = (RaceSeries) newInstance(rsEntity);
            return new RaceFleet(raceSeries, entity);
        }
        if (RaceSeries.KIND.equals(entity.getKind()))
        {
            return new RaceSeries(entity);
        }
        if (Reservation.KIND.equals(entity.getKind()))
        {
            Entity eventEntity = datastore.get(entity.getParent());
            Event event = (Event) newInstance(eventEntity);
            return new Reservation(event, entity);
        }
        if (SwapRequest.KIND.equals(entity.getKind()))
        {
            return new SwapRequest(entity);
        }
        if (Attachment.KIND.equals(entity.getKind()))
        {
            DataObject dob = newInstance(entity.getParent());
            Attachment attachment = new Attachment(entity);
            attachment.setParent(dob);
            return attachment;
        }
        if (Title.KIND.equals(entity.getKind()))
        {
            return new Title(entity);
        }
        if (Year.KIND.equals(entity.getKind()))
        {
            return new Year(entity);
        }
        throw new IllegalArgumentException("unknown entity "+entity);
    }

    @Override
    public DataObjectModel getModel(String kind)
    {
        if (Event.KIND.equals(kind))
        {
            return Event.MODEL;
        }
        if (Inspection.KIND.equals(kind))
        {
            return Inspection.INSPECTION_MODEL;
        }
        if (Messages.KIND.equals(kind))
        {
            return Messages.MODEL;
        }
        if (PatrolShift.KIND.equals(kind))
        {
            return PatrolShift.MODEL;
        }
        if (RaceFleet.Kind.equals(kind))
        {
            return RaceFleet.Model;
        }
        if (RaceSeries.KIND.equals(kind))
        {
            return RaceSeries.MODEL;
        }
        if (Reservation.KIND.equals(kind))
        {
            return Reservation.BASE_MODEL;
        }
        if (SwapRequest.KIND.equals(kind))
        {
            return SwapRequest.SWAP_REQUEST_MODEL;
        }
        if (Attachment.KIND.equals(kind))
        {
            return Attachment.MODEL;
        }
        throw new IllegalArgumentException("unknown kind "+kind);
    }

    @Override
    public Key getRootKey()
    {
        Key rootKey = Keys.getRootKey();
        try
        {
            datastore.get(rootKey);
        }
        catch (EntityNotFoundException ex)
        {
            Entity entity = new Entity(rootKey);
            datastore.put(entity);
        }
        return rootKey;
    }

    @Override
    public void addYear(int year)
    {
        Key yearKey = Keys.getYearKey(year);
        try
        {
            datastore.get(yearKey);
        }
        catch (EntityNotFoundException ex)
        {
            Entity entity = new Entity(yearKey);
            datastore.put(entity);
        }
    }

    @Override
    public Key getYearKey()
    {
        Key yearKey = Keys.getYearKey(new Day());
        try
        {
            datastore.get(yearKey);
        }
        catch (EntityNotFoundException ex)
        {
            Entity entity = new Entity(yearKey);
            datastore.put(entity);
        }
        return yearKey;
    }

    @Override
    public Entity createEntity(Map<String,String[]> parameters)
    {
        String kind = getParameter(parameters, KIND);
        if (kind == null)
        {
            throw new IllegalArgumentException("kind missing in parameters");
        }
        Key key = null;
        String keyStr = getParameter(parameters, KEY);
        if (keyStr != null)
        {
            key = KeyFactory.stringToKey(keyStr);
        }
        else
        {
            Key parent = null;
            String parentStr = getParameter(parameters, PARENT);
            if (parentStr != null)
            {
                parent = KeyFactory.stringToKey(parentStr);
            }
            String idStr = getParameter(parameters, ID);
            if (idStr != null)
            {
                long id = Long.parseLong(idStr);
                key = KeyFactory.createKey(parent, kind, id);
            }
            else
            {
                String name = getParameter(parameters, NAME);
                if (name != null)
                {
                    key = KeyFactory.createKey(parent, kind, name);
                }
                else
                {
                    throw new IllegalArgumentException("id and name missing in parameters");
                }
            }
        }
        Entity entity = new Entity(key);
        for (String param : parameters.keySet())
        {
            if (!isReserved(param))
            {
                String[] value = parameters.get(key);
                if (value != null)
                {
                    if (value.length == 1)
                    {
                        entity.setUnindexedProperty(param, value[0]);
                    }
                    else
                    {
                        List<String> asList = Arrays.asList(value);
                        entity.setUnindexedProperty(param, asList);
                    }
                }
            }
        }
        datastore.put(entity);
        return entity;
    }
    
    @Override
    public Map<String,String[]> getMap(Entity entity)
    {
        Map<String,String[]> map = new HashMap<String,String[]>();
        Key key = entity.getKey();
        map.put(KEY, new String[]{KeyFactory.keyToString(key)});
        for (String property : entity.getProperties().keySet())
        {
            Object value = entity.getProperty(property);
            if (value instanceof Collection<?>)
            {
                Collection<?> c = (Collection<?>) value;
                String[] sa = new String[c.size()];
                int index = 0;
                for (Object o : c)
                {
                    sa[index++] = o.toString();
                }
                map.put(key.getKind()+"."+property, sa);
            }
            else
            {
                map.put(key.getKind()+"."+property, new String[]{value.toString()});
            }
        }
        return map;
    }
    private static boolean isReserved(String str)
    {
        for (String s : RESERVED)
        {
            if (s.equals(str))
            {
                return true;
            }
        }
        return false;
    }
    private static String getParameter(Map<String,String[]> parameters, String parameter)
    {
        String[] ss = parameters.get(parameter);
        if (ss == null)
        {
            return null;
        }
        else
        {
            if (ss.length != 1)
            {
                throw new IllegalArgumentException(parameter+" has several values");
            }
            else
            {
                return ss[0];
            }
        }
    }

    @Override
    public List<AnyDataObject> retrieve(String kind, String queryProperty, Object queryValue, String... properties)
    {
        Query query = new Query(kind);
        query.addFilter(queryProperty, Query.FilterOperator.EQUAL, queryValue);
        PreparedQuery prepared = datastore.prepare(query);
        return AnyDataObject.create(prepared.asList(FetchOptions.Builder.withDefaults()), properties);
    }

    @Override
    public void put(List<? extends DataObject> dataObjectList)
    {
        List<Entity> list = new ArrayList<>();
        for (DataObject dataObject : dataObjectList)
        {
            list.add(dataObject.getEntity());
        }
        datastore.put(list);
    }

    @Override
    public Entity put(DataObject dataObject)
    {
        Entity entity = dataObject.getEntity();
        Key key = datastore.put(entity);
        Entity ne = new Entity(key);
        ne.setPropertiesFrom(entity);
        dataObject.setEntity(ne);
        return ne;
    }

    @Override
    public void delete(DataObject dataObject)
    {
        datastore.delete(dataObject.getEntity().getKey());
    }

    @Override
    public void deleteWithChilds(DataObject dataObject, String... kinds)
    {
        Key key = dataObject.getEntity().getKey();
        Transaction tr = datastore.beginTransaction();
        try
        {
            datastore.delete(key);
            List<Entity> childs = getChilds(key, kinds);
            for (Entity e : childs)
            {
                datastore.delete(e.getKey());
            }
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
    public void delete(List<? extends DataObject> dataObjectList)
    {
        List<Key> list = new ArrayList<Key>();
        for (DataObject dataObject : dataObjectList)
        {
            list.add(dataObject.getEntity().getKey());
        }
        datastore.delete(list);
    }

    @Override
    public List<Entity> convert(List<? extends DataObject> dataObjectList)
    {
        List<Entity> list = new ArrayList<Entity>();
        for (DataObject dataObject : dataObjectList)
        {
            list.add(dataObject.getEntity());
        }
        return list;
    }

    @Override
    public String getMessage(String key)
    {
        Messages messages = getMessages();
        return messages.getString(key);
    }

    @Override
    public Messages getMessages()
    {
        Key key = KeyFactory.createKey(Keys.getRootKey(), Messages.KIND, Messages.NAME);
        Entity entity;
        try
        {
            entity = datastore.get(key);
            return new Messages(entity);
        }
        catch (EntityNotFoundException ex)
        {
            return new Messages();
        }
    }

    @Override
    public void putMessages(Messages messages)
    {
        put(messages);
    }

    @Override
    public List<String> kindList()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Iterator<Entity> load(List<String> kindList)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void store(Iterator<Entity> entities)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int remove(List<String> kindList)
    {
        int count = 0;
        for (String kind : kindList)
        {
            Query query = new Query(kind);
            count += remove(query);
        }
        return count;
    }

    @Override
    public int remove(long year)
    {
        Key yearKey = Keys.getYearKey(year);
        Query query = new Query();
        query.setAncestor(yearKey);
        return remove(query);
    }

    private int remove(Query query)
    {
        int count = 0;
        List<Key> removeList = new ArrayList<Key>();
        query.setKeysOnly();
        PreparedQuery preparedQuery = datastore.prepare(query);
        for (Entity entity : preparedQuery.asIterable(FetchOptions.Builder.withChunkSize(CHUNKSIZE)))
        {
            removeList.add(entity.getKey());
            if (removeList.size() >= CHUNKSIZE)
            {
                datastore.delete(removeList);
                count += removeList.size();
                removeList.clear();
            }
        }
        datastore.delete(removeList);
        count += removeList.size();
        return count;
    }
    
    @Override
    public int backup(long year, ObjectOutputStream out) throws IOException
    {
        Key yearKey = Keys.getYearKey(year);
        Query query = new Query();
        query.setAncestor(yearKey);
        return backup(query, out);
    }

    @Override
    public int backup(List<String> kindList, ObjectOutputStream out) throws IOException
    {
        int count = 0;
        for (String kind : kindList)
        {
            Query query = new Query(kind);
            count += backup(query, out);
        }
        return count;
    }

    private int backup(Query query, ObjectOutputStream out) throws IOException
    {
        int count = 0;
        PreparedQuery preparedQuery = datastore.prepare(query);
        for (Entity entity : preparedQuery.asIterable(FetchOptions.Builder.withChunkSize(CHUNKSIZE)))
        {
            out.writeObject(entity);
            count++;
        }
        return count;
    }
    
    @Override
    public int restore(ObjectInputStream in) throws IOException
    {
        try
        {
            int count = 0;
            List<Entity> list = new ArrayList<Entity>();
            try
            {
                Entity entity = (Entity) in.readObject();
                while (entity != null)
                {
                    list.add(entity);
                    if (list.size() >= CHUNKSIZE)
                    {
                        datastore.put(list);
                        count += list.size();
                        list.clear();
                    }
                    entity = (Entity) in.readObject();
                }
            }
            catch (EOFException ex)
            {
            }
            datastore.put(list);
            count += list.size();
            return count;
        }
        catch (ClassNotFoundException ex)
        {
            throw new IOException(ex);
        }
    }

    @Override
    public int restore(Collection<String> kinds, ObjectInputStream in) throws IOException
    {
        try
        {
            int count = 0;
            List<Entity> list = new ArrayList<Entity>();
            try
            {
                Entity entity = (Entity) in.readObject();
                while (entity != null)
                {
                    if (kinds.contains(entity.getKind()))
                    {
                        list.add(entity);
                        if (list.size() >= CHUNKSIZE)
                        {
                            datastore.put(list);
                            count += list.size();
                            list.clear();
                        }
                    }
                    entity = (Entity) in.readObject();
                }
            }
            catch (EOFException ex)
            {
            }
            datastore.put(list);
            count += list.size();
            return count;
        }
        catch (ClassNotFoundException ex)
        {
            throw new IOException(ex);
        }
    }

    @Override
    public void upload(DataObject attachTo, Attachment.Type type, String title, File... files) throws IOException
    {
        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        FormPoster poster = new FormPoster(new URL(blobstoreService.createUploadUrl("/upload")));
        poster.setFormData("type", type.name());
        poster.setFormData("attachTo", KeyFactory.keyToString(attachTo.createKey()));
        poster.setFormData("title", title);
        poster.addFiles(files);
        poster.post();
    }

    @Override
    public void removeAttachments(List<Attachment> attachments) throws IOException
    {
        for (Attachment attachment : attachments)
        {
            Link link = (Link) attachment.get(Attachment.URL);
            String url = link.getValue();
            int idx = url.indexOf(BLOB_KEY);
            if (idx != -1)
            {
                String blobKeyString = url.substring(idx+BLOB_KEY.length());
                BlobKey blobKey = new BlobKey(blobKeyString);
                BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
                blobstoreService.delete(blobKey);
            }
        }
        delete(attachments);
    }

    @Override
    public List<Attachment> getAttachmentsFor(DataObject parent)
    {
        return getAttachmentsFor(parent.createKey());
    }

    @Override
    public List<Attachment> getAttachmentsFor(Key parent)
    {
        List<Attachment> list = new ArrayList<Attachment>();
        Query query = new Query(Attachment.KIND);
        query.setAncestor(parent);
        PreparedQuery prepared = datastore.prepare(query);
        for (Entity entity : prepared.asIterable())
        {
            try
            {
                if (parent.equals(entity.getParent()))
                {
                    // only direct parents
                    Attachment attachment = (Attachment) newInstance(entity);
                    list.add(attachment);
                }
            }
            catch (EntityNotFoundException ex)
            {
                throw new IllegalArgumentException(ex);
            }
        }
        return list;
    }

    @Override
    public List<Title> getTitles() throws EntityNotFoundException
    {
        List<Title> list = new ArrayList<>();
        Query query = new Query(Title.KIND);
        PreparedQuery prepared = datastore.prepare(query);
        for (Entity entity : prepared.asIterable())
        {
            list.add((Title)newInstance(entity));
        }
        return list;
    }

}

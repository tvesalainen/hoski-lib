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
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Timo Vesalainen
 */
public interface DSUtils
{
    List<Entity> getChilds(Key parent, String... kinds);
            
    String getMessage(String key);
    
    Messages getMessages();
    
    void putMessages(Messages messages);
    
    List<String> kindList();
    
    Iterator<Entity> load(List<String> kindList);
    
    void store(Iterator<Entity> entities);
    
    int remove(List<String> kindList);
    
    int remove(long year);
    
    int backup(long year, ObjectOutputStream out) throws IOException;
    
    int backup(List<String> kindList, ObjectOutputStream out) throws IOException;
    
    int restore(ObjectInputStream in) throws IOException;
    
    int restore(Collection<String> kindList, ObjectInputStream in) throws IOException;
    
    DataObject newInstance(Key key) throws EntityNotFoundException;
    
    DataObject newInstance(Entity entity) throws EntityNotFoundException;
    
    DataObjectModel getModel(String kind);
    
    Key getRootKey();
    
    Key getYearKey();
    
    void addYear(int year);
    
    Entity createEntity(Map<String, String[]> parameters);

    Map<String, String[]> getMap(Entity entity);

    List<AnyDataObject> retrieve(String kind, String queryProperty, Object queryValue, String... properties);
    
    Entity put(DataObject dataObject);
    
    void put(List<? extends DataObject> dataObjectList);
    
    void delete(DataObject dataObject);
    
    void deleteWithChilds(DataObject dataObject, String... kinds);
    
    void delete(List<? extends DataObject> dataObjectList);
    
    List<Entity> convert(List<? extends DataObject> dataObjectList);
    
    Entity get(Key key) throws EntityNotFoundException;

    void upload(DataObject attachTo, Attachment.Type type, String title, File... files) throws IOException;
    
    void removeAttachments(List<Attachment> attachments) throws IOException;
    
    List<Attachment> getAttachmentsFor(DataObject parent);
    
    List<Attachment> getAttachmentsFor(Key parent);
    
    List<Title> getTitles() throws EntityNotFoundException;
}

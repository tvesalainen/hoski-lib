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

import au.com.bytecode.opencsv.CSVWriter;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import fi.hoski.util.EntityReferences;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Timo Vesalainen
 */
public class DataObject implements Cloneable
{

    private static final Pattern REF = Pattern.compile("\\$\\{([^\\}]+)\\}");
    protected DataObjectModel model;
    protected DataObjectData data;
    protected DataObject parent;
    protected List<DataObjectObserver> observers;

    protected DataObject(DataObjectData data)
    {
        this.model = data.model;
        this.data = data;
    }

    protected DataObject(DataObjectModel model, DataObjectData data)
    {
        this.model = model;
        this.data = data;
    }

    protected DataObject(DataObjectModel model, Entity entity)
    {
        if (!model.kind.equals(entity.getKind()))
        {
            throw new IllegalArgumentException("entity kind " + entity.getKind() + " unexpected");
        }
        this.model = model;
        this.data = new EntityData(entity, model);
    }
    /**
     * Returns a String where references to DataObject values are replaced with actual values.
     * E.g. reference ${Firstname} will be replaced with property 'Firstname' value.
     * @param format
     * @return 
     */
    public String format(String format)
    {
        Matcher m = REF.matcher(format);
        StringBuffer sb = new StringBuffer();
        while (m.find())
        {
            Object ob = get(m.group(1));
            if (ob != null)
            {
                m.appendReplacement(sb, ob.toString());
            }
            else
            {
                m.appendReplacement(sb, m.group());
            }
        }
        m.appendTail(sb);
        return sb.toString();
    }

    @Override
    public DataObject clone()
    {
        DataObject dataObject = new DataObject(data.clone());
        dataObject.parent = parent;
        ArrayList<DataObjectObserver> al = (ArrayList<DataObjectObserver>) observers;
        dataObject.observers = (List<DataObjectObserver>) al.clone();
        return dataObject;
    }

    public void addObserver(DataObjectObserver observer)
    {
        if (observers == null)
        {
            observers = new ArrayList<DataObjectObserver>();
        }
        observers.add(observer);
    }

    public void removeObserver(DataObjectObserver observer)
    {
        if (observers != null)
        {
            observers.remove(observer);
        }
    }

    protected void fireChange(String property, Object oldValue, Object newValue)
    {
        if (observers != null)
        {
            for (DataObjectObserver observer : observers)
            {
                observer.changed(this, property, oldValue, newValue);
            }
        }
    }

    public DataObject getParent()
    {
        return parent;
    }

    public void setParent(DataObject parent)
    {
        this.parent = parent;
    }

    private void populateFrom(Entity entity)
    {
        EntityData et = new EntityData(entity, model);
        for (Map.Entry<String, Object> entry : entrySet())
        {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Options)
            {
                Options sel = (Options) value;
                value = sel.getSelection();
            }
            et.set(key, value);
        }
    }

    public DataObjectModel getModel()
    {
        return model;
    }

    public static boolean isEmpty(Object value)
    {
        if (value == null)
        {
            return true;
        }
        if (value instanceof String)
        {
            String s = (String) value;
            return s.isEmpty();
        }
        if (value instanceof Text)
        {
            Text t = (Text) value;
            return t.getValue().isEmpty();
        }
        return false;
    }

    public String getString(String property)
    {
        Object ob = get(property);
        if (ob == null)
        {
            return null;
        }
        if (ob instanceof Text)
        {
            Text text = (Text) ob;
            return text.getValue();
        }
        else
        {
            return ob.toString();
        }
    }

    public Object get(String property)
    {
        if (!model.typeMap.containsKey(property))
        {
            throw new IllegalArgumentException("unknown property " + property);
        }
        Object value = data.get(property);
        if (value == null)
        {
            return model.getDefault(property);
        }
        else
        {
            return value;
        }
    }

    public String getKind()
    {
        return model.kind;
    }

    public String getKindString()
    {
        return model.getLabel(getKind());
    }

    public String getProperty(int index)
    {
        return model.getProperty(index);
    }

    public String getPropertyString(String property)
    {
        return model.getLabel(property);
    }

    public String getPropertyString(int index)
    {
        return model.getLabel(getProperty(index));
    }

    public Class<?> getType(String property)
    {
        return model.getType(property);
    }

    public Class<?> getType(int index)
    {
        return model.getType(getProperty(index));
    }

    public boolean isIndex(String property)
    {
        return model.indexSet.contains(property);
    }

    public void set(String property, Object value)
    {
        Class<?> type = model.typeMap.get(property);
        if (type == null)
        {
            throw new IllegalArgumentException("unknown property " + property);
        }
        Object oldValue = get(property);
        if (isEmpty(value))
        {
            Object def = getDefault(property);
            if (def != null)
            {
                data.set(property, def);
                fireChange(property, oldValue, def);
            }
            else
            {
                data.remove(property);
                fireChange(property, oldValue, null);
            }
        }
        else
        {
            value = model.convert(property, value);
            data.set(property, value);
            fireChange(property, oldValue, value);
        }
    }

    public void setAll(Map<String, Object> m)
    {
        for (String key : m.keySet())
        {
            if (model.typeMap.containsKey(key))
            {
                set(key, m.get(key));
            }
        }
    }

    /**
     * Returns The first, in property order, property which is mandatory and is
     * not set. If all mandatory properties have values, returns null.
     *
     * @return
     */
    public String firstMandatoryNullProperty()
    {
        for (String property : model.propertyList)
        {
            if (
                    model.mandatorySet.contains(property) && 
                    model.defaultMap.get(property) == null &&
                data.get(property) == null
                    )
            {
                return property;
            }
        }
        return firstMandatoryNullProperty(model.propertyList);
    }

    public String firstMandatoryNullProperty(List<String> properties)
    {
        for (String property : properties)
        {
            if (
                    model.mandatorySet.contains(property) && 
                    model.defaultMap.get(property) == null &&
                data.get(property) == null
                    )
            {
                return property;
            }
        }
        return null;
    }

    public String createKeyString()
    {
        return KeyFactory.keyToString(createKey());
    }

    /**
     * Return new Entity
     *
     * @return
     */
    public Entity createEntity()
    {
        return new Entity(createKey());
    }

    public Key createKey()
    {
        throw new UnsupportedOperationException("override!");
    }

    public int propertyCount()
    {
        return model.propertyList.size();
    }

    protected Object getDefault(String property)
    {
        return data.model.getDefault(property);
    }

    public Object getTypeDefault(String property)
    {
        Object def = getDefault(property);
        if (def != null)
        {
            return def;
        }
        return data.model.getTypeDefault(property);
    }

    public Map<String, Object> getAll()
    {
        return data.getAll();
    }

    public Iterable<Entry<String, Object>> entrySet()
    {
        return data.entrySet();
    }

    public void setEntity(Entity entity)
    {
        data = new EntityData(entity, model);
    }

    public Entity getEntity()
    {
        Entity entity = data.getEntity();
        if (entity != null)
        {
            return entity;
        }
        else
        {
            Entity ne = createEntity();
            populateFrom(ne);
            return ne;
        }
    }

    /**
     * Convenience method to support ServletRequest.
     *
     * @param map
     */
    public void populate(Map<String, String[]> map)
    {
        for (Entry<String, String[]> entry : map.entrySet())
        {
            String property = entry.getKey();
            if (model.typeMap.containsKey(property))
            {
                String[] values = entry.getValue();
                if (values.length > 1)
                {
                    throw new UnsupportedOperationException(property + " has multivalue " + values);
                }
                if (values.length == 1)
                {
                    set(property, values[0]);
                }
            }
        }
    }

    public String getFields()
    {
        StringBuilder sb = new StringBuilder();
        for (String property : model.propertyList)
        {
            Object value = get(property);
            if (value != null)
            {
                sb.append(String.format("%20s = %s\n", model.getLabel(property), value.toString()));
            }
        }
        return sb.toString();
    }

    public String getFieldsAsHtmlTable()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='" + getClass().getSimpleName() + "'>");
        for (String property : model.propertyList)
        {
            Object value = get(property);
            if (value != null)
            {
                sb.append(
                        String.format("<tr><th>%s</th><td>%s</td></tr>",
                        model.getLabel(property),
                        EntityReferences.encode(value.toString())));
            }
        }
        sb.append("</table>");
        return sb.toString();
    }
    /**
     * Writes collection of DataObjects in cvs format. OutputStream is not closed
     * after operation.
     * @param os
     * @param collection
     * @throws IOException 
     */
    public static void writeCVS(DataObjectModel model, Collection<? extends DataObject> collection, OutputStream os) throws IOException
    {
        BufferedOutputStream bos = new BufferedOutputStream(os);
        OutputStreamWriter osw = new OutputStreamWriter(bos, "ISO-8859-1");
        CSVWriter writer = new CSVWriter(osw, ',', '"', "\r\n");
        String[] line = model.getProperties();
        writer.writeNext(line);
        for (DataObject dob : collection)
        {
            int index = 0;
            for (String property : model.getPropertyList())
            {
                Object value = dob.get(property);
                if (value != null)
                {
                    line[index++] = value.toString();
                }
                else
                {
                    line[index++] = "";
                }
            }
            writer.writeNext(line);
        }
        writer.flush();
    }
    
    public static List<DataObject> convert(ResultSetModel model, ResultSet rs) throws SQLException
    {
        return convert(model, rs, null);
    }
    public static List<DataObject> convert(ResultSetModel model, ResultSet rs, ResultSetFilter filter) throws SQLException
    {
        List<DataObject> list = new ArrayList<DataObject>();
        while (rs.next())
        {
            ResultSetData resultSetData = new ResultSetData(model, rs);
            if (filter == null || filter.accept(resultSetData))
            {
                list.add(new DataObject(model, resultSetData));
            }
        }
        return list;
    }
}

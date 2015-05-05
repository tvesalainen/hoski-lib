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

import fi.hoski.util.ConvertUtil;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author Timo Vesalainen
 */
public class DataObjectModel implements Cloneable
{
    public static final ResourceBundle resourceBundle = ResourceBundle.getBundle("fi/hoski/datastore/repository/fields");

    private static final Map<Class<?>,Object> typeDefaultMap = new HashMap<Class<?>,Object>();
    static
    {
        typeDefaultMap.put(Integer.class, new Integer(0));
        typeDefaultMap.put(Long.class, new Long(0));
        typeDefaultMap.put(Short.class, new Short((short)0));
        typeDefaultMap.put(Byte.class, new Byte((byte)0));
        typeDefaultMap.put(Character.class, new Character((char)0));
        typeDefaultMap.put(Float.class, new Float(0));
        typeDefaultMap.put(Double.class, new Double(0));
        typeDefaultMap.put(Boolean.class, Boolean.FALSE);
    }
    String kind = "";
    Map<String,Class<?>> typeMap = new HashMap<String,Class<?>>();
    Map<String,Object> defaultMap = new HashMap<String,Object>();
    Set<String> passwordSet = new HashSet<String>();
    Set<String> indexSet = new HashSet<String>();
    Set<String> mandatorySet = new HashSet<String>();
    List<String> propertyList = new ArrayList<String>();

    public DataObjectModel()
    {
    }

    public DataObjectModel(String kind)
    {
        this.kind = kind;
    }

    void addAll(String prefix, DataObjectModel model)
    {
        for (String property : model.propertyList)
        {
            if (model.typeMap.containsKey(property))
            {
                typeMap.put(prefix+"."+property, model.typeMap.get(property));
            }
            if (model.defaultMap.containsKey(property))
            {
                defaultMap.put(prefix+"."+property, model.defaultMap.get(property));
            }
            if (model.passwordSet.contains(property))
            {
                passwordSet.add(prefix+"."+property);
            }
            if (model.indexSet.contains(property))
            {
                indexSet.add(prefix+"."+property);
            }
            if (model.mandatorySet.contains(property))
            {
                mandatorySet.add(prefix+"."+property);
            }
            propertyList.add(prefix+"."+property);
        }
    }
    public void property(String name)
    {
        property(name, String.class, false, false, null);
    }
    public void property(String name, Class<?> type)
    {
        property(name, type, false, false, null);
    }
    public void property(String name, Class<?> type, boolean indexed)
    {
        property(name, type, indexed, false, null);
    }
    public void property(String name, Class<?> type, boolean indexed, boolean mandatory)
    {
        property(name, type, indexed, mandatory, null);
    }
    public void property(String name, Class<?> type, boolean indexed, boolean mandatory, Object defVal)
    {
        typeMap.put(name, type);
        if (indexed)
        {
            indexSet.add(name);
        }
        if (mandatory)
        {
            mandatorySet.add(name);
        }
        propertyList.add(name);
        if (defVal != null)
        {
            if (type.isInstance(defVal))
            {
                defaultMap.put(name, defVal);
            }
            else
            {
                throw new IllegalArgumentException(defVal+" cannot be default value of type "+type);
            }
        }
    }
    public void setPassword(String property)
    {
        passwordSet.add(property);
    }
    public boolean isPassword(String property)
    {
        return passwordSet.contains(property);
    }
    /**
     * Tries to convert object to model type
     * @param value
     * @return 
     */
    public Object convert(String property, Object value)
    {
        if (value == null)
        {
            return defaultMap.get(property);
        }
        Class<?> type = getType(property);
        if (type == null)
        {
            throw new IllegalArgumentException(property+" unknown");
        }
        return ConvertUtil.convertTo(value, type);
    }
    /**
     * Returns a clone of this model where only propertyList starting from from 
     * are present.
     * @param from
     * @return 
     */
    public DataObjectModel view(int from)
    {
        DataObjectModel view = clone();
        view.propertyList = propertyList.subList(from, propertyList.size());
        return view;
    }
    /**
     * Returns a clone of this model where only propertyList starting from from 
     * ending to to (exclusive) are present.
     * @param from
     * @param to
     * @return 
     */
    public DataObjectModel view(int from, int to)
    {
        DataObjectModel view = clone();
        view.propertyList = propertyList.subList(from, to);
        return view;
    }
    /**
     * Returns a clone of this model where only listed propertyList are present.
     * @param properties
     * @return 
     */
    public DataObjectModel view(String... properties)
    {
        DataObjectModel view = clone();
        view.propertyList = new ArrayList<String>();
        for (String property : properties)
        {
            view.propertyList.add(property);
        }
        return view;
    }
    /**
     * Returns a clone of this model where only properties that have values are present
     * @param dos
     * @return 
     */
    public DataObjectModel view(Collection<? extends DataObject> dos)
    {
        DataObjectModel view = clone();
        view.propertyList = new ArrayList<String>();
        Set<String> propertySet = new HashSet<String>();
        for (DataObject dob : dos)
        {
            for (String property : propertyList)    // TODO performance
            {
                Object value = dob.get(property);
                if (value != null)
                {
                    propertySet.add(property);
                }
            }
        }
        for (String property : propertyList)
        {
            if (propertySet.contains(property))
            {
                view.propertyList.add(property);
            }
        }
        return view;
    }
    /**
     * Returns a clone of model where listed propertyList are hidden;
     * @param properties
     * @return 
     */
    public DataObjectModel hide(String... properties)
    {
        DataObjectModel view = clone();
        view.propertyList = new ArrayList<String>();
        view.propertyList.addAll(this.propertyList);
        for (String property : properties)
        {
            view.propertyList.remove(property);
        }
        return view;
    }
    @Override
    public DataObjectModel clone()
    {
        try
        {
            return (DataObjectModel) super.clone();
        }
        catch (CloneNotSupportedException ex)
        {
            throw new UnsupportedOperationException("shouldn't happen");
        }
    }

    /*
    public String getKind()
    {
        return kind;
    }
    */

    public List<String> getPropertyList()
    {
        return propertyList;
    }

    public String[] getProperties()
    {
        return propertyList.toArray(new String[propertyList.size()]);
    }

    public Map<String, Class<?>> getTypeMap()
    {
        return typeMap;
    }

    public Class<?> getType(String property)
    {
        return typeMap.get(property);
    }
    public Object getDefault(String property)
    {
        return defaultMap.get(property);
    }
    public String getProperty(int index)
    {
        return propertyList.get(index);
    }
    public Class<?> getType(int index)
    {
        return getType(getProperty(index));
    }

    public int propertyCount()
    {
        return propertyList.size();
    }

    public String getLabel(String property)
    {
        String[] ss = property.split("\\.");
        if (ss.length == 1)
        {
            try
            {
                return resourceBundle.getString(property);
            }
            catch (MissingResourceException ex)
            {
                return property;
            }
        }
        else
        {
            if (ss.length == 2)
            {
                try
                {
                    return resourceBundle.getString(ss[1]);
                }
                catch (MissingResourceException ex)
                {
                    return ss[1];
                }
            }
            else
            {
                throw new IllegalArgumentException(property);
            }
        }
    }

    public Object getTypeDefault(String property)
    {
        Class<?> type = getType(property);
        Object def = typeDefaultMap.get(type);
        if (def != null)
        {
            return def;
        }
        try
        {
            Constructor<?>[] constructors = type.getConstructors();
            for (Constructor cons : constructors)
            {
                if (cons.getParameterTypes().length == 0)
                {
                    return cons.newInstance();
                }
            }
            for (Constructor cons : constructors)
            {
                Class[] parameterTypes = cons.getParameterTypes();
                if (parameterTypes.length == 1 && String.class.equals(parameterTypes[0]))
                {
                    return cons.newInstance("");
                }
            }
        }
        catch (InstantiationException ex)
        {
            throw new IllegalArgumentException(ex);
        }
        catch (IllegalAccessException ex)
        {
            throw new IllegalArgumentException(ex);
        }
        catch (IllegalArgumentException ex)
        {
            throw new IllegalArgumentException(ex);
        }
        catch (InvocationTargetException ex)
        {
            throw new IllegalArgumentException(ex);
        }
        throw new IllegalArgumentException("type default for "+property+" not found");
    }



}

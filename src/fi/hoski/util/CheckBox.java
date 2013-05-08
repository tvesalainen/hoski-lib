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

package fi.hoski.util;

/**
 * @author Timo Vesalainen
 */
public class CheckBox 
{
    private String title;
    private String clazz;
    private String name;
    private String value;
    private boolean disabled;
    private boolean checked;

    public CheckBox(String title, String clazz, String name, String value, boolean disabled, boolean checked)
    {
        this.title = title;
        this.clazz = clazz;
        this.name = name;
        this.value = value;
        this.disabled = disabled;
        this.checked = checked;
    }

    @Override
    public String toString()
    {
        String dis = "";
        if (disabled)
        {
            dis = " disabled='true'";
        }
        String chk = "";
        if (checked)
        {
            chk = " checked='true'";
        }
        return "<div class='" + clazz + "'><input type=\"checkbox\"" + " name=" + name + " value=" + value + dis+chk+">" + title + "</div>";
    }

}

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


import com.google.appengine.tools.remoteapi.RemoteApiInstaller;
import com.google.appengine.tools.remoteapi.RemoteApiOptions;
import java.io.IOException;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author Timo Vesalainen
 */
public abstract class RemoteAppEngine<T>
{
    private static String username;
    private static RemoteApiOptions options;

    public static void init(String server, String user, String password)
    {
        username = user;
        options = new RemoteApiOptions();
        options.server(server, 443);
        options.credentials(user, password);
    }
    
    public T call() throws IOException
    {

        RemoteApiInstaller installer = new RemoteApiInstaller();
        installer.install(options);
        try
        {
            T t = run();
            options.reuseCredentials(username, installer.serializeCredentials());
            return t;
        }
        finally
        {
            installer.uninstall();
        }
    }

    protected abstract T run() throws IOException;
}

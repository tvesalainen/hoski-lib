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

/**
 * A helper class to make remote connection to app engine.
 * 
 * <p>Implement run method for app engine code. 
 * 
 * @author Timo Vesalainen
 * @param <T>
 */
public abstract class RemoteAppEngine<T>
{
    private static RemoteApiOptions options;

    public static void init(String server)
    {
        options = new RemoteApiOptions();
        options.server(server, 443);
        options.useApplicationDefaultCredential();
    }
    
    public T call() throws IOException
    {

        RemoteApiInstaller installer = new RemoteApiInstaller();
        installer.install(options);
        try
        {
            return run();
        }
        finally
        {
            installer.uninstall();
        }
    }

    protected abstract T run() throws IOException;
}

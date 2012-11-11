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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Timo Vesalainen
 */
public abstract class URLResource
{
    public static final long REFRESHINTERVAL = 10;  //*60*1000;  // 10 minutes
    public static final int TIMEOUT = 10000;   // 10 second
    protected URL url;
    private long lastModified;
    private long lastRefresh;
    private ReentrantLock lock = new ReentrantLock();
    protected Persistence persistence;

    protected URLResource(String url) throws MalformedURLException
    {
        this(new URL(url));
    }

    public URLResource(URL url)
    {
        this(url, null);
    }

    protected URLResource(String url, Persistence persistence) throws MalformedURLException
    {
        this(new URL(url), persistence);
    }

    public URLResource(URL url, Persistence persistence)
    {
        this.url = url;
        this.persistence = persistence;
        if (restore())
        {
            Long lm = (Long)persistence.get(url+".lastModified");
            if (lm != null)
            {
                lastModified = lm;
            }
        }
        
    }

    public void refresh() throws IOException
    {
        // try to refresh from cache
        if (persistence != null)
        {
            Long lm = (Long)persistence.get(url+".lastModified");
            if (lm != null)
            {
                if (lastModified < lm)
                {
                    if (restore())
                    {
                        lastModified = lm;
                        return;
                    }
                }
            }
        }
        lock.lock();
        try
        {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastRefresh > REFRESHINTERVAL)
            {
                lastRefresh = currentTime;
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(TIMEOUT);
                connection.setDoOutput(true);
                if (lastModified > 0)
                {
                    connection.setIfModifiedSince(lastModified);
                }
                switch (connection.getResponseCode())
                {
                    case HttpURLConnection.HTTP_OK:
                        update(connection);
                        lastModified = connection.getLastModified();
                        if (persistence != null)
                        {
                            persistence.put(url+".lastModified", lastModified);
                            store();
                        }
                        break;
                    case HttpURLConnection.HTTP_NOT_MODIFIED:
                        break;
                    default:
                        throw new IOException(url+": "+connection.getResponseCode());
                }
            }
        }
        finally
        {
            lock.unlock();
        }
    }
    protected void update(HttpURLConnection connection) throws IOException
    {
        update(connection.getInputStream());
    }
    protected abstract void update(InputStream in) throws IOException;
    protected abstract boolean restore();
    protected abstract void store();
}

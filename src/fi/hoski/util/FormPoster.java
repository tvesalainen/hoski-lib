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

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Timo Vesalainen
 */
public class FormPoster
{
    private static final String CRLF = "\r\n";
    private Map<String,String> formData = new HashMap<String,String>();
    private List<File> fileList = new ArrayList<File>();
    private URL action;

    public FormPoster(URL action)
    {
        this.action = action;
    }
    
    public void post() throws IOException
    {
        HttpURLConnection connection = (HttpURLConnection) action.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        String uid = UUID.randomUUID().toString();
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary="+uid);
        PrintStream ps = new PrintStream(connection.getOutputStream());
        for (String name : formData.keySet())
        {
            String value = formData.get(name);
            ps.append("--"+uid);
            ps.append(CRLF);
            ps.append("Content-Disposition: form-data; name=\""+name+"\"");
            ps.append(CRLF);
            ps.append(CRLF);
            ps.append(value);
            ps.append(CRLF);
        }
        byte[] buffer = new byte[8192];
        for (File file : fileList)
        {
            String name = file.getName();
            ps.append("--"+uid);
            ps.append(CRLF);
            ps.append("Content-Disposition: form-data; name=\""+name+"\"; filename=\""+name+"\"");
            ps.append(CRLF);
            ps.append("Content-Type: "+MimeTypes.getMimeType(file));
            ps.append(CRLF);
            ps.append("Content-Transfer-Encoding: binary");
            ps.append(CRLF);
            ps.append(CRLF);
            FileInputStream fis = new FileInputStream(file);
            int rc = fis.read(buffer);
            while (rc != -1)
            {
                ps.write(buffer, 0, rc);
                rc = fis.read(buffer);
            }
            fis.close();
            ps.append(CRLF);
        }
        ps.append("--"+uid+"--");
        ps.append(CRLF);
        ps.close();
        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
        {
            throw new IOException(connection.getResponseCode()+" "+connection.getResponseMessage());
        }
    }
    public void setFormData(String name, String value)
    {
        formData.put(name, value);
    }
    public void addFiles(File... files)
    {
        for (File file : files)
        {
            fileList.add(file);
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        try
        {
            URL url = new URL("https://hsk-12345.appspot.com/_ah/upload/AMmfu6aYjhKpH2VqaENgbR0TZCrLJcp-BsU4tADINYZAG2nvBOudr5aq_lzgAzVM2H7_QNhud5-0OnFIBTUE8IW0VLVDh6f3SPEne4AIXUhQSG5ElBq9d4A/ALBNUaYAAAAAT32fyjJNy3iQUXbaJr4JQAfkp0YPIrDq/");
            FormPoster poster = new FormPoster(url);
            poster.addFiles(new File("C:\\temp\\P1020355.JPG"));
            poster.post();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}

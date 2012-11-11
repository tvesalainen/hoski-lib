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

package fi.hoski.util.lys;

import fi.hoski.util.BoatInfo;
import fi.hoski.util.Persistence;
import fi.hoski.util.URLResource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.ParserConfigurationException;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableHeader;
import org.htmlparser.tags.TableRow;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;
import org.xml.sax.SAXException;

/**
 * @author Timo Vesalainen
 */
public class LYSBoatHTMLInfo extends URLResource implements BoatInfo
{
    private static final Map<String, Object> EMPTY_MAP = new HashMap<String, Object>();
    static
    {
        EMPTY_MAP.put(BoatInfo.RATING, "");
        EMPTY_MAP.put(BoatInfo.LYS, "");
        EMPTY_MAP.put(BoatInfo.LYSVAR, "");
    }
    private static final String[] HEADER = {"Purjeno", "Nimi", "Tyyppi", "vuosi", "Seura", "Omistaja", "LYS" ,"Var."};
    private static final Pattern LYS = Pattern.compile("[0-9,]+");
    private Map<String,String[]> boatMap;
    
    public LYSBoatHTMLInfo(String url) throws MalformedURLException, ParserConfigurationException, SAXException
    {
        super(url);
    }

    public LYSBoatHTMLInfo(String url, Persistence persistence) throws MalformedURLException
    {
        super(url, persistence);
    }

    @Override
    protected boolean restore()
    {
        if (persistence != null)
        {
            @SuppressWarnings("unchecked")
            Map<String, String[]> bm = (Map<String, String[]>) persistence.get(url+".boatMap");
            boatMap = bm;
            if (boatMap != null)
            {
                return true;
            }
        }
        boatMap = new HashMap<String,String[]>();
        return false;
    }

    @Override
    protected void store()
    {
        persistence.put(url+".boatMap", boatMap);
    }

    @Override
    protected void update(HttpURLConnection connection) throws IOException
    {
        try
        {
            boatMap.clear();
            boolean headerFound = false;
            Parser parser = new Parser(connection);
            NodeList nl = (NodeList) parser.parse(new NodeClassFilter(TableRow.class));
            SimpleNodeIterator elements = nl.elements();
            while (elements.hasMoreNodes())
            {
                TableRow row = (TableRow) elements.nextNode();
                TableHeader[] headers = row.getHeaders();
                String[] hdsr = getText(headers);
                if (Arrays.deepEquals(HEADER, hdsr))
                {
                    headerFound = true;
                }
                if (headerFound)
                {
                    TableColumn[] columns = row.getColumns();
                    String[] cols = getText(columns);
                    if (cols.length == HEADER.length)
                    {
                        Matcher matcher = LYS.matcher(cols[6]);
                        if (matcher.matches())
                        {
                            boatMap.put(cols[0], cols);
                        }
                    }
                }
            }
        }
        catch (ParserException ex)
        {
            throw new IOException(ex);
        }
    }
    private String[] getText(Node[] nodes)
    {
        String[] arr = new String[nodes.length];
        for (int ii=0;ii<nodes.length;ii++)
        {
            arr[ii] = getText(nodes[ii]);
        }
        return arr;
    }
    private String getText(Node node)
    {
        NodeList children = node.getChildren();
        if (children != null)
        {
            SimpleNodeIterator elements = children.elements();
            while (elements.hasMoreNodes())
            {
                Node n = elements.nextNode();
                if (n instanceof TextNode)
                {
                    TextNode text = (TextNode) n;
                    return text.getText();
                }
                else
                {
                    String text = getText(n);
                    if (text != null)
                    {
                        return text;
                    }
                }
            }
        }
        return null;
    }
    @Override
    protected void update(InputStream in) throws IOException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Map<String, Object> getInfo(String nationality, int sailNumber)
    {
        try
        {
            refresh();
            String[] arr = boatMap.get(nationality+"-"+sailNumber);
            if (arr != null)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put(BoatInfo.NAT, nationality);
                map.put(BoatInfo.SAILNO, sailNumber);
                map.put(BoatInfo.BOAT, arr[1]);
                map.put(BoatInfo.CLASS, arr[2]);
                map.put(BoatInfo.CLUB, arr[4]);
                map.put(BoatInfo.HELMNAME, arr[5]);
                map.put(BoatInfo.LYS, arr[6]);
                map.put(BoatInfo.RATING, arr[6].replace(',', '.'));
                map.put(BoatInfo.LYSVAR, arr[7]);
                map.put(BoatInfo.NOTES, arr[7]);
                return map;
            }
            else
            {
                return EMPTY_MAP;
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(LYSBoatHTMLInfo.class.getName()).log(Level.SEVERE, null, ex);
            return EMPTY_MAP;
        }
    }
    @Override
    public Map<String, Object> getInfo(String boatType)
    {
        return EMPTY_MAP;
    }

    @Override
    public List<String> getBoatTypes()
    {
        return EMPTY_LIST;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        try
        {
            LYSBoatHTMLInfo info = new LYSBoatHTMLInfo("http://www.avomeripurjehtijat.fi/lysmittakirjat/?old");
            info.refresh();
            System.err.println(info.getInfo("FIN", 7937));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}

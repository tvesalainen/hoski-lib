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
public class LYSClassHTMLInfo extends URLResource implements BoatInfo
{
    private static final Map<String, Object> EMPTY_MAP = new HashMap<String, Object>();
    static
    {
        EMPTY_MAP.put(BoatInfo.RATING, "");
        EMPTY_MAP.put(BoatInfo.LYS, "");
        EMPTY_MAP.put(BoatInfo.LYSVAR, "");
    }
    private static final String[] HEADER = {"Venetyyppi", "LYS", "VAR", "Suunnittelija"};
    private static final Pattern LYS = Pattern.compile("[0-9,]+");
    private Map<String,String[]> boatMap;
    private List<String> boatList;
    
    public LYSClassHTMLInfo(String url) throws MalformedURLException, ParserConfigurationException, SAXException
    {
        super(url);
    }

    public LYSClassHTMLInfo(String url, Persistence persistence) throws MalformedURLException
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
            @SuppressWarnings("unchecked")
            List<String> bl = (List<String>) persistence.get(url+".boatList");
            boatList = bl;
            if (boatMap != null && boatList != null)
            {
                return true;
            }
        }
        boatMap = new HashMap<String,String[]>();
        boatList = new ArrayList<String>();
        return false;
    }

    @Override
    protected void store()
    {
        persistence.put(url+".boatMap", boatMap);
        persistence.put(url+".boatList", boatList);
    }

    @Override
    protected void update(HttpURLConnection connection) throws IOException
    {
        try
        {
            boatMap.clear();
            boatList.clear();
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
                        Matcher matcher = LYS.matcher(cols[1]);
                        if (matcher.matches())
                        {
                            boatMap.put(cols[0].toUpperCase(), cols);
                            boatList.add(cols[0]);
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
        return EMPTY_MAP;
    }
    @Override
    public Map<String, Object> getInfo(String boatType)
    {
        try
        {
            if (boatType != null)
            {
                refresh();
                String[] arr = boatMap.get(boatType.toUpperCase());
                if (arr != null)
                {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(BoatInfo.CLASS, arr[0]);
                    map.put(BoatInfo.LYS, arr[1]);
                    map.put(BoatInfo.RATING, arr[1].replace(',', '.'));
                    map.put(BoatInfo.LYSVAR, arr[2]);
                    map.put(BoatInfo.NOTES, arr[2]);
                    map.put(BoatInfo.DESIGNER, arr[3]);
                    return map;
                }
            }
            return EMPTY_MAP;
        }
        catch (IOException ex)
        {
            Logger.getLogger(LYSClassHTMLInfo.class.getName()).log(Level.SEVERE, null, ex);
            return EMPTY_MAP;
        }
    }

    @Override
    public List<String> getBoatTypes()
    {
        try
        {
            refresh();
            return boatList;
        }
        catch (IOException ex)
        {
            Logger.getLogger(LYSClassHTMLInfo.class.getName()).log(Level.SEVERE, null, ex);
            return EMPTY_LIST;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        try
        {
            LYSClassHTMLInfo info = new LYSClassHTMLInfo("http://avomeripurjehtijat.fi/lysmittakirjat/taulukko.php");
            info.refresh();
            System.err.println(info.getInfo("Inferno 31"));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}

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

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO Change to use LPG
 * @author tkv
 */
public class EntityReferences
{

    private static final Pattern ENTITY = Pattern.compile("&lt;!ENTITY");
    private static final Pattern CDATA = Pattern.compile("DATA");
    private static final Pattern DATA = Pattern.compile("\"&amp;#([0-9]+);\"");
    private static final Map<String, Integer> codePointMap = new HashMap<String, Integer>();
    private static final Map<Integer, String> entityMapPlain = new HashMap<Integer, String>();
    private static final Map<Integer, String> entityMapISO8859 = new HashMap<Integer, String>();
    private static final Map<Integer, String> entityMapUTF8 = new HashMap<Integer, String>();

    static
    {
        init("nbsp", 160);
        init("iexcl", 161);
        init("cent", 162);
        init("pound", 163);
        init("curren", 164);
        init("yen", 165);
        init("brvbar", 166);
        init("sect", 167);
        init("uml", 168);
        init("copy", 169);
        init("ordf", 170);
        init("laquo", 171);
        init("not", 172);
        init("shy", 173);
        init("reg", 174);
        init("macr", 175);
        init("deg", 176);
        init("plusmn", 177);
        init("sup2", 178);
        init("sup3", 179);
        init("acute", 180);
        init("micro", 181);
        init("para", 182);
        init("middot", 183);
        init("cedil", 184);
        init("sup1", 185);
        init("ordm", 186);
        init("raquo", 187);
        init("frac14", 188);
        init("frac12", 189);
        init("frac34", 190);
        init("iquest", 191);
        init("Agrave", 192);
        init("Aacute", 193);
        init("Acirc", 194);
        init("Atilde", 195);
        init("Auml", 196);
        init("Aring", 197);
        init("AElig", 198);
        init("Ccedil", 199);
        init("Egrave", 200);
        init("Eacute", 201);
        init("Ecirc", 202);
        init("Euml", 203);
        init("Igrave", 204);
        init("Iacute", 205);
        init("Icirc", 206);
        init("Iuml", 207);
        init("ETH", 208);
        init("Ntilde", 209);
        init("Ograve", 210);
        init("Oacute", 211);
        init("Ocirc", 212);
        init("Otilde", 213);
        init("Ouml", 214);
        init("times", 215);
        init("Oslash", 216);
        init("Ugrave", 217);
        init("Uacute", 218);
        init("Ucirc", 219);
        init("Uuml", 220);
        init("Yacute", 221);
        init("THORN", 222);
        init("szlig", 223);
        init("agrave", 224);
        init("aacute", 225);
        init("acirc", 226);
        init("atilde", 227);
        init("auml", 228);
        init("aring", 229);
        init("aelig", 230);
        init("ccedil", 231);
        init("egrave", 232);
        init("eacute", 233);
        init("ecirc", 234);
        init("euml", 235);
        init("igrave", 236);
        init("iacute", 237);
        init("icirc", 238);
        init("iuml", 239);
        init("eth", 240);
        init("ntilde", 241);
        init("ograve", 242);
        init("oacute", 243);
        init("ocirc", 244);
        init("otilde", 245);
        init("ouml", 246);
        init("divide", 247);
        init("oslash", 248);
        init("ugrave", 249);
        init("uacute", 250);
        init("ucirc", 251);
        init("uuml", 252);
        init("yacute", 253);
        init("thorn", 254);
        init("yuml", 255);
        init("fnof", 402);
        init("Alpha", 913);
        init("Beta", 914);
        init("Gamma", 915);
        init("Delta", 916);
        init("Epsilon", 917);
        init("Zeta", 918);
        init("Eta", 919);
        init("Theta", 920);
        init("Iota", 921);
        init("Kappa", 922);
        init("Lambda", 923);
        init("Mu", 924);
        init("Nu", 925);
        init("Xi", 926);
        init("Omicron", 927);
        init("Pi", 928);
        init("Rho", 929);
        init("Sigma", 931);
        init("Tau", 932);
        init("Upsilon", 933);
        init("Phi", 934);
        init("Chi", 935);
        init("Psi", 936);
        init("Omega", 937);
        init("alpha", 945);
        init("beta", 946);
        init("gamma", 947);
        init("delta", 948);
        init("epsilon", 949);
        init("zeta", 950);
        init("eta", 951);
        init("theta", 952);
        init("iota", 953);
        init("kappa", 954);
        init("lambda", 955);
        init("mu", 956);
        init("nu", 957);
        init("xi", 958);
        init("omicron", 959);
        init("pi", 960);
        init("rho", 961);
        init("sigmaf", 962);
        init("sigma", 963);
        init("tau", 964);
        init("upsilon", 965);
        init("phi", 966);
        init("chi", 967);
        init("psi", 968);
        init("omega", 969);
        init("thetasym", 977);
        init("upsih", 978);
        init("piv", 982);
        init("bull", 8226);
        init("hellip", 8230);
        init("prime", 8242);
        init("Prime", 8243);
        init("oline", 8254);
        init("frasl", 8260);
        init("weierp", 8472);
        init("image", 8465);
        init("real", 8476);
        init("trade", 8482);
        init("alefsym", 8501);
        init("larr", 8592);
        init("uarr", 8593);
        init("rarr", 8594);
        init("darr", 8595);
        init("harr", 8596);
        init("crarr", 8629);
        init("lArr", 8656);
        init("uArr", 8657);
        init("rArr", 8658);
        init("dArr", 8659);
        init("hArr", 8660);
        init("forall", 8704);
        init("part", 8706);
        init("exist", 8707);
        init("empty", 8709);
        init("nabla", 8711);
        init("isin", 8712);
        init("notin", 8713);
        init("ni", 8715);
        init("prod", 8719);
        init("sum", 8721);
        init("minus", 8722);
        init("lowast", 8727);
        init("radic", 8730);
        init("prop", 8733);
        init("infin", 8734);
        init("ang", 8736);
        init("and", 8743);
        init("or", 8744);
        init("cap", 8745);
        init("cup", 8746);
        init("int", 8747);
        init("there4", 8756);
        init("sim", 8764);
        init("cong", 8773);
        init("asymp", 8776);
        init("ne", 8800);
        init("equiv", 8801);
        init("le", 8804);
        init("ge", 8805);
        init("sub", 8834);
        init("sup", 8835);
        init("nsub", 8836);
        init("sube", 8838);
        init("supe", 8839);
        init("oplus", 8853);
        init("otimes", 8855);
        init("perp", 8869);
        init("sdot", 8901);
        init("lceil", 8968);
        init("rceil", 8969);
        init("lfloor", 8970);
        init("rfloor", 8971);
        init("lang", 9001);
        init("rang", 9002);
        init("loz", 9674);
        init("spades", 9824);
        init("clubs", 9827);
        init("hearts", 9829);
        init("diams", 9830);
        init("quot", 34);
        init("amp", 38);
        init("lt", 60);
        init("gt", 62);
        init("OElig", 338);
        init("oelig", 339);
        init("Scaron", 352);
        init("scaron", 353);
        init("Yuml", 376);
        init("circ", 710);
        init("tilde", 732);
        init("ensp", 8194);
        init("emsp", 8195);
        init("thinsp", 8201);
        init("zwnj", 8204);
        init("zwj", 8205);
        init("lrm", 8206);
        init("rlm", 8207);
        init("ndash", 8211);
        init("mdash", 8212);
        init("lsquo", 8216);
        init("rsquo", 8217);
        init("sbquo", 8218);
        init("ldquo", 8220);
        init("rdquo", 8221);
        init("bdquo", 8222);
        init("dagger", 8224);
        init("Dagger", 8225);
        init("permil", 8240);
        init("lsaquo", 8249);
        init("rsaquo", 8250);
        init("euro", 8364);
    }

    private static final void init(String entity, int codePoint)
    {
        codePointMap.put(entity, codePoint);
        entityMapPlain.put(codePoint, entity);
        if (codePoint > 255)
        {
            entityMapISO8859.put(codePoint, entity);
        }
        if (
                "lt".equals(entity) ||
                "gt".equals(entity) ||
                "amp".equals(entity)
                )
        {
            entityMapUTF8.put(codePoint, entity);
            entityMapISO8859.put(codePoint, entity);
        }
    }
    /**
     * Encodes String into HTML format. &amp; -&gt; &amp;
     * @param str
     * @return
     */
    public static final String encode(String str, Charset charset)
    {
        return encode(str, charset.name());
    }
    /**
     * Encodes String into HTML format. &amp; -&gt; &amp;
     * @param str
     * @return
     */
    public static final String encode(String str, String charset)
    {
        if (charset.contains("8859"))
        {
            return encode(str, entityMapISO8859);
        }
        else
        {
            if (charset.toLowerCase().contains("utf"))
            {
                return encode(str, entityMapUTF8);
            }
            else
            {
                return encode(str, entityMapPlain);
            }
        }
    }
    /**
     * Encodes String into HTML format. &amp; -&gt; &amp;
     * @param str
     * @return
     */
    public static final String encode(String str)
    {
        return encode(str, entityMapPlain);
    }
    private static final String encode(String str, Map<Integer,String> map)
    {
        if (str != null)
        {
            StringBuilder sb = new StringBuilder();
            for (int ii = 0; ii < str.length(); ii++)
            {
                int codePoint = str.codePointAt(ii);
                String entity = map.get(codePoint);
                if (entity != null)
                {
                    sb.append("&" + entity + ";");
                }
                else
                {
                    sb.appendCodePoint(codePoint);
                }
            }
            return sb.toString();
        }
        else
        {
            return null;
        }
    }
    private static final Pattern ENT = Pattern.compile("&([a-zA-Z0-9#]+);");
    private static final Pattern HENT = Pattern.compile("#([0-9]+)");
    /**
     * Decodes String from HTML format. &amp; -&gt; &amp;
     * @param str
     * @return
     */
    public static final String decode(String str)
    {
        Matcher m = ENT.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (m.find())
        {
            String entity = m.group(1);
            Matcher m2 = HENT.matcher(entity);
            Integer codePoint = null;
            if (m2.matches())
            {
                codePoint = Integer.parseInt(m2.group(1));
            }
            else
            {
                codePoint = codePointMap.get(entity);
            }
            if (codePoint == null)
            {
                m.appendReplacement(sb, m.group());
            }
            else
            {
                String s = new String(new int[] {codePoint.intValue()}, 0, 1);
                m.appendReplacement(sb, s);
            }
        }
        m.appendTail(sb);
        return sb.toString();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        try
        {
            System.err.println(encode("<>&"));
            /*
            URL url = new URL("http://www.w3.org/TR/1999/REC-html401-19991224/sgml/entities.html");
            Scanner scanner = new Scanner(url.openStream());
            String entity = scanner.findWithinHorizon(ENTITY, 0);
            while (entity != null)
            {
            String reference = scanner.next();
            String cdata = scanner.findInLine(CDATA);
            if (cdata != null)
            {
            String data = scanner.findInLine(DATA);
            if (data != null)
            {
            Matcher mm = DATA.matcher(data);
            if (mm.matches())
            {
            int i = Integer.parseInt(mm.group(1));
            System.err.println("init(\""+reference+"\", "+i+");");
            }
            }
            }
            entity = scanner.findWithinHorizon(ENTITY, 0);
            }
             */
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}

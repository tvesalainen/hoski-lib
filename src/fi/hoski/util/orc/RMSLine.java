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
package fi.hoski.util.orc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;

/**
 *
 * @author tkv
 */
public class RMSLine
{
    private static final CharsetDecoder DECODER = new AlturaCharset().newDecoder();
    byte[] buf;

    public RMSLine(byte[] buf)
    {
        this.buf = buf;
    }

    public static RMSLine readLine(InputStream in) throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int cc = in.read();
        while (cc != -1 && cc != '\n')
        {
            if (cc != '\r')
            {
                baos.write(cc);
            }
            cc = in.read();
        }
        return new RMSLine(baos.toByteArray());
    }

    public boolean isEmpty()
    {
        return buf.length == 0;
    }

    public String substring(int begin, int end) throws CharacterCodingException
    {
        ByteBuffer bb = ByteBuffer.wrap(buf, begin, end-begin);
        try
        {
            CharBuffer cb = DECODER.decode(bb);
            return cb.toString();
        }
        catch (CharacterCodingException ex)
        {
            System.err.println(toString());
            throw ex;
        }
    }

    public String substring(int begin) throws CharacterCodingException
    {
        ByteBuffer bb = ByteBuffer.wrap(buf, begin, buf.length-begin);
        CharBuffer cb = DECODER.decode(bb);
        return cb.toString();
    }

    @Override
    public String toString()
    {
        return new String(buf);
    }

}

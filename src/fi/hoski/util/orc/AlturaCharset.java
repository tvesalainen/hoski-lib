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

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;

/**
 *
 * @author tkv
 */
public class AlturaCharset extends Charset
{
    private static final char Auml = 196;
    private static final char auml = 228;
    private static final char Ouml = 214;
    private static final char ouml = 246;
    private static final char Aring = 197;
    private static final char aring = 229;
    private static final char eacute = 233;

    public AlturaCharset()
    {
        super("ALTURA", new String[]{} );
    }

    @Override
    public boolean contains(Charset cs)
    {
        if (cs instanceof AlturaCharset)
        {
            return true;
        }
        if ("US-ASCII".equals(cs.name()))
        {
            return true;
        }
        return false;
    }

    @Override
    public CharsetDecoder newDecoder()
    {
        return new CharsetDecoderImpl(this);
    }

    @Override
    public CharsetEncoder newEncoder()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public class CharsetDecoderImpl extends CharsetDecoder
    {

        public CharsetDecoderImpl(Charset cs)
        {
            super(cs, 1, 2);
        }

        @Override
        protected CoderResult decodeLoop(ByteBuffer in, CharBuffer out)
        {
            while (in.hasRemaining())
            {
                if (!out.hasRemaining())
                {
                    return CoderResult.OVERFLOW;
                }
                byte b = in.get();
                if (b >= 0 && b < 128)
                {
                    out.put((char)b);
                }
                else
                {
                    int cc = b & 0xff;
                    switch (cc)
                    {
                        case 0x8f:
                            out.put(Aring);
                            break;
                        case 0xbd:
                        case 0x8e:
                            out.put(Auml);
                            break;
                        case 0x84:
                            out.put(auml);
                            break;
                        case 0xa2:
                        case 0x99:
                            out.put(Ouml);
                            break;
                        case 0x9d:
                        case 0x94:
                            out.put(ouml);
                            break;
                        case 0xa0:
                        case 0x86:
                            out.put(aring);
                            break;
                        case 0x9a:
                        case 0x90:
                        case 0x82:
                            out.put(eacute);
                            break;
                    default:
                            out.put('?');
                            break;
                    }
                }
            }
            return CoderResult.UNDERFLOW;
        }

    }
    public static void main(String... args)
    {
        try
        {

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}

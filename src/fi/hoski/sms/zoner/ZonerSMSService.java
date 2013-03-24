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
package fi.hoski.sms.zoner;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import fi.hoski.datastore.Repository;
import fi.hoski.datastore.repository.Keys;
import fi.hoski.datastore.repository.Messages;
import fi.hoski.sms.SMSService;
import fi.hoski.sms.SMSException;
import fi.hoski.sms.IllegalCharacterException;
import fi.hoski.sms.SMSStatus;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Timo Vesalainen
 * @see https://sms.zoner.fi/pdf/http_api.pdf
 */
public class ZonerSMSService implements SMSService
{

    public enum Field {USERNAME, PASSWORD, NUMBERFROM, NUMBERTO, MESSAGE, DR, ID }
    public static final Charset ISO_8859_15 = Charset.forName("ISO-8859-15");
    public static final CharsetEncoder ENCODER = ISO_8859_15.newEncoder();
    public static final int MAX1 = 160;
    public static final int MAX2 = 306;
    public static final int MAX3 = 459;
    private static final String URL_STRING = " https://sms.zoner.fi/sms.php";
    private EnumMap<Field,String> fieldMap = new EnumMap<>(Field.class);

    public ZonerSMSService(String username, String password)
    {
        set(Field.USERNAME, username);
        set(Field.PASSWORD, password);
    }

    public ZonerSMSService(DatastoreService datastore)
    {
        Key key = KeyFactory.createKey(Keys.getRootKey(), Repository.MESSAGES, Messages.NAME);
        Entity entity;
        try
        {
            entity = datastore.get(key);
            set(Field.USERNAME, entity.getProperty(Messages.SMSUSERNAME).toString());
            set(Field.PASSWORD, entity.getProperty(Messages.SMSPASSWORD).toString());
        }
        catch (EntityNotFoundException ex)
        {
            throw new IllegalArgumentException(ex);
        }
    }
    
    private void set(Field field, String value)
    {
        fieldMap.put(field, value);
    }
    private String getFrom()
    {
        return "12345"; // sonera bug
        /*
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        String email = user.getEmail();
        if (email.length() > 11)
        {
            email = email.substring(0, 11);
        }
        return email;
        */
    }
    @Override
    public SMSStatus send(String numberTo, String message) throws IOException, SMSException
    {
        return send(getFrom(), numberTo, message);
    }

    @Override
    public List<SMSStatus> send(List<String> numberTo, String message) throws IOException, SMSException
    {
        return send(getFrom(), numberTo, message);
    }

    @Override
    public SMSStatus send(String numberFrom, String numberTo, String message) throws IOException, SMSException
    {
        checkCredit(message);
        String trackingId = sendTo(numberFrom, numberTo, message);
        return new ZonerStatus(this, trackingId);
    }

    private void checkCredit(String message) throws IOException, SMSException
    {
        int left = messagesLeft();
        int count;
        try
        {
            count = messageCount(message);
        }
        catch (IllegalCharacterException ex)
        {
            throw new SMSException(ex);
        }
        if (left < count)
        {
            throw new OutOfCreditException(left+" left messages not enough");
        }
    }
    @Override
    public List<SMSStatus> send(String numberFrom, List<String> numberTos, String message) throws IOException, SMSException
    {
        checkCredit(message);
        List<String> trackinIdList = new ArrayList<String>();
        for (String numberTo : numberTos)
        {
            String trackingId = sendTo(numberFrom, numberTo, message);
            trackinIdList.add(trackingId);
        }
        List<SMSStatus> statusList = new ArrayList<SMSStatus>();
        for (String trackinId : trackinIdList)
        {
            SMSStatus smsStatus = new ZonerStatus(this, trackinId);
            statusList.add(smsStatus);
        }
        return statusList;
    }
    private String sendTo(String numberFrom, String numberTo, String message) throws IOException, SMSException
    {
        message = message.replace('€', (char)0x80);
        set(Field.NUMBERFROM, numberFrom);
        set(Field.NUMBERTO, numberTo);
        set(Field.MESSAGE, message);
        set(Field.DR, "1");
        String ok = service(Field.USERNAME, Field.PASSWORD, Field.NUMBERTO, Field.NUMBERFROM, Field.MESSAGE, Field.DR);
        String[] ss = ok.split(" ");
        return ss[1];
    }
    public String status(String trackingId) throws IOException, SMSException
    {
        set(Field.ID, trackingId);
        String status = service(Field.USERNAME, Field.PASSWORD, Field.ID);
        return status;
    }
    @Override
    public int messagesLeft() throws IOException, SMSException
    {
        String line = service(Field.USERNAME, Field.PASSWORD);
        try
        {
            return Integer.parseInt(line);
        }
        catch (NumberFormatException ex)
        {
            return 0;
        }
    }

    private String service(Field... fields) throws IOException, SMSException
    {
        StringBuilder sb = new StringBuilder();
        for (Field field : fields)
        {
            if (sb.length() > 0)
            {
                sb.append('&');
            }
            String name = field.name().toLowerCase();
            sb.append(name);
            sb.append('=');
            sb.append(URLEncoder.encode(fieldMap.get(field), "ISO-8859-15"));
        }
        String msg = sb.toString();
        URL url = new URL(URL_STRING);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(5000);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
        writer.write(msg);
        writer.close();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), ISO_8859_15));
            String line = reader.readLine();
            reader.close();
            if (line.startsWith("ERR"))
            {
                throw new SMSException(line+" "+msg);
            }
            return line;
        }
        else
        {
            throw new SMSException(connection.getResponseMessage());
        }
    }
    @Override
    public int messageCount(String message) throws CharacterCodingException, IllegalCharacterException
    {
        int size = messageSize(message);
        if (size <= MAX1)
        {
            return 1;
        }
        if (size <= MAX2)
        {
            return 2;
        }
        if (size <= MAX3)
        {
            return 3;
        }
        throw new IllegalArgumentException(message + " is too long");
    }

    @Override
    public int messageSize(String message) throws CharacterCodingException, IllegalCharacterException
    {
        CharBuffer cb = CharBuffer.wrap(message);
        ByteBuffer bb = ByteBuffer.allocate(message.length());
        ENCODER.reset();
        CoderResult result = ENCODER.encode(cb, bb, false);
        check(result, cb);
        int size = 0;
        bb.flip();
        for (int ii = 0; ii < bb.limit(); ii++)
        {
            byte cc = bb.get();
            switch (cc)
            {
                case '[':
                case '\\':
                case ']':
                case '^':
                case '{':
                case '|':
                case '}':
                case '~':
                case -92:   // Euro
                    size += 2;
                    break;
                default:
                    size++;
            }
        }
        return size;
    }

    private void check(CoderResult result, CharBuffer cb) throws CharacterCodingException, IllegalCharacterException
    {
        if (result.isUnderflow())
        {
            return;
        }
        if (result.isError())
        {
            int len = result.length();
            throw new IllegalCharacterException(cb.charAt(0), cb.position());
        }
        else
        {
            result.throwException();
        }
    }

    public static void main(String... args)
    {
        try
        {
            /*
            ZonerSMSService sms = new ZonerSMSService();
            sms.messageSize("abc€£$");
            sms.set(Field.USERNAME, "vesalainen");
            sms.set(Field.PASSWORD, "Fene9rak");
            System.err.println(sms.messagesLeft());
            //System.err.println(sms.send("Satama", "0401234567", "Virhe viesti"));
            * 
            */
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}

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
package fi.hoski.mail;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import fi.hoski.datastore.repository.DataObject;
import fi.hoski.datastore.repository.RaceEntry;
import fi.hoski.datastore.repository.Reservation;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * @author Timo Vesalainen
 */
public class MailServiceImpl implements MailService
{

    private static final Logger log = Logger.getLogger("fi.hoski.mail.MailServiceImpl");

    @Override
    public InternetAddress createInternetAddress(DataObject dataObject) throws UnsupportedEncodingException
    {
        if (dataObject instanceof Reservation)
        {
            return createInternetAddressFromReservation(dataObject.getAll());
        }
        if (dataObject instanceof RaceEntry)
        {
            RaceEntry raceEntry = (RaceEntry) dataObject;
            return new InternetAddress((String)raceEntry.get(RaceEntry.HELMEMAIL), (String)raceEntry.get(RaceEntry.HELMNAME));
        }
        throw new IllegalArgumentException(dataObject+" doensn't have email address");
    }

    private InternetAddress createInternetAddressFromReservation(Map<String, Object> map) throws UnsupportedEncodingException
    {
        String personal = map.get(Reservation.FIRSTNAME) + " " + map.get(Reservation.LASTNAME);
        String email = (String) map.get(Reservation.EMAIL);
        return new InternetAddress(email, personal);
    }

    @Override
    public void sendMail(String subject, String body, String htmlBody, InternetAddress... addresses) throws UnsupportedEncodingException
    {
        sendMail(subject, body, htmlBody, Arrays.asList(addresses));
    }

    @Override
    public void sendMail(String subject, String body, String htmlBody, List<InternetAddress> addresses) throws UnsupportedEncodingException
    {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        InternetAddress from = new InternetAddress(user.getEmail(), user.getUserId());
        sendMail(from, subject, body, htmlBody, addresses);
    }

    @Override
    public void sendMail(InternetAddress from, String subject, String body, String htmlBody, InternetAddress... addresses)
    {
        sendMail(from, subject, body, htmlBody, Arrays.asList(addresses));
    }

    @Override
    public void sendMail(InternetAddress from, String subject, String body, String htmlBody, List<InternetAddress> addresses)
    {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        try
        {
            Message msg = new MimeMessage(session);
            msg.setFrom(from);
            for (InternetAddress address : addresses)
            {
                msg.addRecipient(Message.RecipientType.BCC, address);
            }
            msg.setSubject(subject);
            Multipart mp = new MimeMultipart();
            msg.setContent(mp);
            if (body != null)
            {
                MimeBodyPart textPart = new MimeBodyPart();
                textPart.setText(body);
                mp.addBodyPart(textPart);
            }
            if (htmlBody != null)
            {
                MimeBodyPart htmlPart = new MimeBodyPart();
                htmlPart.setContent(htmlBody, "text/html");
                mp.addBodyPart(htmlPart);
            }
            Transport.send(msg);
            log.info("mail(from=" + from + " to=" + addresses + " subject=" + subject + " msg=" + msg + ")");
        }
        catch (AddressException e)
        {
            throw new IllegalArgumentException(e);
        }
        catch (MessagingException e)
        {
            throw new IllegalArgumentException(e);
        }
    }
}

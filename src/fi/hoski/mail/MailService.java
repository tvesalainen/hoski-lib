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

import fi.hoski.datastore.repository.DataObject;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import javax.mail.internet.InternetAddress;

/**
 *
 * @author Timo Vesalainen
 */
public interface MailService
{

    InternetAddress createInternetAddress(DataObject dataObject) throws UnsupportedEncodingException;

    void sendMail(String subject, String body, String htmlBody, List<InternetAddress> addresses) throws UnsupportedEncodingException;

    void sendMail(String subject, String body, String htmlBody, InternetAddress... addresses) throws UnsupportedEncodingException;

    void sendMail(InternetAddress from, String subject, String body, String htmlBody, InternetAddress... addresses);

    void sendMail(InternetAddress from, String subject, String body, String htmlBody, List<InternetAddress> addresses);
    
}

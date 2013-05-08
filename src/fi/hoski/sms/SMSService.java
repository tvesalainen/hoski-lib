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
package fi.hoski.sms;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.nio.charset.CharacterCodingException;
import java.util.List;

/**
 *
 * @author Timo Vesalainen
 */
public interface SMSService
{
    String status(String trackingId) throws IOException, SMSException;
    /**
     * Return number of sms messages left
     * @return
     * @throws UnsupportedEncodingException
     * @throws MalformedURLException
     * @throws IOException
     * @throws SMSException 
     */
    int messagesLeft() throws IOException, SMSException;
    /**
     * Return how many sms is needed to send the message
     * @param message
     * @return
     * @throws CharacterCodingException
     * @throws IllegalCharacterException 
     */
    int messageCount(String message) throws CharacterCodingException, IllegalCharacterException;
    /**
     * Return the number of characters needed for sending message
     * @param message
     * @return
     * @throws CharacterCodingException
     * @throws IllegalCharacterException 
     */
    int messageSize(String message) throws CharacterCodingException, IllegalCharacterException;
    SMSStatus send(String numberTo, String message) throws IOException, SMSException;
    /**
     * Sends a SMS message. Returns status.
     * @param numberFrom 
     * @param numberTo
     * @param message
     * @return
     * @throws IOException
     * @throws SMSException 
     */
    SMSStatus send(String numberFrom, String numberTo, String message) throws IOException, SMSException;
    List<SMSStatus> send(List<String> numberTo, String message) throws IOException, SMSException;
    /**
     * Sends a list of SMS messages. Returns status list.
     * @param numberFrom
     * @param numberTo
     * @param message
     * @return
     * @throws IOException
     * @throws SMSException 
     */
    List<SMSStatus> send(String numberFrom, List<String> numberTo, String message) throws IOException, SMSException;
    
}

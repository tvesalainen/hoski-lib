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

import fi.hoski.sms.SMSException;
import fi.hoski.sms.SMSStatus;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;

/**
 * @author Timo Vesalainen
 */
public class ZonerStatus implements SMSStatus
{
    // ZonerStatus codes

    /**
     * Message delivered to handset.
     */
    public static final int DELIVERED = 1;
    /**
     * Message has been queued on the SMSC, but no delivered yet.
     */
    public static final int BUFFERED = 2;
    /**
     * The message failed to deliver. The GSM error code may give more
     * information.
     */
    public static final int FAILED = 3;
    /**
     * Message expired, could not be delivered within the validity period.
     */
    public static final int EXPIRED = 5;
    /**
     * Message rejected by SMSC.
     */
    public static final int REJECTED = 6;
    /**
     * SMSC error, message could not be processed this time.
     */
    public static final int ERROR = 7;
    /**
     * Unknown status, usually generated after 24 hours if no status has been
     * returned from the SMSC.
     */
    public static final int UNKNOWN1 = 11;
    /**
     * Unknown status, SMSC returned a non standard status code.
     */
    public static final int UNKNOWN2 = 12;
    
    private ZonerSMSService svc;
    private String trackingId;
    private String status;
    private int statusCode;
    private int errorCode;
    private Date receiveDate;
    private InetAddress internetAddress;
    private String numberTo;
    private String numberFrom;
    private Date sendDate;
    private String message;

    public ZonerStatus(ZonerSMSService svc, String trackingId)
    {
        this.svc = svc;
        this.trackingId = trackingId;
    }

    private void retrieve()
    {
        try
        {
            if (status == null)
            {
                status = svc.status(trackingId);
                /*
                String[] ss = status.split(":");
                trackingId = ss[0];
                if (!ss[1].isEmpty())
                {
                    statusCode = Integer.parseInt(ss[1]);
                }
                if (!ss[2].isEmpty())
                {
                    errorCode = Integer.parseInt(ss[2]);
                }
                if (!ss[3].isEmpty())
                {
                    receiveDate = new Date(Long.parseLong(ss[3]) * 1000);
                }
                internetAddress = InetAddress.getByName(ss[4]);
                numberTo = ss[5];
                numberFrom = ss[6];
                if (!ss[7].isEmpty())
                {
                    sendDate = new Date(Long.parseLong(ss[7]) * 1000);
                }
                message = ss[8];
                * 
                */
            }
        }
        catch (IOException ex)
        {
            throw new IllegalArgumentException(ex);
        }
        catch (SMSException ex)
        {
            throw new IllegalArgumentException(ex);
        }
    }

    @Override
    public boolean delivered()
    {
        retrieve();
        return statusCode == DELIVERED;
    }

    @Override
    public boolean buffered()
    {
        retrieve();
        return statusCode == BUFFERED;
    }

    @Override
    public boolean failed()
    {
        return !delivered() && !buffered();
    }

    public int getErrorCode()
    {
        retrieve();
        return errorCode;
    }

    public InetAddress getInternetAddress()
    {
        retrieve();
        return internetAddress;
    }

    public String getMessage()
    {
        retrieve();
        return message;
    }

    public String getNumberFrom()
    {
        retrieve();
        return numberFrom;
    }

    public String getNumberTo()
    {
        retrieve();
        return numberTo;
    }

    public Date getReceiveDate()
    {
        retrieve();
        return receiveDate;
    }

    public Date getSendDate()
    {
        retrieve();
        return sendDate;
    }

    public String getStatus()
    {
        retrieve();
        return status;
    }

    public int getStatusCode()
    {
        retrieve();
        return statusCode;
    }

    public String getTrackingId()
    {
        retrieve();
        return trackingId;
    }

    @Override
    public String toString()
    {
        retrieve();
        return status;
        /*
        retrieve();
        return "Status="
                + "\ntrackingId=" + trackingId
                + "\nstatusCode=" + statusString(statusCode)
                + "\nerrorCode=" + errorString(errorCode)
                + "\nreceiveDate=" + receiveDate
                + "\ninternetAddress=" + internetAddress
                + "\nnumberTo=" + numberTo
                + "\nnumberFrom=" + numberFrom
                + "\nsendDate=" + sendDate
                + "\nmessage=" + message + "\n";
                * 
                */
    }

    public static String statusString(int statusCode)
    {
        switch (statusCode)
        {
            case 1:
                return "Message delivered to handset.";
            case 2:
                return "Message has been queued on the SMSC, but no delivered yet.";
            case 3:
                return "The message failed to deliver. The GSM error code may give more information.";
            case 5:
                return "Message expired, could not be delivered within the validity period.";
            case 6:
                return "Message rejected by SMSC.";
            case 7:
                return "SMSC error, message could not be processed this time.";
            case 11:
                return "Unknown status, usually generated after 24 hours if no status has been returned from the SMSC.";
            case 12:
                return "Unknown status, SMSC returned a non standard status code.";
            default:
                return "unknown status " + statusCode;
        }
    }

    public static String errorString(int errorCode)
    {
        switch (errorCode)
        {
            case 0x0000:
                return "ok";
            case 0x0001:
                return "unknownSubscriber";
            case 0x0002:
                return "unknownBaseStation";
            case 0x0003:
                return "unknownMSC";
            case 0x0005:
                return "unidentifiedSubscriber";
            case 0x0006:
                return "absentsubscriberSM";
            case 0x0007:
                return "unknownEquipment";
            case 0x0008:
                return "roamingNotAllowed";
            case 0x0009:
                return "illegalSubscriber";
            case 0x000a:
                return "bearerServiceNotProvisioned";
            case 0x000b:
                return "teleserviceNotProvisioned";
            case 0x000c:
                return "illegalEquipment";
            case 0x000d:
                return "callBarred";
            case 0x000e:
                return "forwardingViolation";
            case 0x000f:
                return "cug-Reject";
            case 0x0010:
                return "illegalSS-Operation";
            case 0x0011:
                return "ss-ErrorStatus";
            case 0x0012:
                return "ss-NotAvailable";
            case 0x0013:
                return "ss-SubscriptionViolation";
            case 0x0014:
                return "ss-Incompatibility";
            case 0x0015:
                return "facilityNotSupported";
            case 0x0017:
                return "invalidTargetBaseStation";
            case 0x0018:
                return "noRadioResourceAvailable";
            case 0x0019:
                return "noHandoverNumberAvailable";
            case 0x001a:
                return "subsequentHandoverFailure";
            case 0x001b:
                return "absentSubscriber";
            case 0x001c:
                return "incompatibleTerminal";
            case 0x001d:
                return "shortTermDenial";
            case 0x001e:
                return "longTermDenial";
            case 0x001f:
                return "subscriberBusyForMT-SMS";
            case 0x0020:
                return "sm-DeliveryFailure";
            case 0x0021:
                return "messageWaitingListFull";
            case 0x0022:
                return "systemFailure";
            case 0x0023:
                return "dataMissing";
            case 0x0024:
                return "unexpectedDataValue";
            case 0x0025:
                return "pw-RegistrationFailure";
            case 0x0026:
                return "negativePW-Check";
            case 0x0027:
                return "noRoamingNumberAvailable";
            case 0x0028:
                return "tracingBufferFull";
            case 0x002b:
                return "numberOfPW-AttemptsViolation";
            case 0x002c:
                return "numberChanged";
            case 0x002d:
                return "busySubscriber";
            case 0x002e:
                return "noSubscriberReply";
            case 0x002f:
                return "forwardingFailed";
            case 0x0030:
                return "or-NotAllowed";
            case 0x0031:
                return "ati-NotAllowed";
            case 0x0032:
                return "noGroupCallNumberAvailable";
            case 0x0033:
                return "resourceLimitation";
            case 0x0034:
                return "unauthorizedRequestingNetwork";
            case 0x0035:
                return "unauthorizedLCSClient";
            case 0x0036:
                return "positionMethodFailure";
            case 0x003a:
                return "unknownOrUnreachableLCSClient";
            case 0x0047:
                return "unknownAlphabet";
            case 0x0048:
                return "Ussd-Busy";
            case 0x0056:
                return "SubscriberLocationReport";
            case 0x2100:
                return "Unrecognized component";
            case 0x2101:
                return "Mistyped component";
            case 0x2102:
                return "Badly structured component";
            case 0x2200:
                return "Duplicate invoke ID";
            case 0x2201:
                return "Unrecognized operation";
            case 0x2202:
                return "Mistyped parameter";
            case 0x2203:
                return "Resource limitation";
            case 0x2204:
                return "Initiating release";
            case 0x2205:
                return "Unrecognized linked ID";
            case 0x2206:
                return "Linked response unexpected";
            case 0x2207:
                return "Unexpected linked operation";
            case 0x2300:
                return "Unrecognized invoke ID";
            case 0x2301:
                return "Return Result unexpected";
            case 0x2302:
                return "Mistyped parameter";
            case 0x2400:
                return "Unrecognized invoke ID";
            case 0x2401:
                return "Return Error unexpected";
            case 0x2402:
                return "Unrecognized error";
            case 0x2403:
                return "Unexpected error";
            case 0x2404:
                return "Mistyped parameter";
            case 0x6000:
                return "MemoryCapacityExceeded";
            case 0x6001:
                return "EquipmentProtocolError";
            case 0x6002:
                return "EquipmentNotSM-Equipped";
            case 0x6003:
                return "UnknownServiceCentre";
            case 0x6004:
                return "UnknownServiceCentre";
            case 0x6005:
                return "InvalidSME-Address";
            case 0x6006:
                return "subscriberNotSC-Subscriber";
            case 0x8000:
                return "Unrecognized message type";
            case 0x8001:
                return "Unrecognized transaction ID";
            case 0x8002:
                return "Badly formatted transaction portion";
            case 0x8003:
                return "Incorrect transaction portion";
            case 0x8004:
                return "Resource limitation";
            case 0x800b:
                return "Dialogue collision";
            case 0x8010:
                return "Node not reachable";
            case 0xC001:
                return "sc-AddressNotIncluded";
            case 0xC002:
                return "mnrf-Set";
            case 0xC004:
                return "Mcef-Set";
            case 0xC008:
                return "mnrg-Set";
            case 0xE001:
                return "The dialogue has received a MAP-DELIMITER unexpectedly.";
            case 0xE002:
                return "The dialogue has received a MAP-SERVICE-REQUEST unexpectedly.";
            case 0xE010:
                return "Could not decode an ASN.1 encoded parameter.";
            case 0xE011:
                return "Could not ASN.1 encode a parameter.";
            case 0xE020:
                return "Dialogue queue size exceeded.";
            case 0xE040:
                return "Dialogue timed out, i.e. the far side did not respond or there is a network problem.";
            case 0xE080:
                return "Invalid delivery outcome (< 0 or > 2 on any MAP level, or 1 on MAP 1)";
            case 0xE081:
                return "Invalid destination address";
            default:
                if (errorCode >= 0x8100 && errorCode <= 0x81ff)
                {
                    return "Incorrect message length";
                }
                if (errorCode >= 0x8200 && errorCode <= 0x82ff)
                {
                    return "Missing mandatory IE";
                }
                if (errorCode >= 0x8300 && errorCode <= 0x83ff)
                {
                    return "Incorrect IE length";
                }
                if (errorCode >= 0x8400 && errorCode <= 0x84ff)
                {
                    return "Bad parameter value";
                }
                if (errorCode >= 0x8600 && errorCode <= 0x86ff)
                {
                    return "Invalid dialogue ID";
                }
                if (errorCode >= 0x8700 && errorCode <= 0x87ff)
                {
                    return "Exceeded maximum length";
                }
                if (errorCode >= 0x8800 && errorCode <= 0x88ff)
                {
                    return "Invalid parameter";
                }
                if (errorCode >= 0x8900 && errorCode <= 0x89ff)
                {
                    return "Inappropriate transport message";
                }
                if (errorCode >= 0x8a00 && errorCode <= 0x8aff)
                {
                    return "Agent not registered";
                }
                if (errorCode >= 0x8b00 && errorCode <= 0x8bff)
                {
                    return "Dialogue collision";
                }
                if (errorCode >= 0x8c00 && errorCode <= 0x8cff)
                {
                    return "TC User not bound";
                }
                if (errorCode >= 0xE100 && errorCode <= 0xE1ff)
                {
                    return "Invalid destination addressCould not open MAP dialogue; the lowest 8 bits contain the code returned by TDAPI";
                }
                return "unknown code 0x" + Integer.toHexString(errorCode);
        }
    }
}

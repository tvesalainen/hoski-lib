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

import java.util.*;

public class BankingBarcode
{

    private char version = '5';
    private BankAccountNumber account;
    private double total;
    private int cents;
    private CreditorReference reference;
    private Calendar dueDate;
    private String bic;

    public BankingBarcode(
            BankAccountNumber account,
            double total,
            CreditorReference reference,
            Date dueDate,
            String bic)
    {
        this.account = account;
        this.total = total;
        this.cents = (int) Math.round(total * 100);
        this.reference = reference;
        if (dueDate != null)
        {
            this.dueDate = Calendar.getInstance();
            this.dueDate.setTime(dueDate);
        }
        this.bic = bic;
    }

    public BankingBarcode(
            char version,
            String account,
            double total,
            String reference,
            Date dueDate,
            String bic)
    {
        if (version != '4' && version != '5')
        {
            throw new IllegalArgumentException("version " + version + " is illegal");
        }
        this.version = version;
        this.account = new BankAccountNumber(account);
        this.total = total;
        this.cents = (int) Math.round(total * 100);
        this.reference = new CreditorReference(reference, false);
        if (dueDate != null)
        {
            this.dueDate = Calendar.getInstance();
            this.dueDate.setTime(dueDate);
        }
        this.bic = bic;
    }

    public double getTotal()
    {
        return total;
    }

    public Date getDueDate()
    {
        return dueDate.getTime();
    }

    public BankAccountNumber getAccount()
    {
        return account;
    }

    public CreditorReference getReference()
    {
        return reference;
    }

    public Map<String,Object> getMetaData()
    {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("tili", account.toString());
        map.put("iban", account.getIBAN());
        map.put("bic", bic);
        map.put("viite", reference.toString());
        map.put("rf", reference.toFormattedRFString());
        map.put("dueday", new Day(dueDate.getTime()).toString());
        map.put("total", String.format("%.2f", total));
        String barcode = toString4();
        map.put("barcode4", barcode);
        barcode = toString5();
        map.put("barcode5", barcode);
        return map;
    }
    
    public String toString()
    {
        switch (version)
        {
            case '2':
                return toString2();
            case '4':
                return toString4();
            case '5':
                return toString5();
            default:
                throw new IllegalArgumentException("wrong version " + version);
        }
    }

    public String toString2()
    {
        char ver = '2';
        int[] chk = new int[]
        {
            3, 7, 1
        };
        int cd = 0;
        int ii = 0;
        int jj = 0;
        String sum = null;
        char[] buf = new char[54];
        Arrays.fill(buf, '0');
        ii = 0;
        // version
        buf[ii++] = ver;
        // account
        ii = account.fill(ver, ii, buf);
        // total
        sum = String.valueOf(cents);
        sum.getChars(0, sum.length(), buf, ii + 8 - sum.length());
        ii += 8;
        // reference
        ii = reference.fill(ver, ii, buf);
        // due date
        if (dueDate != null)
        {
            sum = String.valueOf(dueDate.get(Calendar.YEAR));
            sum.getChars(2, sum.length(), buf, ii);
            ii += 2;
            sum = String.valueOf(dueDate.get(Calendar.MONTH) + 1);
            sum.getChars(0, sum.length(), buf, ii + 2 - sum.length());
            ii += 2;
            sum = String.valueOf(dueDate.get(Calendar.DATE));
            sum.getChars(0, sum.length(), buf, ii + 2 - sum.length());
            ii += 2;
        }
        else
        {
            ii += 6;
        }
        // filler
        ii += 4;
        // check 1
        for (jj = 0; jj < 53; jj++)
        {
            cd += (buf[jj] - '0') * chk[jj % 3];
        }
        buf[ii] = (char) ('0' + ((10 - (cd % 10)) % 10));
        return new String(buf);
    }

    public String toString4()
    {
        char ver = '4';
        int[] chk = new int[]
        {
            3, 7, 1
        };
        int cd = 0;
        int ii = 0;
        int jj = 0;
        String sum = null;
        char[] buf = new char[54];
        Arrays.fill(buf, '0');
        ii = 0;
        // version
        buf[ii++] = ver;
        // account
        ii = account.fill(ver, ii, buf);
        // total
        sum = String.valueOf(cents);
        sum.getChars(0, sum.length(), buf, ii + 8 - sum.length());
        ii += 8;
        // filler
        ii += 3;
        // reference
        ii = reference.fill(ver, ii, buf);
        // due date
        if (dueDate != null)
        {
            sum = String.valueOf(dueDate.get(Calendar.YEAR));
            sum.getChars(2, sum.length(), buf, ii);
            ii += 2;
            sum = String.valueOf(dueDate.get(Calendar.MONTH) + 1);
            sum.getChars(0, sum.length(), buf, ii + 2 - sum.length());
            ii += 2;
            sum = String.valueOf(dueDate.get(Calendar.DATE));
            sum.getChars(0, sum.length(), buf, ii + 2 - sum.length());
            ii += 2;
        }
        else
        {
            ii += 6;
        }
        return new String(buf);
    }

    public String toString5()
    {
        char ver = '5';
        int[] chk = new int[]
        {
            3, 7, 1
        };
        int cd = 0;
        int ii = 0;
        int jj = 0;
        String sum = null;
        char[] buf = new char[54];
        Arrays.fill(buf, '0');
        ii = 0;
        // version
        buf[ii++] = ver;
        // account
        ii = account.fill(ver, ii, buf);
        // total
        sum = String.valueOf(cents);
        sum.getChars(0, sum.length(), buf, ii + 8 - sum.length());
        ii += 8;
        // reference
        ii = reference.fill(ver, ii, buf);
        // due date
        if (dueDate != null)
        {
            sum = String.valueOf(dueDate.get(Calendar.YEAR));
            sum.getChars(2, sum.length(), buf, ii);
            ii += 2;
            sum = String.valueOf(dueDate.get(Calendar.MONTH) + 1);
            sum.getChars(0, sum.length(), buf, ii + 2 - sum.length());
            ii += 2;
            sum = String.valueOf(dueDate.get(Calendar.DATE));
            sum.getChars(0, sum.length(), buf, ii + 2 - sum.length());
            ii += 2;
        }
        else
        {
            ii += 6;
        }
        return new String(buf);
    }

    public static void main(String[] args)
    {
        try
        {
            Day day = new Day(2012, 6, 7);
            BankingBarcode pvk = new BankingBarcode(
                    '5',
                    "227918-22392",
                    120,
                    "1111",
                    day.getDate(),
                    "BIC");

            System.out.println(pvk.toString4());
        }
        catch (Exception e)
        {
            e.printStackTrace(System.err);
        }
    }
}

package com.shootwithsam.utils;

import java.util.StringTokenizer;

/**
 * Created by Noetic PC 6 on 8/4/2017.
 */

public class GetFormattedDate {

    private static String strNewDate = "";

    public static String getMySQLFormatDate(String strDate) {
        try {
            if (strDate.equalsIgnoreCase(""))
                strNewDate = "";
            else {
                StringTokenizer tokenizer = new StringTokenizer(strDate, "-");
                String day = tokenizer.nextToken();
                String month = tokenizer.nextToken();
                String year = tokenizer.nextToken();
                strNewDate = year + "-" + month + "-" + day;
            }
        } catch (Exception e) {
            e.printStackTrace();
            strNewDate = "";
        }
        return strNewDate;
    }

    public static String getNonMySQLFormatDate(String strDate) {
        try {
            if (strDate.equalsIgnoreCase(""))
                strNewDate = "";
            else {
                StringTokenizer tokenizer = new StringTokenizer(strDate, "-");
                String year = tokenizer.nextToken();
                String month = tokenizer.nextToken();
                String day = tokenizer.nextToken();
                strNewDate = day + "-" + month + "-" + year;
            }
        } catch (Exception e) {
            e.printStackTrace();
            strNewDate = "";
        }
        return strNewDate;
    }
}

package com.easyjava.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static final String YYY_MM_DD = "yyy_MM_dd";
    public static final String _YYYMMDD = "yyy/MM/dd";
    public static final String YYYMMDD = "yyyMMdd";

    public static String format(Date date,String patten){
       return new SimpleDateFormat(patten).format(date);
    }

    public static String parse(String date,String patten){
        try {
            new SimpleDateFormat(patten).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}

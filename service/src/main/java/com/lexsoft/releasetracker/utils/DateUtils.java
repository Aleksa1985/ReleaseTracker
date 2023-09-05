package com.lexsoft.releasetracker.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static final String DATE_FORMAT = "dd/MM/yyyy";

    private static final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

    public static Date stringToDate(String dateString) throws ParseException {
        return sdf.parse(dateString);
    }

    public static String dateToString(Date date) {
        return sdf.format(date);
    }
}

package com.lexsoft.releasetracker.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateUtils {

    private static final String DATE_FORMAT = "dd/MM/yyyy";

    private static final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

    public static Date stringToDate(String dateString)  {
        Date result = null;
        try {
            result = sdf.parse(dateString);
        } catch (ParseException e) {
            log.error("Unable to parse date: {}", dateString);
        }
        return result;
    }

    public static String dateToString(Date date) {
        return sdf.format(date);
    }
}

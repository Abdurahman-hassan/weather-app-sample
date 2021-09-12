package com.agreencode.proweatherapp.Common;

import android.location.Location;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {
    public static final String APP_ID ="2363b11d4517c35440df98375157ef86";
    public static Location CURRENT_LOCATION = null;

    public static String ConvertUnixToDate(long dt) {

        Date date = new Date(dt*1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a  EE dd/MM/yyyy");
        String formatted = simpleDateFormat.format(date);
        return formatted;
    }

    public static String ConvertUnixToDateWithoutDay(long dt) {

        Date date = new Date(dt*1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy");
        String formatted = simpleDateFormat.format(date);
        return formatted;
    }

    public static String ConvertUnixToDayAndHour(long dt) {

        Date date = new Date(dt*1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE || ha || ");
        String formatted = simpleDateFormat.format(date);
        return formatted;
    }
}
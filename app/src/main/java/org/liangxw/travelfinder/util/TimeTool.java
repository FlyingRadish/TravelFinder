package org.liangxw.travelfinder.util;

import java.util.Calendar;

public class TimeTool {

    public static String getBetterTime(long timeInMill) {
        long dt = System.currentTimeMillis() - timeInMill;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMill);
        dt = dt / 1000;

        if (dt < 60) {
            return dt + "秒前";
        } else if (dt < 3600) {
            return dt / 60 + "分钟前";
        } else if (dt < 60 * 60 * 24) {
            return "今天 " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
        } else {
            return (calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
        }
    }


    public static Calendar getCalendarFromInt(int yyyyMMdd) {
        int day = yyyyMMdd % 100;
        int month = (yyyyMMdd / 100) % 100;
        int year = yyyyMMdd / 10000;
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, 0, 0);
        return calendar;
    }

    public static Calendar getCalendarFromString(String yyyy_MM_dd) {
        int dateInt = DateString2Int(yyyy_MM_dd);
        return getCalendarFromInt(dateInt);
    }


    public static int DateString2Int(String date) {
        return Integer.parseInt(date.replace("-", ""));
    }

    public static String Int2DateString(int date) {
        int day = date % 100;
        int month = (date / 100) % 100;
        int year = date / 10000;
        String dateString = year + "-" + month + "-";
        if (day < 10) {
            dateString = dateString + "0";
        }
        dateString = dateString + day;
        return dateString;
    }

    public static String getyyyy_MM_dd_HHmmss(Calendar calendar) {

        return getyyyy_MM_dd(calendar) + " " + getHHmmss(calendar);
    }

    public static String getyyyy_MM_dd(Calendar calendar) {
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String dateString = calendar.get(Calendar.YEAR) + "-";
        if (month < 10) {
            dateString = dateString + "0";
        }
        dateString = dateString + month + "-";
        if (day < 10) {
            dateString = dateString + "0";
        }
        dateString = dateString + day;
        return dateString;
    }

    public static String getHHmmss(Calendar calendar) {
        return calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":"
                + calendar.get(Calendar.SECOND);
    }

    public static int getyyyyMMdd(Calendar calendar) {
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        return year * 10000 + month * 100 + day;
    }

    public static String getyyyy_MM_dd_HHmmss(long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        return getyyyy_MM_dd_HHmmss(calendar);
    }

    public static long getTimeInMills(String yyyy_MM_dd) {
        return getCalendarFromString(yyyy_MM_dd).getTimeInMillis();
    }

    public static long getTimeInMills(int yyyyMMdd) {
        return getCalendarFromInt(yyyyMMdd).getTimeInMillis();
    }
}

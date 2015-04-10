package org.liangxw.travelfinder.util.logger;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by houxg on 2015/1/3.
 */
public class FileLogger implements LogNode {

    private final static String LOG_DIR = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/HojyLog/LeaderTeam";

    private static void writeFile(String type, String tag, String content) {
        Calendar calendar = Calendar.getInstance();
        String logName = getyyyy_MM_dd(calendar) + "_log.txt";
        StringBuilder builder = new StringBuilder();
        builder.append(getyyyy_MM_dd_HHmmss(calendar))
                .append("  ")
                .append(type)
                .append("/")
                .append(tag)
                .append("：")
                .append(content)
                .append("\r\n");
        writeToFile(LOG_DIR, logName, builder.toString(), true);
    }


    @Override
    public void log(int priority, String tag, String content) {
        String priorityStr = Log.getPriorityStr(priority);
        writeFile(priorityStr, tag, content);
    }

    private static String getyyyy_MM_dd_HHmmss(Calendar calendar) {

        return getyyyy_MM_dd(calendar) + " " + getHHmmss(calendar);
    }

    private static String getHHmmss(Calendar calendar) {
        return calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":"
                + calendar.get(Calendar.SECOND);
    }

    private static String getyyyy_MM_dd(Calendar calendar) {
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

    private static boolean writeToFile(String path, String fileName, String content, boolean isAppend) {
        File dir = new File(path);
        File file = new File(path + "/" + fileName);
        if (!dir.isDirectory()) {
            if (!dir.mkdirs()) {
                return false;
            }
        }

        if (!file.isFile()) {
            try {
                if (!file.createNewFile()) {
                    return false;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                return false;
            }
        }

        try {
            FileWriter fileWriter = new FileWriter(file, isAppend);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

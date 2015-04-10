package org.liangxw.travelfinder.util.logger;


/**
 * Created by houxg on 2015/1/2.
 */
public class Log {

    public static final int VERBOSE = android.util.Log.VERBOSE;
    public static final int DEBUG = android.util.Log.DEBUG;
    public static final int INFO = android.util.Log.INFO;
    public static final int WARN = android.util.Log.WARN;
    public static final int ERROR = android.util.Log.ERROR;
    public static final int WTF = android.util.Log.ASSERT;

    private static final int LOGTYPE_NONE = 0x00;
    private static final int LOGTYPE_LOGCAT = 0x01;
    private static final int LOGTYPE_FILE = 0x02;
    private static final int LOGTYPE_VIEW = 0x04;

    private static int LOG_TYPE = LOGTYPE_LOGCAT | LOGTYPE_FILE | LOGTYPE_VIEW;
//    private static int LOG_TYPE = LOGTYPE_NONE;

    private static LogNode fileLogger = new FileLogger();

    private static LogNode viewLogger;

    public static void e(String tag, String content) {
        if (LOG_TYPE == LOGTYPE_NONE) {
            return;
        }
        if ((LOG_TYPE & LOGTYPE_LOGCAT) > 0) {
            android.util.Log.e(tag, content);
        }
        if ((LOG_TYPE & LOGTYPE_FILE) > 0) {
            fileLogger.log(ERROR, tag, content);
        }
        if ((LOG_TYPE & LOGTYPE_VIEW) > 0 && viewLogger != null) {
            viewLogger.log(ERROR, tag, content);
        }
    }

    public static void i(String tag, String content) {
        if (LOG_TYPE == LOGTYPE_NONE) {
            return;
        }
        if ((LOG_TYPE & LOGTYPE_LOGCAT) > 0) {
            android.util.Log.i(tag, content);
        }
        if ((LOG_TYPE & LOGTYPE_FILE) > 0) {
            fileLogger.log(INFO, tag, content);
        }
        if ((LOG_TYPE & LOGTYPE_VIEW) > 0 && viewLogger != null) {
            viewLogger.log(INFO, tag, content);
        }
    }

    public static void d(String tag, String content) {
        if (LOG_TYPE == LOGTYPE_NONE) {
            return;
        }
        if ((LOG_TYPE & LOGTYPE_LOGCAT) > 0) {
            android.util.Log.d(tag, content);
        }
        if ((LOG_TYPE & LOGTYPE_FILE) > 0) {
            fileLogger.log(DEBUG, tag, content);
        }
        if ((LOG_TYPE & LOGTYPE_VIEW) > 0 && viewLogger != null) {
            viewLogger.log(DEBUG, tag, content);
        }
    }

    public static void v(String tag, String content) {
        if (LOG_TYPE == LOGTYPE_NONE) {
            return;
        }
        if ((LOG_TYPE & LOGTYPE_LOGCAT) > 0) {
            android.util.Log.v(tag, content);
        }
        if ((LOG_TYPE & LOGTYPE_FILE) > 0) {
            fileLogger.log(VERBOSE, tag, content);
        }
        if ((LOG_TYPE & LOGTYPE_VIEW) > 0 && viewLogger != null) {
            viewLogger.log(VERBOSE, tag, content);
        }
    }

    public static void w(String tag, String content) {
        if (LOG_TYPE == LOGTYPE_NONE) {
            return;
        }
        if ((LOG_TYPE & LOGTYPE_LOGCAT) > 0) {
            android.util.Log.w(tag, content);
        }
        if ((LOG_TYPE & LOGTYPE_FILE) > 0) {
            fileLogger.log(WARN, tag, content);
        }
        if ((LOG_TYPE & LOGTYPE_VIEW) > 0 && viewLogger != null) {
            viewLogger.log(WARN, tag, content);
        }
    }

    public static void wtf(String tag, String content) {
        if (LOG_TYPE == LOGTYPE_NONE) {
            return;
        }
        if ((LOG_TYPE & LOGTYPE_LOGCAT) > 0) {
            android.util.Log.wtf(tag, content);
        }
        if ((LOG_TYPE & LOGTYPE_FILE) > 0) {
            fileLogger.log(WTF, tag, content);
        }
        if ((LOG_TYPE & LOGTYPE_VIEW) > 0 && viewLogger != null) {
            viewLogger.log(WTF, tag, content);
        }
    }

    public static void setViewLogger(LogNode view) {
        viewLogger = view;
    }

    public static String getPriorityStr(int priority) {
        String priorityStr = "";
        switch (priority) {
            case Log.VERBOSE:
                priorityStr = "V";
                break;
            case Log.DEBUG:
                priorityStr = "D";
                break;
            case Log.INFO:
                priorityStr = "I";
                break;
            case Log.WARN:
                priorityStr = "W";
                break;
            case Log.ERROR:
                priorityStr = "E";
                break;
            case Log.WTF:
                priorityStr = "WTF";
                break;
            default:
                break;
        }
        return priorityStr;
    }
}

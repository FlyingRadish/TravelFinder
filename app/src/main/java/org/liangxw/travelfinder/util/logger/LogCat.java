package org.liangxw.travelfinder.util.logger;

/**
 * Created by houxg on 2015/4/13.
 */
public class LogCat implements LogNode {
    @Override
    public void log(int priority, String tag, String content) {
        switch (priority) {
            case Log.DEBUG:
                android.util.Log.d(tag, content);
                break;
            case Log.VERBOSE:
                android.util.Log.v(tag, content);
                break;
            case Log.INFO:
                android.util.Log.i(tag, content);
                break;
            case Log.WARN:
                android.util.Log.w(tag, content);
                break;
            case Log.ERROR:
                android.util.Log.e(tag, content);
                break;
            case Log.WTF:
                android.util.Log.wtf(tag, content);
                break;
            default:
                android.util.Log.wtf(tag, "default?" + content);
                break;
        }
    }
}

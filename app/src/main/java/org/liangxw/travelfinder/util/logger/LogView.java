package org.liangxw.travelfinder.util.logger;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 * Created by houxg on 2015/1/3.
 */
public class LogView extends TextView implements LogNode{
    public LogView(Context context) {
        super(context);
    }

    public LogView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LogView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void log(int priority, String tag, String content) {
        String priorityStr = Log.getPriorityStr(priority);
        final StringBuilder outputBuilder = new StringBuilder();

        outputBuilder.append(priorityStr).append("\t")
                .append(tag).append("\t")
                .append(content).append("\n");

        // In case this was originally called from an AsyncTask or some other off-UI thread,
        // make sure the update occurs within the UI thread.
        ((Activity) getContext()).runOnUiThread((new Thread(new Runnable() {
            @Override
            public void run() {
                // Display the text we just generated within the LogView.
                append(outputBuilder.toString());
            }
        })));
    }
}

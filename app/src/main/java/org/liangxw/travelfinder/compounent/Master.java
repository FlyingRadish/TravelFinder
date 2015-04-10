package org.liangxw.travelfinder.compounent;

import android.app.Application;
import android.widget.Toast;


import org.liangxw.travelfinder.util.logger.Log;

import de.greenrobot.event.EventBus;

/**
 * Created by houxg on 2014/12/19.
 */
public class Master extends Application implements Thread.UncaughtExceptionHandler {

    private static EventBus eventBus;

    public Master()
    {
        eventBus = new EventBus();
    }

    public static EventBus getEventBus() {
        if (eventBus == null) {
            eventBus = new EventBus();
            Log.wtf("houxg", "EventBus re-create?");
        }
        return eventBus;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Toast.makeText(this,"异常错误！", Toast.LENGTH_SHORT).show();
    }
}

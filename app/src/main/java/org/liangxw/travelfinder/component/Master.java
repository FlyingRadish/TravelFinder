package org.liangxw.travelfinder.component;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;

import org.liangxw.travelfinder.model.Group;
import org.liangxw.travelfinder.model.UGMap;
import org.liangxw.travelfinder.util.logger.Log;

import de.greenrobot.event.EventBus;

/**
 * Created by houxg on 2014/12/19.
 */
public class Master extends Application implements Thread.UncaughtExceptionHandler {


    private static EventBus eventBus;
    private final static String TAG = Master.class.getSimpleName();
    private final static String LEANCLOUD_APPID = "ny2rwjcs4yh2ywer683c8fows809egmjwfi82p1rkmpi1kcd";
    private final static String LEANCLOUD_APPKEY = "esctbelmuzndb61hkh7ak250mchae8nvfcfug4p2km5urvyl";

    public Master() {
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
        AVObject.registerSubclass(Group.class);
        AVObject.registerSubclass(UGMap.class);
        AVOSCloud.initialize(this, LEANCLOUD_APPID, LEANCLOUD_APPKEY);
//        AVUser.alwaysUseSubUserClass(User.class);
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Log.i(TAG, "uncaughtException:" + ex.getMessage());
        ex.printStackTrace();
        Log.wtf(getPackageName(), Log.getStackTraceString(ex));
    }
}

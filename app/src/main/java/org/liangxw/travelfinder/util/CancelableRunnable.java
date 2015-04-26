package org.liangxw.travelfinder.util;

/**
 * Created by houxg on 2015/1/3.
 */
public abstract class CancelableRunnable implements Runnable {

    protected boolean isRun = true;


    public void cancel() {
        isRun = false;
    }

    public static void cancelIfNotNull(CancelableRunnable obj) {
        if (obj != null) {
            obj.cancel();
        }
    }
}

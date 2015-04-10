package org.liangxw.travelfinder.compounent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import org.liangxw.travelfinder.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by houxg on 2015/4/10.
 */
public class ActivityStack {
    private static ActivityStack instance;
    private List<Activity> stack;
    private Context context;

    public ActivityStack(Context ctx) {
        context = ctx.getApplicationContext();
        stack = new ArrayList<Activity>();
    }

    public static synchronized ActivityStack getInstance(Context context) {
        if (instance == null) {
            instance = new ActivityStack(context);
        }
        return instance;
    }

    public void add(Activity activity) {
        stack.add(activity);
    }

    public void remove(Activity activity) {
        stack.remove(activity);
    }

    public void removeWithRules(Activity activity, ActivityRule rule) {
        int index = stack.indexOf(activity);
        if (rule == null || index == -1) {
            remove(activity);
        } else {
            Activity preActivity = null;
            if (index >= 1) {
                preActivity = stack.get(index - 1);
            }
            if (rule.onPop(preActivity, activity)) {
                remove(activity);
            }
        }
    }

    public void removeAll() {
        for (int i = stack.size() - 1; i <= 0; i++) {
            stack.get(i).finish();
            stack.remove(i);
        }
    }

    public void restart() {
        for (Activity activity : stack) {
            activity.finish();
        }
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, MainActivity.class);
        context.startActivity(intent);
    }

    public interface ActivityRule {
        boolean onPop(Activity prev, Activity now);
    }
}

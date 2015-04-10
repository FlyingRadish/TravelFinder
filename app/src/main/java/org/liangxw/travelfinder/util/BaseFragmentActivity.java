package org.liangxw.travelfinder.util;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;


/**
 * Activity基础类
 * Created by houxg on 2014/12/14.
 */
public class BaseFragmentActivity extends FragmentActivity {

    public void startService(Class<?> cls)
    {
        Intent intent = new Intent(this, cls);
        startService(intent);
    }

    public void startActivity(Class<?> cls) {
        this.startActivity(cls, null, null);
    }

    public void startActivity(Class<?> cls, String action) {
        this.startActivity(cls, action, null);
    }

    public void startActivity(Class<?> cls, Bundle data) {
        this.startActivity(cls, null, data);
    }

    public void startActivity(Class<?> cls, String action, Bundle data) {
        Intent intent = new Intent(this, cls);
        if (action != null) {
            intent.setAction(action);
        }
        if (data != null) {
            intent.putExtras(data);
        }
        startActivity(intent);
    }

    public void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void toast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

}

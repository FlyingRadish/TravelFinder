package org.liangxw.travelfinder.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Des:
 * Created by houxg on 2015/3/19.
 */
public class ToastTool {

    public static void show(Context context, String text)
    {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void show(Context context, int resId)
    {
        Toast toast = Toast.makeText(context, context.getText(resId), Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void show(Context context, CharSequence text)
    {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }
}

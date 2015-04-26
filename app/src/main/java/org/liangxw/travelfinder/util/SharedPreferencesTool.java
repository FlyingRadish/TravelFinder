package org.liangxw.travelfinder.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Des:
 * Created by houxg on 2015/2/5.
 */
public class SharedPreferencesTool {

    Context context;
    String name;

    public SharedPreferencesTool(Context context, String name) {
        this.context = context;
        this.name = name;
    }

    public void write(String key, String val) {
        write(context, name, key, val);
    }

    public String read(Context context, String key, String deafault) {
        return read(context, name, key, deafault);
    }

    public static void write(Context context, String name, String key, String val) {
        SharedPreferences preferences = context.getSharedPreferences(
                name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, val);
        editor.commit();
    }

    public static void write(Context context, String name, String key, boolean val) {
        SharedPreferences preferences = context.getSharedPreferences(
                name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, val);
        editor.commit();
    }

    public static void write(Context context, String name, String key, long val) {
        SharedPreferences preferences = context.getSharedPreferences(
                name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, val);
        editor.commit();
    }

    public static void write(Context context, String name, String key, int val) {
        SharedPreferences preferences = context.getSharedPreferences(
                name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, val);
        editor.commit();
    }

    public static int read(Context context, String name, String key, int def) {
        SharedPreferences preferences = context.getSharedPreferences(
                name, Context.MODE_PRIVATE);
        return preferences.getInt(key, def);
    }

    public static String read(Context context, String name, String key, String def) {
        SharedPreferences preferences = context.getSharedPreferences(
                name, Context.MODE_PRIVATE);
        return preferences.getString(key, def);
    }

    public static boolean read(Context context, String name, String key, boolean def) {
        SharedPreferences preferences = context.getSharedPreferences(
                name, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, def);
    }

    public static long read(Context context, String name, String key, long def) {
        SharedPreferences preferences = context.getSharedPreferences(
                name, Context.MODE_PRIVATE);
        return preferences.getLong(key, def);
    }
}

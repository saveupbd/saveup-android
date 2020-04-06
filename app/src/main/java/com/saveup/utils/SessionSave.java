package com.saveup.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionSave {

    public static void saveSession(String key, String value, Context context) {
        if (context != null) {
            Editor editor = context.getSharedPreferences("KEY", Activity.MODE_PRIVATE).edit();
            editor.putString(key, value);
            editor.apply();
        }
    }

    public static String getSession(String key, Context context) {
        SharedPreferences prefs = context.getSharedPreferences("KEY", Activity.MODE_PRIVATE);
        return prefs.getString(key, "");
    }

    public static void saveSession(String key, boolean value, Context context) {
        Editor editor = context.getSharedPreferences("KEY", Activity.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getSession(String key, Context context, boolean b) {
        SharedPreferences prefs = context.getSharedPreferences("KEY", Activity.MODE_PRIVATE);
        return prefs.getBoolean(key, true);
    }
}

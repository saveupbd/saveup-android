package com.saveup.utils;

import android.app.Activity;

import java.util.ArrayList;

/**
 * @author AAPBD on 08,February,2017
 * @version 5.0
 * @Project TravelSwitch
 * @Package com.aapbd.utils
 */


public class StringUtil {

    public static boolean isEmpty(CharSequence str) {
        return (str == null || str.length() == 0);
    }
    public static ArrayList<Activity> mActivityList = new ArrayList<>();
    public static String FULL = "normal";
}

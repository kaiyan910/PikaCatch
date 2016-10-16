package com.crookk.pikaplus.core.utils;

import android.util.Log;

import com.crookk.pikaplus.BuildConfig;

public class LogUtils {

    public static void d(Object o, String message) {
        if (BuildConfig.DEBUG) {
            Log.d(o.getClass().getSimpleName(), message);
        }
    }

    public static void d(Object o, String message, Object... args) {
        if (BuildConfig.DEBUG) {
            Log.d(o.getClass().getSimpleName(), String.format(message, args));
        }
    }

    public static void e(Object o, Exception e) {
        if (BuildConfig.DEBUG) {
            Log.e(o.getClass().getSimpleName(), e.getMessage(), e);
        }
    }

}

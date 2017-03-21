package com.rc.darren.smartbutler.utils;

import android.util.Log;

/**
 * 项目名：    SmartButler
 * 包名：      com.rc.darren.smartbutler.utils
 * 文件名：    L
 * 创建者：    Darren
 * 创建时间：  2017/2/24 15:49
 * 描述：      封装LOG
 */

public class L {
    private static final boolean DEBUG = true;
    private static final String TAG = "Smartbutler";

    public static void d(String text) {
        if (DEBUG) {
            Log.d(TAG, text);
        }
    }

    public static void i(String text) {
        if (DEBUG) {
            Log.i(TAG, text);
        }
    }

    public static void w(String text) {
        if (DEBUG) {
            Log.w(TAG, text);
        }
    }

    public static void e(String text) {
        if (DEBUG) {
            Log.e(TAG, text);
        }
    }
}

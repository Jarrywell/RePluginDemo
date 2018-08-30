package com.android.test.host.demo;

import com.qihoo360.replugin.base.IPC;

import android.text.TextUtils;
import android.util.Log;

/**
 * des:
 * author: libingyan
 * Date: 18-8-30 15:54
 */
public class DLog {

    public final static String TAG = "Host-Demo";

    public static int d(String tag, String msg) {
        return Log.d(TAG, format(tag, msg));
    }

    public static int d(String tag, String msg, Throwable tr) {
        return Log.d(TAG, format(tag, msg), tr);
    }

    public static int i(String tag, String msg) {
        return Log.i(TAG, format(tag, msg));
    }

    public static int i(String tag, String msg, Throwable tr) {
        return Log.i(TAG, format(tag, msg), tr);
    }


    private static String format(String tag, String msg) {
        return "[" + tag + "][" + getCurrentProcessName() + "-" + Thread.currentThread().getId() + "]:" + msg;
    }

    private static String getCurrentProcessName() {
        final String name = IPC.getCurrentProcessName();
        if (!TextUtils.isEmpty(name)) {
            String[] values =  name.split(":");
            if (values.length > 1) {
                return values[1];
            }
        }
        return "main";
    }
}

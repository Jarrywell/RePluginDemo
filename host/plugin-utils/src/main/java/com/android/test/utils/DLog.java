package com.android.test.utils;

import android.support.annotation.Keep;
import android.text.TextUtils;
import android.util.Log;

import java.io.FileInputStream;

/**
 * des:
 * author: libingyan
 * Date: 18-8-30 15:54
 */

@Keep
public class DLog {

    public final static String TAG = "Host-Plugin";

    public static int d(String tag, String msg) {
        return Log.d(tag(), format(tag, msg));
    }

    public static int d(String tag, String msg, Throwable tr) {
        return Log.d(tag(), format(tag, msg), tr);
    }

    public static int i(String tag, String msg) {
        return Log.i(tag(), format(tag, msg));
    }

    public static int i(String tag, String msg, Throwable tr) {
        return Log.i(tag(), format(tag, msg), tr);
    }


    private static String format(String tag, String msg) {
        return "[" + tag + "][" + getCurrentProcessName() + "-" + Thread.currentThread().getId() + "]:" + msg;
    }

    private static String sTag = "";
    public static void setTag(String tag) {
        if (TextUtils.isEmpty(sTag)) {
            sTag = tag;
        }
    }

    private static String tag() {
        return !TextUtils.isEmpty(sTag) ? sTag : TAG;
    }

    private static String sCurrentProcess;
    private static String getCurrentProcessName() {
        if (sCurrentProcess == null) {
            sCurrentProcess = getCurrentProcessNameInner();
        }
        final String name = sCurrentProcess;
        //final String name = IPC.getCurrentProcessName();
        if (!TextUtils.isEmpty(name)) {
            String[] values =  name.split(":");
            if (values.length > 1) {
                return values[1];
            }
        }
        return "main";
    }


    /**
     * 返回当前的进程名
     *
     * @return
     */
    private static String getCurrentProcessNameInner() {
        FileInputStream in = null;
        try {
            String fn = "/proc/self/cmdline";
            in = new FileInputStream(fn);
            byte[] buffer = new byte[256];
            int len = 0;
            int b;
            while ((b = in.read()) > 0 && len < buffer.length) {
                buffer[len++] = (byte) b;
            }
            if (len > 0) {
                String s = new String(buffer, 0, len, "UTF-8");
                return s;
            }
        } catch (Throwable e) {
            return "@null";
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {

                }
            }
        }
        return "@null";
    }
}

package com.test.android.plugin2;

/**
 * des:
 * author: libingyan
 * Date: 18-9-6 17:45
 */
public class PluginString {

    static {
        System.loadLibrary("plugin-string");
    }


    public native static String getString(long time);
}

package com.test.android.plugin2;

import com.android.test.utils.DLog;
import com.qihoo360.replugin.RePlugin;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

/**
 * des:
 * author: libingyan
 * Date: 18-8-30 17:02
 */
public class Plugin2Application extends Application {

    private final static String TAG = "Plugin2Application";



    @Override
    public void onCreate() {
        super.onCreate();
        DLog.i(TAG, "onCreate()");

    }

    @Override
    protected void attachBaseContext(Context base) {
        /**
         * 设置日志打印自定可以tag
         */
        DLog.setTag("Plugin2");
        super.attachBaseContext(base);

        DLog.i(TAG, "attachBaseContext()");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        DLog.i(TAG, "onLowMemory()");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        DLog.i(TAG, "onTrimMemory()");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        DLog.i(TAG, "onConfigurationChanged()");
    }
}

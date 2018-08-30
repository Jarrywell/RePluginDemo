package com.test.android.plugin1;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

/**
 * des:
 * author: libingyan
 * Date: 18-8-30 17:02
 */
public class Plugin1Application extends Application {

    private final static String TAG = "Plugin1Application";

    @Override
    public void onCreate() {
        super.onCreate();

        DLog.i(TAG, "onCreate()");
    }

    @Override
    protected void attachBaseContext(Context base) {
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

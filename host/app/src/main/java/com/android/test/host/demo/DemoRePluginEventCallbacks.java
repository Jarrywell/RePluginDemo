package com.android.test.host.demo;

import com.android.test.utils.DLog;
import com.qihoo360.replugin.RePluginEventCallbacks;
import com.qihoo360.replugin.model.PluginInfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * des:
 * author: libingyan
 * Date: 18-8-30 16:27
 */
public class DemoRePluginEventCallbacks extends RePluginEventCallbacks {
    private static final String TAG = "DemoRePluginEventCallbacks";

    public DemoRePluginEventCallbacks(Context context) {
        super(context);
    }

    @Override
    public void onInstallPluginFailed(String path, InstallResult code) {
        super.onInstallPluginFailed(path, code);
        DLog.i(TAG, "onInstallPluginFailed() path: " + path);
    }

    @Override
    public void onInstallPluginSucceed(PluginInfo info) {
        super.onInstallPluginSucceed(info);
        DLog.i(TAG, "onInstallPluginSucceed() PluginExtra: " + info);
    }

    @Override
    public void onStartActivityCompleted(String plugin, String activity, boolean result) {
        super.onStartActivityCompleted(plugin, activity, result);
        DLog.i(TAG, "onStartActivityCompleted() plugin: " + plugin + ", activity: " + activity + ", result: " + result);
    }

    @Override
    public void onPrepareAllocPitActivity(Intent intent) {
        super.onPrepareAllocPitActivity(intent);
        DLog.i(TAG, "onPrepareAllocPitActivity() intent: " + intent);
    }

    @Override
    public void onPrepareStartPitActivity(Context context, Intent intent, Intent pittedIntent) {
        super.onPrepareStartPitActivity(context, intent, pittedIntent);
        DLog.i(TAG, "onPrepareStartPitActivity() intent: " + intent + ", pittedIntent: " + pittedIntent);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        super.onActivityDestroyed(activity);
        DLog.i(TAG, "onActivityDestroyed() activity: " + activity);
    }

    @Override
    public void onBinderReleased() {
        super.onBinderReleased();
        DLog.i(TAG, "onBinderReleased()");
    }
}

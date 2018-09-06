package com.test.android.plugin2;


import com.android.test.utils.DLog;
import com.qihoo360.replugin.RePlugin;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * des:
 * author: libingyan
 * Date: 18-9-5 17:40
 */
@Keep
public class Plugin2Service1 extends Service {

    private final String TAG = "Plugin2Service1";

    private final String PLUGIN2_BINDER_TEST_NAME = "plugin2-binder-test";

    @Override
    public void onCreate() {
        super.onCreate();

        DLog.i(TAG, "onCreate()");

        RePlugin.registerPluginBinder(PLUGIN2_BINDER_TEST_NAME, new Plugin2RequestImp());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DLog.i(TAG, "onStartCommand()");

        Toast.makeText(this, "Plugin2Service1.onStartCommand()", Toast.LENGTH_SHORT).show();

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        DLog.i(TAG, "onBind()");
        return null;
    }
}

package com.test.android.plugin1.service;


import com.android.test.utils.DLog;

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
public class Plugin1Service1 extends Service {

    private final String TAG = "Plugin1Service1";

    @Override
    public void onCreate() {
        super.onCreate();

        DLog.i(TAG, "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DLog.i(TAG, "onStartCommand()");

        Toast.makeText(this, "Plugin1Service1.onStartCommand()", Toast.LENGTH_SHORT).show();

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        DLog.i(TAG, "onBind()");
        return null;
    }
}

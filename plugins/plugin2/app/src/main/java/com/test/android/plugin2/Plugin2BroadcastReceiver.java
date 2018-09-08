package com.test.android.plugin2;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.test.utils.DLog;

/**
 * des:
 * author: libingyan
 * Date: 18-9-5 17:08
 */
public class Plugin2BroadcastReceiver extends BroadcastReceiver {

    private final String TAG = "Plugin2BroadcastReceiver";


    public static final String ACTION = "com.test.android.plugin2.receiver.ACTION1_TEST";

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (ACTION.equals(action)) {
            final String extra = intent.getStringExtra("extra");
            Toast.makeText(context, "receiver action: " + action + ", extra: " + extra, Toast.LENGTH_LONG).show();
            DLog.i(TAG, "onReceive() action: " + action + ", context: " + context + ", extra" + extra);

            Intent serviceIntent = new Intent(context, Plugin2Service1.class);
            context.startService(serviceIntent);
        }
    }
}

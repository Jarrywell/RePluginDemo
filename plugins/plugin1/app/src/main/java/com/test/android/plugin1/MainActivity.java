package com.test.android.plugin1;

import com.android.test.utils.DLog;
import com.android.test.utils.TimeUtils;
import com.test.android.plugin1.activity.InnerActivity;
import com.test.android.plugin1.activity.preference.PreferenceActivity1;
import com.test.android.plugin1.activity.single_instance.SingleInstanceActivity1;
import com.test.android.plugin1.activity.single_task.SingleTaskActivity1;
import com.test.android.plugin1.activity.single_top.SingleTopActivity1;
import com.test.android.plugin1.activity.standard.StandardActivity;
import com.test.android.plugin1.activity.notification.NotificationActivity;
import com.test.android.plugin1.activity.notification.NotificationUtils;
import com.test.android.plugin1.activity.task_affinity.TA1Activity1;
import com.test.android.plugin1.activity.theme.ThemeActivity1;
import com.test.android.plugin1.provider.FileProviderActivity;
import com.test.android.plugin1.receiver.Plugin1BroadcastReceiver;
import com.test.android.plugin1.service.Plugin1Service1;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.annotation.Keep;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import static com.test.android.plugin1.activity.notification.NotificationUtils.ACTION_NOTIFICATION_TEST;
import static com.test.android.plugin1.activity.notification.NotificationUtils.NOTIFICATION_EXTRA;

@Keep
public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DLog.i(TAG, "onCreate()");

        DLog.i(TAG, "plugin test plugin-utils class: " + TimeUtils.getNowString());

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_NOTIFICATION_TEST);
        registerReceiver(mNofificationReceiver, filter);

        findViewById(R.id.id_btn_start_inner_activity).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, InnerActivity.class));
                }
            });

        findViewById(R.id.id_btn_start_host_activity).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.android.test.host.demo", "com.android.test.host.demo.DemoFragmentActivity"));
                    startActivity(intent);
                }
            });

        findViewById(R.id.id_btn_start_standard_activity).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, StandardActivity.class));
                }
            });

        findViewById(R.id.id_btn_start_single_top_activity).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, SingleTopActivity1.class));
                }
            });

        findViewById(R.id.id_btn_start_single_instance_activity).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, SingleInstanceActivity1.class));
                }
            });

        findViewById(R.id.id_btn_start_single_task_activity).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, SingleTaskActivity1.class));
                }
            });

        findViewById(R.id.id_btn_start_intent_filter_activity).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent("android.intent.action.plugin1");
                    intent.addCategory("category_plugin1");
                    startActivity(intent);
                }
            });

        findViewById(R.id.id_btn_send_notification).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NotificationUtils.sendNotification(MainActivity.this,
                        "这里是标题", "这里是内容", "这里是通知栏携带的数据");
                }
            });

        findViewById(R.id.id_btn_start_preference_activity).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, PreferenceActivity1.class));
                }
            });

        findViewById(R.id.id_btn_start_task_affinity_activity).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, TA1Activity1.class));
                }
            });

        findViewById(R.id.id_btn_start_theme_activity).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, ThemeActivity1.class));
                }
            });

        findViewById(R.id.id_btn_start_file_provider_activity).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, FileProviderActivity.class));
                }
            });

        findViewById(R.id.id_btn_start_provider).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String authorities = "com.android.test.host.demo.plugin1.TEST_PROVIDER";

                    Uri uri = Uri.parse("content://" + authorities + "/" + "test");
                    ContentValues cv = new ContentValues();
                    cv.put("name", "plugin1 demo");
                    cv.put("address", "beijing");

                    final Uri result = getContentResolver().insert(uri, cv);
                    DLog.d(TAG, "provider insert result: " + result);
                    Toast.makeText(v.getContext(), (result != null ? result.toString() : ""), Toast.LENGTH_SHORT).show();
                }
            });

        findViewById(R.id.id_btn_start_plugin_receiver).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Plugin1BroadcastReceiver.ACTION);
                    intent.putExtra("extra", "this is extra test!!!");

                    sendBroadcast(intent);
                }
            });


        findViewById(R.id.id_btn_start_plugin_service).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent intent = new Intent(MainActivity.this, Plugin1Service1.class);
                    //startService(intent);

                    Intent intent = new Intent("com.test.android.plugin1.service.action.SERVICE1");
                    startService(intent);
                }
            });

    }

    @Override
    protected void onResume() {
        super.onResume();

        DLog.i(TAG, "onResume()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mNofificationReceiver);

        DLog.i(TAG, "onDestroy()");
    }

    private BroadcastReceiver mNofificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && !TextUtils.isEmpty(intent.getAction())) {
                DLog.i(TAG, "onReceive() action:" + intent.getAction());
                if (intent.getAction().equals(ACTION_NOTIFICATION_TEST)) {
                    final String extra = intent.getStringExtra(NOTIFICATION_EXTRA);
                    startNotificationActivity(context, extra);
                }
            }
        }
    };

    private void startNotificationActivity(Context context, String extra) {
        Toast.makeText(context, extra, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
        intent.putExtra(NOTIFICATION_EXTRA, extra);
        startActivity(intent);
    }
}

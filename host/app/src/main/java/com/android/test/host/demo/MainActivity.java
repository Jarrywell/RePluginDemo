package com.android.test.host.demo;

import com.android.test.utils.DLog;
import com.android.test.utils.TimeUtils;
import com.qihoo360.replugin.PluginDexClassLoader;
import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.component.provider.PluginProviderClient;
import com.qihoo360.replugin.component.service.PluginServiceClient;
import com.test.android.plugin2.IPlugin2Request;
import com.test.android.plugin2.PluginString;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DLog.i(TAG, "host test plugin-utils class: " + TimeUtils.getNowString());

        /**
         * 简单测试activity
         */
        findViewById(R.id.btn_plugin1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PluginManager.PluginExtra info = PluginManager.PLUGINS.get(PluginManager.PLUGIN1_NAME);
                RePlugin.startActivity(MainActivity.this, RePlugin.createIntent(info.pluginName, info.activitys[0]));
            }
        });

        /**
         * 测试启动多进程的插件activity
         */
        findViewById(R.id.btn_plugin1_inner_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PluginManager.PluginExtra info = PluginManager.PLUGINS.get(PluginManager.PLUGIN1_NAME);
                RePlugin.startActivity(MainActivity.this, RePlugin.createIntent(info.pluginName, info.activitys[1]));
            }
        });

        /**
         * 测试启动插件的startActivityForResult()
         */
        findViewById(R.id.btn_plugin1_result_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PluginManager.PluginExtra info = PluginManager.PLUGINS.get(PluginManager.PLUGIN1_NAME);
                RePlugin.startActivityForResult(MainActivity.this, RePlugin.createIntent(info.pluginName, info.activitys[2]), REQUEST_CODE);
            }
        });

        /**
         * 测试动态加载插件中的fragment
         */
        findViewById(R.id.btn_plugin1_fragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (RePlugin.isPluginInstalled(PluginManager.PLUGIN1_NAME)) {
                    startActivity(new Intent(MainActivity.this, DemoFragmentActivity.class));
                } else {
                    DLog.i(TAG, "plugin: " + PluginManager.PLUGIN1_NAME + ", don't installed!!");
                }
            }
        });

        /**
         * 测试通过action启动插件中的activity
         */
        findViewById(R.id.btn_plugin1_intent_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.plugin1");
                intent.addCategory("category_plugin1");
                PluginManager.PluginExtra info = PluginManager.PLUGINS.get(PluginManager.PLUGIN1_NAME);
                RePlugin.startActivity(MainActivity.this, intent, info.pluginName, null);

            }
        });

        /**
         * 测试插件中的file provider
         */
        findViewById(R.id.btn_plugin1_file_provider).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PluginManager.PluginExtra info = PluginManager.PLUGINS.get(PluginManager.PLUGIN1_NAME);
                RePlugin.startActivity(MainActivity.this, RePlugin.createIntent(info.pluginName, info.activitys[3]));
            }
        });

        /**
         * 测试插件中的provider
         */
        findViewById(R.id.id_btn_plugin1_provider).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String authorities = "com.android.test.host.demo.plugin1.TEST_PROVIDER";

                Uri uri = Uri.parse("content://" + authorities + "/" + "test");
                ContentValues cv = new ContentValues();
                cv.put("name", "plugin1 demo");
                cv.put("address", "beijing");

                /**
                 * 宿主操作插件中的provider时context必须要传插件中的context
                 */
                Context pluginContext = RePlugin.fetchContext(PluginManager.PLUGIN1_NAME);
                final Uri result = PluginProviderClient.insert(pluginContext, uri, cv);
                DLog.d(TAG, "provider insert result: " + result);

                Toast.makeText(v.getContext(), (result != null ? result.toString() : ""), Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * 测试插件中的广播
         */
        findViewById(R.id.id_btn_start_plugin_receiver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String ACTION = "com.test.android.plugin1.receiver.ACTION1_TEST";
                Intent intent = new Intent(ACTION);
                intent.putExtra("extra", "this is extra test!!!");

                sendBroadcast(intent);

            }
        });

        /**
         * 通过action启动插件中的service
         */
        findViewById(R.id.id_btn_start_plugin_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*PluginManager.PluginExtra info = PluginManager.PLUGINS.get(PluginManager.PLUGIN1_NAME);
                Intent intent = RePlugin.createIntent(info.pluginName, info.services[0]);
                PluginServiceClient.startService(MainActivity.this, intent);*/

                Intent intent = new Intent("com.test.android.plugin1.service.action.SERVICE1");
                Context pluginContext = RePlugin.fetchContext(PluginManager.PLUGIN1_NAME);
                PluginServiceClient.startService(pluginContext, intent);
            }
        });

        /**
         * 启动插件2中的activity
         */
        findViewById(R.id.id_btn_start_plugin2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.plugin2"); //plugin2->MainActivity
                RePlugin.startActivity(MainActivity.this, intent, PluginManager.PLUGIN2_NAME, null);
            }
        });

        /**
         * 测试插件中动态注册的binder
         */
        findViewById(R.id.id_btn_test_plugin2_binder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IBinder b = RePlugin.fetchBinder(PluginManager.PLUGIN2_NAME, "plugin2-binder-test");
                if (b == null) {
                    DLog.i(TAG, "fetchBinder() of plugin2-binder-test is null!!!");
                    return;
                }
                IPlugin2Request request = IPlugin2Request.Stub.asInterface(b);
                try {
                    boolean result = request.requestPluginName("宿主获取插件2中的binder");
                    DLog.i(TAG, "requestPluginName() result: " + result);
                } catch (Exception e) {
                    DLog.i(TAG, "requestPluginName() exception!!", e);
                }
            }
        });

        /**
         * 测试调用插件中so库中的函数
         */
        findViewById(R.id.id_btn_test_plugin2_so).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PluginDexClassLoader classLoader = (PluginDexClassLoader) RePlugin.fetchClassLoader(PluginManager.PLUGIN2_NAME);
                    final String path = classLoader.findLibrary("plugin-string");
                    DLog.i(TAG, "findLibrary result: " + path);

                    Class<?> cls = classLoader.loadClass("com.test.android.plugin2.PluginString");
                    Method method = cls.getDeclaredMethod("getString",long.class);
                    Object result = method.invoke(null, 0);
                    Toast.makeText(MainActivity.this, result.toString(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    DLog.i(TAG, "loadClass exception! ", e);
                }

                /*final String result = PluginString.getString(0);
                DLog.i(TAG, "test jni string result: " + result);
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();*/
            }
        });
    }

    private final int REQUEST_CODE = 0x0111;
    private final int RESULT_CODE = 0x0100;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE == requestCode && resultCode == RESULT_CODE) {
            Toast.makeText(this, data.getStringExtra("extra"), Toast.LENGTH_LONG).show();
        }
    }
}

package com.android.test.host.demo;

import com.qihoo360.replugin.RePlugin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        findViewById(R.id.btn_plugin1_intent_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.plugin1");
                intent.addCategory("category_plugin1");
                PluginManager.PluginExtra info = PluginManager.PLUGINS.get(PluginManager.PLUGIN1_NAME);
                RePlugin.startActivity(MainActivity.this, intent, info.pluginName, null);

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

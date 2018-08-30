package com.android.test.host.demo;

import com.qihoo360.replugin.RePlugin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_plugin1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PluginManager.PluginExtra info = PluginManager.PLUGINS.get(PluginManager.PLUGIN1_NAME);
                RePlugin.startActivity(MainActivity.this, RePlugin.createIntent(info.pluginName, info.activitys[0]));
            }
        });

        findViewById(R.id.btn_plugin1_inner_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PluginManager.PluginExtra info = PluginManager.PLUGINS.get(PluginManager.PLUGIN1_NAME);
                RePlugin.startActivity(MainActivity.this, RePlugin.createIntent(info.pluginName, info.activitys[1]));
            }
        });
    }
}

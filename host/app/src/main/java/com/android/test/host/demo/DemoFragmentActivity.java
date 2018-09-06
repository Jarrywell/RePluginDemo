package com.android.test.host.demo;

import com.android.test.utils.DLog;
import com.qihoo360.replugin.RePlugin;

import android.content.ComponentName;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * des: 问题Fragment cannot be cast to android.support.v4.app.Fragment 仍没有解决!!!
 * author: libingyan
 * Date: 18-8-30 20:46
 */
@Keep
public class DemoFragmentActivity extends FragmentActivity {

    private final String TAG = "DemoFragmentActivity";

    private final String PLUGIN_FRAGMENT_NAME = "com.test.android.plugin1.fragment.Plugin1Demo1Fragment";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ComponentName target = RePlugin.createComponentName(PluginManager.PLUGIN1_NAME, PLUGIN_FRAGMENT_NAME);
        RePlugin.registerHookingClass(PLUGIN_FRAGMENT_NAME, target, null);

        final boolean hooked = RePlugin.isHookingClass(target);

        DLog.i(TAG, "class: " + PLUGIN_FRAGMENT_NAME + ", hooked: " + hooked);


        setContentView(R.layout.activity_plugin_fragment);


        ClassLoader classLoader = RePlugin.fetchClassLoader(PluginManager.PLUGIN1_NAME);

        try {
            Class<?> cls = classLoader.loadClass(PLUGIN_FRAGMENT_NAME);
            DLog.i(TAG, "loaded class: " + cls);

            Fragment fragment = cls.asSubclass(Fragment.class).newInstance();
            DLog.i(TAG, "loaded fragment: " + fragment);
        } catch (Exception e) {
            DLog.i(TAG, "loaded fragment.", e);
            e.printStackTrace();
        }
    }
}

package com.android.test.host.demo;

import com.android.test.utils.DLog;
import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.RePluginApplication;
import com.qihoo360.replugin.RePluginCallbacks;
import com.qihoo360.replugin.RePluginConfig;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

/**
 * des:
 * author: libingyan
 * Date: 18-8-30 15:53
 */
public class DemoApplication extends RePluginApplication {

    private final static String TAG = "DemoApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        DLog.i(TAG, "onCreate()");

        //增加签名验证,暂时注释
        RePlugin.addCertSignature("A9B61A779F3F687A20B26C7A03B4449C");

        //是否启用调试器
        RePlugin.enableDebugger(this, BuildConfig.DEBUG);

        registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
    }

    @Override
    protected void attachBaseContext(Context base) {
        /**
         * 设置日志打印自定可以tag
         */
        DLog.setTag("Host");
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

    @Override
    protected RePluginConfig createConfig() {
        RePluginConfig config = new RePluginConfig();

        /**release包打开*/
        config.setVerifySign(!BuildConfig.DEBUG);

        /**当插件没有指定类时，是否允许使用宿主的类*/
        config.setUseHostClassIfNotFound(true);

        /** 设置宿主的 BuildID */
        config.setHostBuild("100001");

        /** 设置宿主的 VersionName */
        config.setHostVersionName(BuildConfig.VERSION_NAME);

        /**
         * 是否打印更详细的日志
         */
        config.setPrintDetailLog(BuildConfig.DEBUG);

        /**
         * 设置插件化框架的事件回调方法，调用者可自定义插件框架的事件回调行为
         */
        config.setEventCallbacks(new DemoRePluginEventCallbacks(this));

        // 在Art上，优化第一次loadDex的速度
        config.setOptimizeArtLoadDex(true);

        return config;
    }

    @Override
    protected RePluginCallbacks createCallbacks() {
        return new DemoRePluginCallbacks(this);
    }


    private ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            DLog.i(TAG, "onActivityCreated() activity: " + activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            DLog.i(TAG, "onActivityDestroyed() activity: " + activity);
        }
    };
}

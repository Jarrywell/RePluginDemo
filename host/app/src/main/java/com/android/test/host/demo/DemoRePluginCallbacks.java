package com.android.test.host.demo;

import com.android.test.utils.DLog;
import com.qihoo360.replugin.PluginDexClassLoader;
import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.RePluginCallbacks;
import com.qihoo360.replugin.RePluginClassLoader;
import com.qihoo360.replugin.model.PluginInfo;
import com.qihoo360.replugin.utils.Dex2OatUtils;
import com.qihoo360.replugin.utils.InterpretDex2OatHelper;
import com.qihoo360.replugin.utils.ReflectUtils;

import android.content.Context;
import android.content.Intent;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import dalvik.system.PathClassLoader;

/**
 * des:
 * author: libingyan
 * Date: 18-8-30 16:15
 */
public class DemoRePluginCallbacks extends RePluginCallbacks {
    private static final String TAG = "DemoRePluginCallbacks";

    private ClassLoader mOriginClassLoader;

    public DemoRePluginCallbacks(Context context) {
        super(context);
    }

    @Override
    public RePluginClassLoader createClassLoader(ClassLoader parent, ClassLoader original) {
        DLog.i(TAG, "createClassLoader() parent: " + parent.getClass().getName() + ", origin: " + original.getClass().getName());
        mOriginClassLoader = original;
        return super.createClassLoader(parent, original);
    }

    @Override
    public PluginDexClassLoader createPluginClassLoader(PluginInfo pi, String dexPath,
        String optimizedDirectory, String librarySearchPath, ClassLoader parent) {
        DLog.i(TAG, "createPluginClassLoader() PluginExtra: " + pi + ", dexPath: " + dexPath
            + ", optimizedDirectory: " + optimizedDirectory + ", librarySearchPath: "
            + librarySearchPath + ", parent: " + parent.getClass().getName());

        String odexName = pi.makeInstalledFileName() + ".dex";
        if (RePlugin.getConfig().isOptimizeArtLoadDex()) {
            Dex2OatUtils.injectLoadDex(dexPath, optimizedDirectory, odexName);
        }

        long being = System.currentTimeMillis();

        PluginDexClassLoader pluginDexClassLoader = super.createPluginClassLoader(pi, dexPath,
                optimizedDirectory, librarySearchPath, parent);

        if (BuildConfig.DEBUG) {
            DLog.d(Dex2OatUtils.TAG, "createPluginClassLoader use:" + (System.currentTimeMillis() - being));
            String odexAbsolutePath = (optimizedDirectory + File.separator + odexName);
            DLog.d(Dex2OatUtils.TAG, "createPluginClassLoader odexSize:" + InterpretDex2OatHelper.getOdexSize(odexAbsolutePath));
        }

        /**
         * hook classLoader的native加载目录（可以增加判断条件，金针对so插件做）
         */
        boolean hook = HookPathClassLoader.hookNativeLibraryPath(pluginDexClassLoader, mOriginClassLoader);
        DLog.i(TAG, "hookNativeLibraryPath() result: " + hook);


        hook = HookPathClassLoader.hookExtendDexPath(pluginDexClassLoader, mOriginClassLoader);
        DLog.i(TAG, "hookExtendDexPath() result: " + hook);


        return pluginDexClassLoader;
    }

    @Override
    public boolean onPluginNotExistsForActivity(Context context, String plugin, Intent intent, int process) {
        DLog.i(TAG, "onPluginNotExistsForActivity() plugin: " + plugin + ", process: " + process
            + ", context: " + context + ", intent: " + intent);
        PluginManager.PluginExtra info = PluginManager.PLUGINS.get(plugin);
        if (info != null) {
            final File apkFile = new File(info.apkPath);
            if (apkFile.exists()) {
                PluginInfo pluginInfo = RePlugin.install(info.apkPath);
                if (pluginInfo != null) {
                    /**
                     * 预加载插件(解压出apk文件),建议在子线程调用
                     */
                    final boolean success = RePlugin.preload(pluginInfo);

                    RePlugin.startActivity(context, intent);

                    return success;
                }
            }
        }
        return super.onPluginNotExistsForActivity(context, plugin, intent, process);
    }

    @Override
    public boolean onLoadLargePluginForActivity(Context context, String plugin, Intent intent, int process) {
        DLog.i(TAG, "onLoadLargePluginForActivity() plugin: " + plugin + ", process: " + process
            + ", context: " + context + ", intent: " + intent);
        return super.onLoadLargePluginForActivity(context, plugin, intent, process);
    }


    @Override
    public boolean isPluginBlocked(PluginInfo pluginInfo) {
        //DLog.i(TAG, "isPluginBlocked() pluginInfo: " + pluginInfo);
        return super.isPluginBlocked(pluginInfo);
    }

}

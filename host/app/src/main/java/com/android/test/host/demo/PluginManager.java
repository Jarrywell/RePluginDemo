package com.android.test.host.demo;

import com.android.test.utils.annotations.Beta;

import java.util.HashMap;
import java.util.Map;

/**
 * des:
 * author: libingyan
 * Date: 18-8-30 17:18
 */
@Beta
public class PluginManager {


    private static final String BASE_PATH = "/sdcard/plugins-mz/";

    public static final String PLUGIN1_NAME = "plugin1";

    public static final String PLUGIN2_NAME = "plugin2";

    public static final Map<String, PluginExtra> PLUGINS = new HashMap<>();

    static {
        PLUGINS.put(PLUGIN1_NAME, new PluginExtra(PLUGIN1_NAME, "plugin1.apk", new String[] {
            "com.test.android.plugin1.MainActivity",
            "com.test.android.plugin1.activity.InnerActivity",
            "com.test.android.plugin1.activity.ForResultActivity",
            "com.test.android.plugin1.provider.FileProviderActivity",
        }, new String[] {"com.test.android.plugin1.service.Plugin1Service1"}));

        PLUGINS.put(PLUGIN2_NAME, new PluginExtra(PLUGIN2_NAME, "plugin2.apk",
            new String[] {}, new String[] {}));

    }


    public static class PluginExtra {
        public String pluginName;
        public String apkPath;
        public String[] activitys;
        public String[] services;

        public PluginExtra(String pluginName, String apkName, String[] activitys, String[] services) {
            this.pluginName = pluginName;
            this.apkPath = BASE_PATH + apkName;
            this.activitys = activitys;
            this.services = services;
        }

    }

}

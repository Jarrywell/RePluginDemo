package com.android.test.host.demo;

import java.util.HashMap;
import java.util.Map;

/**
 * des:
 * author: libingyan
 * Date: 18-8-30 17:18
 */
public class PluginManager {


    private static final String BASE_PATH = "/sdcard/plugins-mz/";

    public static final String PLUGIN1_NAME = "plugin1";

    public static final Map<String, PluginExtra> PLUGINS = new HashMap<>();

    static {
        PLUGINS.put(PLUGIN1_NAME, new PluginExtra(PLUGIN1_NAME, "plugin1.apk", new String[] {
            "com.test.android.plugin1.MainActivity",
            "com.test.android.plugin1.InnerActivity",
            "com.test.android.plugin1.ForResultActivity",
        }, new String[] {""}));

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

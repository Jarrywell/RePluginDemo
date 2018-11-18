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

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

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

                /**
                 * 调用方式一:反射调用插件中so对应的java方法,成功
                 */
                /*try {
                    PluginDexClassLoader classLoader = (PluginDexClassLoader) RePlugin.fetchClassLoader(PluginManager.PLUGIN2_NAME);
                    final String path = classLoader.findLibrary("plugin-string");
                    DLog.i(TAG, "findLibrary result: " + path);

                    Class<?> cls = classLoader.loadClass("com.test.android.plugin2.PluginString");
                    Method method = cls.getDeclaredMethod("getString",long.class);
                    Object result = method.invoke(null, 0);
                    Toast.makeText(MainActivity.this, result.toString(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    DLog.i(TAG, "loadClass exception! ", e);
                }*/

                /**
                 * 测试lib库名字的拼接
                 */
                /*String filename = System.mapLibraryName("plugin-string");
                DLog.i(TAG, "mapLibraryName filename: " + filename);*/

                /**
                 * 测试二:
                 * 模拟System.loadLibrary("plugin-string"),
                 * public static void loadLibrary(String libname) {
                 *      Runtime.getRuntime().loadLibrary0(VMStack.getCallingClassLoader(), libname);
                 * }
                 *
                 * dalvik.system.VMStack.getCallingClassLoader(),返回的ClassLoader不是RePluginClassLoader,而是系统的PathClassLoader!!!!
                 *
                 */
                /*try {
                    Class<?> vmStackClass = Class.forName("dalvik.system.VMStack");
                    Method vmMethod = vmStackClass.getDeclaredMethod("getCallingClassLoader");
                    vmMethod.setAccessible(true);
                    final Object vmResult = vmMethod.invoke(null);
                    DLog.i(TAG, "vmResult: " + vmResult);
                } catch (Exception e) {
                    DLog.i(TAG, "VMStack exception! ", e);
                }
                DLog.i(TAG, "getClassLoader(): " + getClassLoader());*/

                /**
                 *
                 * 测试三: 失败
                 *
                 * 模拟Runtime.getRuntime().loadLibrary0(classLoader, libname);但是将classLoader替换成插件的classLoader
                 *
                 * so库Loader成功,但是调用PluginString.getString()抛出如下异常:
                 *
                 * 09-06 20:05:44.399 E/AndroidRuntime(28414): java.lang.UnsatisfiedLinkError:
                 * No implementation found for java.lang.String com.test.android.plugin2.PluginString.getString(long)
                 * (tried Java_com_test_android_plugin2_PluginString_getString and Java_com_test_android_plugin2_PluginString_getString__J)
                 *
                 */
                /*Runtime runtime = Runtime.getRuntime();
                try {
                    Method runntimeMethod = Runtime.class.getDeclaredMethod("loadLibrary0", ClassLoader.class, String.class);
                    runntimeMethod.setAccessible(true);
                    PluginDexClassLoader classLoader = (PluginDexClassLoader) RePlugin.fetchClassLoader(PluginManager.PLUGIN2_NAME);
                    runntimeMethod.invoke(runtime, classLoader, "plugin-string");
                } catch (InvocationTargetException e) {
                    DLog.i(TAG, "Runtime exception! ", e.getTargetException());
                }catch (Exception e) {
                    DLog.i(TAG, "Runtime exception! ", e);
                }
                final String result = PluginString.getString(0);
                DLog.i(TAG, "test jni string result: " + result);
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();*/

                /**
                 * 测试四: 失败
                 * 直接将PluginString类从插件中拷贝过来,调System.loadLibrary("plugin-string")后,抛如下异常:
                 * java.lang.UnsatisfiedLinkError: dalvik.system.PathClassLoader
                 * [DexPathList[[zip file "/data/app/com.android.test.host.demo-1/base.apk"],
                 * nativeLibraryDirectories=[/data/app/com.android.test.host.demo-1/lib/arm64, /system/lib64, /vendor/lib64]]]
                 * couldn't find "libplugin-string.so
                 *
                 * classLoader仍然是原始的PathClassLoader导致加载不到so文件
                 */
                /*final String normal = PluginString.getString(0);
                DLog.i(TAG, "test jni string result: " + normal);
                Toast.makeText(MainActivity.this, normal, Toast.LENGTH_LONG).show();*/

                /**
                 * 测试五： 成功！！！
                 * hook住插件的native加载目录
                 */
                boolean running = RePlugin.isPluginRunning(PluginManager.PLUGIN2_NAME);
                if (!running) {
                    if (!RePlugin.isPluginInstalled(PluginManager.PLUGIN2_NAME)) {
                        RePlugin.install(PluginManager.PLUGINS.get(PluginManager.PLUGIN2_NAME).apkPath);
                    }
                    RePlugin.fetchClassLoader(PluginManager.PLUGIN2_NAME);
                }
                DLog.i(TAG, PluginManager.PLUGIN2_NAME + " running: " + running);

                final String normal = PluginString.getString(0);
                DLog.i(TAG, "test jni string result: " + normal);
                Toast.makeText(MainActivity.this, normal, Toast.LENGTH_LONG).show();
            }
        });

        /**
         * 测试插件升级
         */
        findViewById(R.id.id_btn_test_update_plugins).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<String> plugins = PluginManager.PLUGINS.keySet();
                for (String plugin : plugins) {
                    PluginManager.PluginExtra extra = PluginManager.PLUGINS.get(plugin);
                    if (extra != null) {
                        File apkFile = new File(extra.apkPath);
                        if (apkFile.exists() && apkFile.length() > 0) {
                            RePlugin.install(apkFile.getAbsolutePath());
                        }
                    }
                }
            }
        });

        findViewById(R.id.id_btn_test_resource).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DemoResourceActivity.class));
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

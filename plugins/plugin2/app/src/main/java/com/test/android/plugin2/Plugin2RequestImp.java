package com.test.android.plugin2;


import com.qihoo360.replugin.RePlugin;

import android.os.RemoteException;
import android.widget.Toast;

/**
 * des:
 * author: libingyan
 * Date: 18-9-6 16:10
 */
public class Plugin2RequestImp extends IPlugin2Request.Stub {

    @Override
    public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble,
        String aString) throws RemoteException {

    }

    @Override
    public boolean requestPluginName(String extra) throws RemoteException {

        final String name = "plugin2 -> version: " + RePlugin.getVersion() + ", sdkVersion" + RePlugin.getSDKVersion();

        Toast.makeText(RePlugin.getPluginContext(), name, Toast.LENGTH_SHORT).show();

        return true;
    }
}

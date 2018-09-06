// IPlugin2Request.aidl
package com.test.android.plugin2;

// Declare any non-default types here with import statements

interface IPlugin2Request {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    /**
     * 测试fetchBinder接口应用
     *
     */
    boolean requestPluginName(String extra);
}

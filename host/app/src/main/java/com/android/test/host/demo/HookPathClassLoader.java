package com.android.test.host.demo;

import android.os.Build;

import com.android.test.utils.DLog;
import com.qihoo360.replugin.PluginDexClassLoader;
import com.qihoo360.replugin.utils.ReflectUtils;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;


public class HookPathClassLoader {

    private static final String TAG = "HookPathClassLoader";

    /**
     * 用于 hook native library目录
     * @param pluginClassLoader
     * @param originClassLoader
     * @return
     */
    public static boolean hookNativeLibraryPath(PluginDexClassLoader pluginClassLoader, ClassLoader originClassLoader) {


        /**
         * 仅仅针对5.0以上版本适配
         */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return false;
        }


        List<Object[]> allElements = new LinkedList<>();
        List<Object> allFiles = new LinkedList<>();
        try {

            /**
             * 获取宿主中的nativeLibraryPathElements
             */
            Object originPathList = ReflectUtils.readField(originClassLoader.getClass(), originClassLoader, "pathList");
            Object[] originElements = (Object[]) ReflectUtils.readField(originPathList.getClass(),
                    originPathList, "nativeLibraryPathElements");
            for (Object obj : originElements) {
                DLog.i(TAG, "origin native library element: " + obj);
            }

            /**
             * 获取插件中的nativeLibraryPathElements
             */
            Object pluginPathList = ReflectUtils.readField(pluginClassLoader.getClass(), pluginClassLoader, "pathList");
            Object[] pluginNativeElements = (Object[]) ReflectUtils.readField(pluginPathList.getClass(),
                    pluginPathList, "nativeLibraryPathElements");
            for (Object obj : pluginNativeElements) {
                DLog.i(TAG, "plugin native library element: " + obj);
            }

            /**
             * 合并两个element
             */
            allElements.add(pluginNativeElements); //将插件目录放在前，在findLibrary时就会先查找插件目录
            allElements.add(originElements); //宿主放在后
            Object combineElements = combineArray(allElements);

            /**
             * 写回parent中
             */
            ReflectUtils.writeField(originPathList.getClass(), originPathList, "nativeLibraryPathElements", combineElements);


            /**
             * 获取宿主中的nativeLibraryDirectories
             */
            List<Object> originNativeFiles = (List<Object>) ReflectUtils.readField(originPathList.getClass(),
                    originPathList, "nativeLibraryDirectories");
            for (Object obj : originNativeFiles) {
                DLog.i(TAG, "origin native library file: " + obj);
            }


            /**
             * 获取插件中的nativeLibraryDirectories
             */
            List<Object> pluginNativeFiles = (List<Object>) ReflectUtils.readField(pluginPathList.getClass(),
                    pluginPathList, "nativeLibraryDirectories");
            for (Object obj : pluginNativeFiles) {
                DLog.i(TAG, "plugin native library file: " + obj);
            }

            /**
             * 合并两native files
             */
            allFiles.addAll(pluginNativeFiles); //插件在前
            allFiles.addAll(originNativeFiles); //宿主在后


            /**
             * 写回nativeLibraryDirectories
             */
            ReflectUtils.writeField(originPathList.getClass(), originPathList, "nativeLibraryDirectories", allFiles);

            /**
             * 验证写回是否正常
             */
            Object[] outElements = (Object[]) ReflectUtils.readField(originPathList.getClass(),
                    originPathList, "nativeLibraryPathElements");
            for (Object obj : outElements) {
                DLog.i(TAG, "out parent native library element: " + obj);
            }


            /**
             * 验证写回是否正常
             */
            List<Object> outNativeFiles = (List<Object>) ReflectUtils.readField(originPathList.getClass(), originPathList, "nativeLibraryDirectories");
            for (Object obj : outNativeFiles) {
                DLog.i(TAG, "out parent native library file: " + obj);
            }
        } catch (Exception e) {
            DLog.i(TAG, "hookNativeLibraryPath() exception!!!", e);
        }

        return true;
    }

    private static Object combineArray(List<Object[]> allElements) {

        int startIndex = 0;
        int arrayLength = 0;
        Object[] originalElements = null;

        for (Object[] elements : allElements) {

            if (originalElements == null) {
                originalElements = elements;
            }

            arrayLength += elements.length;
        }

        Object[] combined = (Object[]) Array.newInstance(
                originalElements.getClass().getComponentType(), arrayLength);

        for (Object[] elements : allElements) {

            System.arraycopy(elements, 0, combined, startIndex, elements.length);
            startIndex += elements.length;
        }

        return combined;
    }
}

package com.android.test.host.demo;

import android.os.Build;

import com.android.test.utils.DLog;
import com.qihoo360.replugin.PluginDexClassLoader;
import com.qihoo360.replugin.utils.ReflectUtils;

import java.io.File;
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

        Object originPathList = null;
        Object pluginPathList = null;

        try {
            originPathList = ReflectUtils.readField(originClassLoader.getClass(), originClassLoader, "pathList");
            pluginPathList = ReflectUtils.readField(pluginClassLoader.getClass(), pluginClassLoader, "pathList");
        } catch (Exception e) {
            DLog.i(TAG, "hookNativeLibraryPath() exception!!!", e);
        }

        /**
         * 获取不到pathList
         */
        if (originPathList == null || pluginPathList == null) {

            DLog.i(TAG, "read pathList is null!!!");

            return false;
        }


        List<Object[]> allElements = new LinkedList<>();
        try {
            /**
             * 获取宿主中的nativeLibraryPathElements
             */
            Object[] originElements = (Object[]) ReflectUtils.readField(originPathList.getClass(),
                    originPathList, "nativeLibraryPathElements");
            for (Object obj : originElements) {
                DLog.i(TAG, "origin native library element: " + obj);
            }

            /**
             * 获取插件中的nativeLibraryPathElements
             */
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
             * 验证写回是否正常
             */
            Object[] outElements = (Object[]) ReflectUtils.readField(originPathList.getClass(),
                originPathList, "nativeLibraryPathElements");
            for (Object obj : outElements) {
                DLog.i(TAG, "out parent native library element: " + obj);
            }
        } catch (Exception e) {
            /**
             * android 5.x系统不存在nativeLibraryPathElements
             */
            DLog.i(TAG, "nativeLibraryPathElements() exception!!!", e);
        }

        /**
         * nativeLibraryDirectories要么是一个List<File>,要么是一个File[]
         */
        try {

            /**
             * 获取宿主中的nativeLibraryDirectories
             */
            Object originValue = ReflectUtils.readField(originPathList.getClass(), originPathList, "nativeLibraryDirectories");

            /**
             * 获取插件中的nativeLibraryDirectories
             */
            Object pluginValue = ReflectUtils.readField(pluginPathList.getClass(), pluginPathList, "nativeLibraryDirectories");

            if (originValue == null || pluginValue == null) {

                DLog.i(TAG, "read nativeLibraryDirectories is null!!!");

                return false;
            }

            /**
             * 合并
             */
            Object result = combineNativeLibraryDirectories(originValue, pluginValue);
            if (result != null) {
                /**
                 * 写回nativeLibraryDirectories
                 */
                ReflectUtils.writeField(originPathList.getClass(), originPathList, "nativeLibraryDirectories", result);
            } else {

                DLog.i(TAG, "combineNativeLibraryDirectories() is null!!!");

                return false;
            }

            /**
             * 验证写回是否正常
             */
            Object resultNativeFiles = ReflectUtils.readField(originPathList.getClass(), originPathList, "nativeLibraryDirectories");
            if (resultNativeFiles instanceof List) {
                for (Object obj : (List<Object>)resultNativeFiles) {
                    DLog.i(TAG, "out parent native library file: " + obj);
                }
            } else if (resultNativeFiles instanceof Object[]) {
                for (Object obj : (Object[])resultNativeFiles) {
                    DLog.i(TAG, "out parent native library file: " + obj);
                }
            }
        } catch (Exception e) {
            DLog.i(TAG, "nativeLibraryDirectories() exception!!!", e);
        }
        return true;
    }

    /**
     * 根据差异合并两个nativeLibraryDirectories
     * @param originValue
     * @param pluginValue
     * @return
     */
    private static Object combineNativeLibraryDirectories(Object originValue, Object pluginValue) {
        if (originValue instanceof List && pluginValue instanceof List) {
            List<Object> allFiles = new LinkedList<>();

            List<Object> originNativeFiles = (List<Object>) originValue;
            for (Object obj : originNativeFiles) {
                DLog.i(TAG, "origin native library file: " + obj);
            }

            List<Object> pluginNativeFiles =(List<Object>) pluginValue;
            for (Object obj : pluginNativeFiles) {
                DLog.i(TAG, "plugin native library file: " + obj);
            }

            /**
             * 合并两native files
             */
            allFiles.addAll(pluginNativeFiles); //插件在前
            allFiles.addAll(originNativeFiles); //宿主在后

            return allFiles;

        } else if (originValue instanceof Object[] && pluginValue instanceof Object[]) {
            List<Object[]> allFiles = new LinkedList<>();

            Object[] originNativeFiles = (Object[]) originValue;
            for (Object obj : originNativeFiles) {
                DLog.i(TAG, "origin native library file: " + obj);
            }

            Object[] pluginNativeFiles = (Object[]) pluginValue;
            for (Object obj : pluginNativeFiles) {
                DLog.i(TAG, "plugin native library file: " + obj);
            }

            /**
             * 合并两个element
             */
            allFiles.add(pluginNativeFiles); //将插件目录放在前，在findLibrary时就会先查找插件目录
            allFiles.add(originNativeFiles); //宿主放在后

            return combineArray(allFiles);

        } else {
            return null;
        }
    }

    public static boolean hookExtendDexPath(PluginDexClassLoader pluginClassLoader, ClassLoader originClassLoader) {
        /**
         * 仅仅针对5.0以上版本适配
         */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return false;
        }


        List<Object[]> allElements = new LinkedList<>();
        try {

            /**
             * 获取宿主中的dexElements
             */
            Object originPathList = ReflectUtils.readField(originClassLoader.getClass(), originClassLoader, "pathList");
            Object[] originElements = (Object[]) ReflectUtils.readField(originPathList.getClass(),
                originPathList, "dexElements");
            for (Object obj : originElements) {
                DLog.i(TAG, "origin dex element: " + obj);
            }

            /**
             * 获取插件中的dexElements
             */
            Object pluginPathList = ReflectUtils.readField(pluginClassLoader.getClass(), pluginClassLoader, "pathList");
            Object[] pluginElements = (Object[]) ReflectUtils.readField(pluginPathList.getClass(),
                pluginPathList, "dexElements");
            for (Object obj : pluginElements) {
                DLog.i(TAG, "plugin dex element: " + obj);
            }

            /**
             * 合并两个element
             */
            allElements.add(originElements); //宿主放在后
            allElements.add(pluginElements); //将插件目录放在前，在findLibrary时就会先查找插件目录
            Object combineElements = combineArray(allElements);

            /**
             * 写回parent中
             */
            ReflectUtils.writeField(originPathList.getClass(), originPathList, "dexElements", combineElements);

        } catch (Exception e) {
            DLog.i(TAG, "hookExtendDexPath() exception!!!", e);
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

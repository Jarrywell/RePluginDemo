#include <jni.h>

JNIEXPORT jstring JNICALL
Java_com_test_android_plugin2_PluginString_getString(JNIEnv *env, jclass type, jlong time) {

    // TODO
    return (*env)->NewStringUTF(env, "这里是so文件中获取的文本!!!");
}
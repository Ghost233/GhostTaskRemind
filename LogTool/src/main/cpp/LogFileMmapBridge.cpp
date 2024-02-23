#include <jni.h>

//
// Created by Ghost233 on 2023/1/30.
//

#include "LogFileMmap.h"

#include "LogHelper.h"

void mmapTypeCreateNewFileResponseFunc(std::string filename, LogFile *logFile) {
    jobject thiz = (jobject) logFile->jniObjectAndroidONLY;
    ENTER_JNI
    jclass clazz = env->GetObjectClass(thiz);
    jmethodID createNewFileResponseMethod = env->GetMethodID(clazz, "createNewFileResponse",
                                                             JNISigType(JNISIGArray(JNIString),
                                                                        JNIVoid));
    env->CallVoidMethod(thiz, createNewFileResponseMethod, env->NewStringUTF(filename.c_str()));
    LEAVE_JNI
}

extern "C"
JNIEXPORT jlong JNICALL
Java_me_ghost233_logtoolhandleinterface_logfilechandle_YSLogFileMmapBridge_nativeInit(
        JNIEnv *env, jobject thiz, jstring dir_path, jstring filename_prefix,
        jstring filename_suffix, jlong file_max_size, jlong file_time_interval_with_second) {
    auto ysLogFileMmap = new LogFileMmap();
    ysLogFileMmap->dirPath = env->GetStringUTFChars(dir_path, 0);
    ysLogFileMmap->filenamePrefix = env->GetStringUTFChars(filename_prefix, 0);
    ysLogFileMmap->filenameSuffix = env->GetStringUTFChars(filename_suffix, 0);
    ysLogFileMmap->fileMaxSize = (int) file_max_size;
    ysLogFileMmap->fileTimeIntervalWithSecond = (int) file_time_interval_with_second;
    ysLogFileMmap->jniObjectAndroidONLY = env->NewGlobalRef(thiz);
    ysLogFileMmap->createNewFileResponse = mmapTypeCreateNewFileResponseFunc;
    return (jlong) ysLogFileMmap;
}
extern "C"
JNIEXPORT void JNICALL
Java_me_ghost233_logtool_logtoolhandleinterface_logfilechandle_LogFileMmapBridge_nativeStart(
        JNIEnv *env, jobject thiz, jlong native_pointer) {

    auto ysLogFileMmap = (LogFileMmap *) native_pointer;
    ysLogFileMmap->start();
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_me_ghost233_logtool_logtoolhandleinterface_logfilechandle_LogFileMmapBridge_nativeWriteLog(
        JNIEnv *env, jobject thiz, jlong native_pointer, jbyteArray data, jlong start_index,
        jlong size) {
    if (native_pointer == 0) {
        return false;
    }
    auto ysLogFileMmap = (LogFileMmap *) native_pointer;
    jbyte *byteArray = env->GetByteArrayElements(data, nullptr); //获取数组指针
    auto result = ysLogFileMmap->writeLog((const char *) byteArray, (int) start_index, (int) size);
    return result;
}
extern "C"
JNIEXPORT void JNICALL
Java_me_ghost233_logtool_logtoolhandleinterface_logfilechandle_LogFileMmapBridge_nativeFlush(
        JNIEnv *env, jobject thiz, jlong native_pointer) {
    if (native_pointer == 0) {
        return;
    }
    auto ysLogFileMmap = (LogFileMmap *) native_pointer;
    ysLogFileMmap->flush();
}
extern "C"
JNIEXPORT void JNICALL
Java_me_ghost233_logtool_logtoolhandleinterface_logfilechandle_LogFileMmapBridge_nativeEnd(
        JNIEnv *env, jobject thiz, jlong native_pointer) {
    if (native_pointer == 0) {
        return;
    }
    auto ysLogFileMmap = (LogFileMmap *) native_pointer;
    ysLogFileMmap->end();
}
extern "C"
JNIEXPORT jstring JNICALL
Java_me_ghost233_logtool_logtoolhandleinterface_logfilechandle_LogFileMmapBridge_nativeCreateNewFilenameWithTime(
        JNIEnv *env, jobject thiz, jlong native_pointer) {
    if (native_pointer == 0) {
        return env->NewStringUTF("");
    }
    auto ysLogFileMmap = (LogFileMmap *) native_pointer;
    auto result = ysLogFileMmap->createNewFilenameWithTime();
    return env->NewStringUTF(result.c_str());
}
extern "C"
JNIEXPORT void JNICALL
Java_me_ghost233_logtool_logtoolhandleinterface_logfilechandle_LogFileMmapBridge_destory(
        JNIEnv *env, jobject thiz, jlong native_pointer) {
    if (native_pointer == 0) {
        return;
    }
    auto ysLogFileMmap = (LogFileMmap *) native_pointer;
    env->DeleteGlobalRef(static_cast<jobject>(ysLogFileMmap->jniObjectAndroidONLY));
    delete ysLogFileMmap;
}
extern "C"
JNIEXPORT jstring JNICALL
Java_me_ghost233_logtool_logtoolhandleinterface_logfilechandle_LogFileMmapBridge_nativeGetNowFilename(
        JNIEnv *env, jobject thiz, jlong native_pointer) {
    if (native_pointer == 0) {
        return env->NewStringUTF("");
    }
    auto ysLogFileMmap = (LogFileMmap *) native_pointer;
    auto result = ysLogFileMmap->getNowFilename();
    return env->NewStringUTF(result.c_str());
}
extern "C"
JNIEXPORT void JNICALL
Java_me_ghost233_logtool_logtoolhandleinterface_logfilechandle_LogFileMmapBridge_00024Companion_nativeRepair(
        JNIEnv *env, jobject thiz, jstring filename) {
    auto filenameStr = env->GetStringUTFChars(filename, 0);
    LogFileMmap::repair(filenameStr);
}
extern "C"
JNIEXPORT void JNICALL
Java_me_ghost233_logtool_logtoolhandleinterface_logfilechandle_LogFileMmapBridge_nativeRenew(
        JNIEnv *env, jobject thiz, jlong native_pointer) {
    if (native_pointer == 0) {
        return;
    }
    auto ysLogFileMmap = (LogFileMmap *) native_pointer;
    ysLogFileMmap->renew();
}

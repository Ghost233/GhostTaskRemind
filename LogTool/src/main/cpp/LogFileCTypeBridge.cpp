#include <jni.h>

//
// Created by Ghost233 on 2023/1/30.
//

#include "LogFileCType.h"

#include "LogHelper.h"

void cTypeCreateNewFileResponseFunc(std::string filename, LogFile *logFile) {
    jobject thiz = (jobject) logFile->jniObjectAndroidONLY;
//    ENTER_JNI
    USE_DEFER; \
    JNIEnv *env = NULL;      \
    LogHelper::getInstance()->getJNIEnv(&env);
    jclass clazz = env->GetObjectClass(thiz);
    jmethodID createNewFileResponseMethod = env->GetMethodID(clazz, "createNewFileResponse",
                                                             JNISigType(JNISIGArray(JNIString),
                                                                        JNIVoid));
    env->CallVoidMethod(thiz, createNewFileResponseMethod, env->NewStringUTF(filename.c_str()));
    LEAVE_JNI
}

extern "C"
JNIEXPORT jlong JNICALL
Java_me_ghost233_logtool_logtoolhandleinterface_logfilechandle_LogFileCTypeBridge_nativeInit(
        JNIEnv *env, jobject thiz, jstring dir_path, jstring filename_prefix,
        jstring filename_suffix, jlong file_max_size,
        jlong file_time_interval_with_second) {
    auto ysLogFileCType = new LogFileCType();
    ysLogFileCType->dirPath = env->GetStringUTFChars(dir_path, 0);
    ysLogFileCType->filenamePrefix = env->GetStringUTFChars(filename_prefix, 0);
    ysLogFileCType->filenameSuffix = env->GetStringUTFChars(filename_suffix, 0);
    ysLogFileCType->fileMaxSize = (int) file_max_size;
    ysLogFileCType->fileTimeIntervalWithSecond = (int) file_time_interval_with_second;
    ysLogFileCType->jniObjectAndroidONLY = env->NewGlobalRef(thiz);
    ysLogFileCType->createNewFileResponse = cTypeCreateNewFileResponseFunc;
    return (jlong) ysLogFileCType;
}
extern "C"
JNIEXPORT void JNICALL
Java_me_ghost233_logtool_logtoolhandleinterface_logfilechandle_LogFileCTypeBridge_nativeStart(
        JNIEnv *env, jobject thiz, jlong native_pointer) {
    auto ysLogFileCType = (LogFileCType *) native_pointer;
    ysLogFileCType->start();
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_me_ghost233_logtool_logtoolhandleinterface_logfilechandle_LogFileCTypeBridge_nativeWriteLog(
        JNIEnv *env, jobject thiz, jlong native_pointer, jbyteArray data, jlong start_index,
        jlong size) {
    auto ysLogFileCType = (LogFileCType *) native_pointer;
    jbyte *byteArray = env->GetByteArrayElements(data, nullptr); //获取数组指针
    auto result = ysLogFileCType->writeLog((const char *) byteArray, (int) start_index, (int) size);
    return result;
}
extern "C"
JNIEXPORT void JNICALL
Java_me_ghost233_logtool_logtoolhandleinterface_logfilechandle_LogFileCTypeBridge_nativeFlush(
        JNIEnv *env, jobject thiz, jlong native_pointer) {
    auto ysLogFileCType = (LogFileCType *) native_pointer;
    ysLogFileCType->flush();
}
extern "C"
JNIEXPORT void JNICALL
Java_me_ghost233_logtool_logtoolhandleinterface_logfilechandle_LogFileCTypeBridge_nativeEnd(
        JNIEnv *env, jobject thiz, jlong native_pointer) {
    auto ysLogFileCType = (LogFileCType *) native_pointer;
    ysLogFileCType->end();
}
extern "C"
JNIEXPORT jstring JNICALL
Java_me_ghost233_logtool_logtoolhandleinterface_logfilechandle_LogFileCTypeBridge_nativeCreateNewFilenameWithTime(
        JNIEnv *env, jobject thiz, jlong native_pointer) {
    auto ysLogFileCType = (LogFileCType *) native_pointer;
    auto result = ysLogFileCType->createNewFilenameWithTime();
    return env->NewStringUTF(result.c_str());
}
extern "C"
JNIEXPORT void JNICALL
Java_me_ghost233_logtool_logtoolhandleinterface_logfilechandle_LogFileCTypeBridge_destory(
        JNIEnv *env, jobject thiz, jlong native_pointer) {
    auto ysLogFileCType = (LogFileCType *) native_pointer;
    env->DeleteGlobalRef(static_cast<jobject>(ysLogFileCType->jniObjectAndroidONLY));
    delete ysLogFileCType;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_me_ghost233_logtool_logtoolhandleinterface_logfilechandle_LogFileCTypeBridge_nativeGetNowFilename(
        JNIEnv *env, jobject thiz, jlong native_pointer) {
    auto ysLogFileCType = (LogFileCType *) native_pointer;
    auto result = ysLogFileCType->getNowFilename();
    return env->NewStringUTF(result.c_str());
}
extern "C"
JNIEXPORT void JNICALL
Java_me_ghost233_logtool_logtoolhandleinterface_logfilechandle_LogFileCTypeBridge_nativeRenew(
        JNIEnv *env, jobject thiz, jlong native_pointer) {
    auto ysLogFileCType = (LogFileCType *) native_pointer;
    ysLogFileCType->renew();
}
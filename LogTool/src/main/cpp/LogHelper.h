//
// Created by Ghost233 on 2023/5/30.
//

#ifndef CARLYNC_PORSCHE_YSLOGHELPER_H
#define CARLYNC_PORSCHE_YSLOGHELPER_H

#include <android/log.h>
#include <jni.h>
#include <iostream>
#include <stdio.h>
#include <memory>
#include <mutex>
#include <thread>
#include <string>
#include <stack>
#include <vector>
#include <map>
#include <unordered_map>
#include <thread>
#include <functional>

#include "JNIDefer.h"

#ifndef ENTER_JNI

#define ENTER_JNI USE_DEFER; \
    JNIEnv *env = NULL;      \
    LogHelper::getInstance()->getJNIEnv(&env);

#endif //ENTER_JNI

#ifndef LEAVE_JNI

#define LEAVE_JNI ReleaseDefer;

#endif //LEAVE_JNI

#define JNIBoolean "Z"
#define JNIVoid "V"
#define JNIByte "B"
#define JNIChar "c"
#define JNIShort "S"
#define JNIInt "I"
#define JNILong "J"
#define JNIFloat "F"
#define JNIDouble "D"
#define JNIString "Ljava/lang/String;"
#define JNIObject "Ljava/lang/Object;"
#define JNIHashMap "Ljava/util/HashMap;"

#define JNIBooleanArray "[Z"
#define JNIByteArray "[B"
#define JNICharArray "[c"
#define JNIShortArray "[S"
#define JNIIntArray "[I"
#define JNILongArray "[J"
#define JNIFloatArray "[F"
#define JNIDoubleArray "[D"
#define JNIStringArray "[Ljava/lang/String;"
#define JNIObjectArray "[Ljava/lang/Object;"
#define JNISIGArray(...) std::vector<std::string>{__VA_ARGS__}
#define JNISigType(in, out) LogHelper::typeSigMaker(in, out).c_str()
#define JNIClassPathSig(type) YSLogHelper::classPathToSig(type).c_str()
#define JNIToCPP(type)    jclass cls = env->GetObjectClass(thiz); \
jlong pointer = env->GetLongField(thiz, env->GetFieldID(cls, "nativePointer", "J")); \
type *obj = (type *) pointer;


class LogHelper {
public:
    static std::shared_ptr<LogHelper> getInstance();

    void initJavaVM(JNIEnv *env);

    JavaVM *getJavaVM();

    void getJNIEnv(JNIEnv **env);

    static void string_replace(std::string &s1, const std::string &s2, const std::string &s3);

    static std::string typeSigMaker(const std::vector<std::string> &inTypeString,
                                    std::string outTypeString);

    static std::string classPathToSig(const std::string &classPath);

    jobject thiz;
    jclass native_clazz;

private:
    static std::shared_ptr<LogHelper> m_instance_ptr;
    static std::mutex m_mutex;

    JavaVM *gJavaVM;
};

#endif //CARLYNC_PORSCHE_YSLOGHELPER_H

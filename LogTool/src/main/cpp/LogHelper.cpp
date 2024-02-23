//
// Created by Ghost233 on 2023/5/30.
//

#include "LogHelper.h"

std::shared_ptr<LogHelper> LogHelper::m_instance_ptr;
std::mutex LogHelper::m_mutex;

std::shared_ptr<LogHelper> LogHelper::getInstance() {
    if (m_instance_ptr == nullptr) {
        std::lock_guard<std::mutex> lk(m_mutex);
        if (m_instance_ptr == nullptr) {
            m_instance_ptr = std::make_shared<LogHelper>();
        }
    }
    return m_instance_ptr;
}

void LogHelper::initJavaVM(JNIEnv *env) {
    env->GetJavaVM(&this->gJavaVM);
}

JavaVM *LogHelper::getJavaVM() {
    return this->gJavaVM;
}

void LogHelper::getJNIEnv(JNIEnv **env) {
    int getEnvStat = this->getJavaVM()->GetEnv((void **) env, JNI_VERSION_1_6);
    if (getEnvStat == JNI_EDETACHED) {
        if (this->getJavaVM()->AttachCurrentThread(env, NULL) != 0) {
        }
    } else if (getEnvStat == JNI_OK) {
    } else if (getEnvStat == JNI_EVERSION) {
    }
}

std::string LogHelper::typeSigMaker(const std::vector<std::string> &inTypeString,
                                    std::string outTypeString) {
    std::string typeSig = "(";
    if ((inTypeString.size() == 1) && (inTypeString[0] == JNIVoid)) {
    } else {
        for (auto &i: inTypeString) {
            typeSig += i;
        }
    }
    typeSig += ")";
    typeSig += outTypeString;

    return typeSig;
}

void LogHelper::string_replace(std::string &s1, const std::string &s2, const std::string &s3) {
    std::string::size_type pos = 0;
    std::string::size_type a = s2.size();
    std::string::size_type b = s3.size();
    while ((pos = s1.find(s2, pos)) != std::string::npos) {
        s1.replace(pos, a, s3);
        pos += b;
    }
}

std::string LogHelper::classPathToSig(const std::string &classPath) {
    std::string sig(classPath);
    string_replace(sig, ".", "/");
    return sig;
}

extern "C"
JNIEXPORT void JNICALL
Java_me_ghost233_logtool_LogHelper_nativeInit(JNIEnv *env, jobject thiz) {
    LogHelper::getInstance()->thiz = env->NewGlobalRef(thiz);
    LogHelper::getInstance()->native_clazz = env->GetObjectClass(thiz);
    LogHelper::getInstance()->initJavaVM(env);
}
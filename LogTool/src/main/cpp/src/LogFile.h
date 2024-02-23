//
// Created by Ghost233 on 2023/1/14.
//

#ifndef LOGTOOL_YSLOGFILE_H
#define LOGTOOL_YSLOGFILE_H

#include <string>
#include <mutex>

class LogFile {
public:

    virtual void start() = 0;

    virtual bool writeLog(const char *data, long startIndex, unsigned long size) = 0;

    virtual void flush() = 0;

    virtual void end() = 0;

    virtual void renew() = 0;

    virtual std::string getNowFilename() = 0;

    virtual std::string createNewFilenameWithTime();

    bool isOpen;

    //确保每一个创建文件时只有一个线程在创建,避免冲突
    static std::mutex globalCreateFileMutex;

    std::mutex fileMutex;

    std::string dirPath;
    std::string filenamePrefix;
    std::string filenameSuffix;
    long fileMaxSize;
    long fileTimeIntervalWithSecond;

    void *jniObjectAndroidONLY;
    std::function<void(std::string, LogFile *)> createNewFileResponse;
};

#endif //LOGTOOL_YSLOGFILE_H

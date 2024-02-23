//
// Created by Ghost233 on 2023/1/14.
//

#include "LogFile.h"

#include <sys/time.h>

std::mutex LogFile::globalCreateFileMutex;

std::string LogFile::createNewFilenameWithTime() {
    //根据当前时间创建文件名 "@filenamePrefix_年_月_日_时_分_秒_年_月_日_时_分_秒_@filenameSuffix.txt"
    // 获取当前时间
    struct timeval curTime{};
    gettimeofday(&curTime, nullptr);

    // 设置起始日期为本地时间
    struct tm startDate{};
    localtime_r(&(curTime.tv_sec), &startDate);
    startDate.tm_zone = "Asia/Shanghai";

    // 计算结束日期（startDate + 4小时）
    struct timeval endTimeval{};
    endTimeval.tv_sec = curTime.tv_sec + 4 * 3600;
    endTimeval.tv_usec = curTime.tv_usec;

    // 将结束日期转换为本地时间
    struct tm endDate{};
    localtime_r(&(endTimeval.tv_sec), &endDate);
    endDate.tm_zone = "Asia/Shanghai";

    char filename[1000] = {0};
    //使用snprintf进行格式化
    int length = snprintf(filename, 1000,
                          "%s%s_%04d-%02d-%02d_%02d_%02d_%02d_%04d-%02d-%02d_%02d_%02d_%02d_%s.txt",
                          dirPath.c_str(), filenamePrefix.c_str(),
                          startDate.tm_year + 1900, startDate.tm_mon + 1, startDate.tm_mday,
                          startDate.tm_hour, startDate.tm_min, startDate.tm_sec,
                          endDate.tm_year + 1900, endDate.tm_mon + 1, endDate.tm_mday,
                          endDate.tm_hour, endDate.tm_min, endDate.tm_sec,
                          filenameSuffix.c_str());
    return {filename, static_cast<unsigned long>(length)};
}

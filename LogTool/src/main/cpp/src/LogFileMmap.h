//
// Created by Ghost233 on 2023/1/13.
//

#ifndef LOGTOOL_YSLOGFILEMMAP_H
#define LOGTOOL_YSLOGFILEMMAP_H

#include <stdio.h>
#include <string>

#include "LogFile.h"

class LogFileMmap : public LogFile {
public:
    LogFileMmap();

    virtual void start();

    virtual bool writeLog(const char *data, long startIndex, unsigned long size);

    virtual void flush();

    virtual void end();

    virtual void renew();

    virtual std::string getNowFilename();

    static void repair(std::string filename);

protected:

    void _flush();

    void release();

    FILE *logFile;

    std::string nowFilename;
    int nowFileSize;

    std::string mmapFileName;
    int fileDescriptor;
    static std::size_t logPageMaxSize;
    static std::size_t filePageMaxSize;
    static int fileStartIndexOffset;
    static int intSize;
    char *mmapChar;
    int *mmapEndIndex;
};


#endif //LOGTOOL_YSLOGFILEMMAP_H

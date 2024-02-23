//
//  LogFileCType.hpp
//  LogTool
//
//  Created by Ghost233 on 2023/1/13.
//

#ifndef YSLogFileCType_h
#define YSLogFileCType_h

#include <stdio.h>
#include <string>

#include "LogFile.h"

class LogFileCType : public LogFile {
public:

    virtual void start();

    virtual bool writeLog(const char *data, long startIndex, unsigned long size);

    virtual void flush();

    virtual void end();

    virtual void renew();

    virtual std::string getNowFilename();

protected:

    bool open(std::string filepath);

    void release();

    FILE *file;

    int fileSize;

    std::string nowFilename;
};

#endif /* YSLogFileCType_h */

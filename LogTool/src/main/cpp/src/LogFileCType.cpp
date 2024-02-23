//
//  LogFileCType.cpp
//  LogTool
//
//  Created by Ghost233 on 2023/1/13.
//

#include <thread>
#include <time.h>
#include "LogFileCType.h"

#pragma mark OutController

void LogFileCType::start() {
    fileMutex.lock();
    renew();
    fileMutex.unlock();
}

bool LogFileCType::writeLog(const char *data, long startIndex, unsigned long size) {
    fileMutex.lock();
    bool flag = fwrite(data + startIndex, 1, size, file) == size;
    fileSize += size;
    if (fileSize > fileMaxSize) {
        renew();
    }
    fileMutex.unlock();
    return flag;
}

void LogFileCType::flush() {
    if (file == NULL) {
        return;
    }
    fileMutex.lock();
    fflush(file);
    fileMutex.unlock();
}

void LogFileCType::end() {
    if (file == NULL) {
        return;
    }
    fileMutex.lock();
    release();
    fileMutex.unlock();
}

std::string LogFileCType::getNowFilename() {
    return this->nowFilename;
}

#pragma mark InsideController

bool LogFileCType::open(std::string filepath) {
    file = fopen(filepath.c_str(), "w");
    return file != NULL;
}

void LogFileCType::renew() {
    globalCreateFileMutex.lock();
    if (file != NULL) {
        release();
    }

    isOpen = false;
    std::string tempNowFilename = createNewFilenameWithTime();
    int tryCount = 0;
    while (!open(tempNowFilename)) {
//        std::this_thread::sleep_for(std::chrono::milliseconds(1000));//睡眠1000毫秒
        tempNowFilename = createNewFilenameWithTime();
        tryCount++;
        if (tryCount > 100) {
            isOpen = false;
            break;
        }
    }
    if (tryCount <= 100) {
        isOpen = true;
        if (this->createNewFileResponse != NULL) {
            this->createNewFileResponse(tempNowFilename, this);
        }
        this->nowFilename = tempNowFilename;
    }
    globalCreateFileMutex.unlock();
}

void LogFileCType::release() {
    fclose(file);
    file = NULL;
}

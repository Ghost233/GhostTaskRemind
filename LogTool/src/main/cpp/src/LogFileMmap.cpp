//
// Created by Ghost233 on 2023/1/13.
//

#include "LogFileMmap.h"

#include <sys/mman.h>
#include <unistd.h>
#include <sys/fcntl.h>
#include <thread>

#pragma mark OutController

int LogFileMmap::intSize = sizeof(int);
int LogFileMmap::fileStartIndexOffset = intSize * 1;
std::size_t LogFileMmap::logPageMaxSize = 1 * 1024;
std::size_t LogFileMmap::filePageMaxSize = logPageMaxSize + fileStartIndexOffset;

LogFileMmap::LogFileMmap() {
    fileDescriptor = -1;
}

void LogFileMmap::start() {
//    std::unique_lock<std::mutex> lock(fileMutex, std::try_to_lock);
//    while (lock.owns_lock() == false){
//        std::this_thread::sleep_for(std::chrono::milliseconds(1));
//    }
    renew();
}

bool LogFileMmap::writeLog(const char *data, long startIndex, unsigned long size) {
//    std::unique_lock<std::mutex> lock(fileMutex, std::try_to_lock);
//    while (lock.owns_lock() == false){
//        std::this_thread::sleep_for(std::chrono::milliseconds(1));
//    }
    if (!isOpen) {
        return false;
    }
    if ((*mmapEndIndex) + size > logPageMaxSize) {
        _flush();
    }
    nowFileSize += size;
    if (size > logPageMaxSize) {
        fwrite(data + startIndex, 1, size, logFile);
        fflush(logFile);
    } else {
        memcpy(mmapChar + (*mmapEndIndex) + fileStartIndexOffset, data + startIndex, size);
        (*mmapEndIndex) += size;
    }
    if (nowFileSize > fileMaxSize) {
        _flush();
        renew();
    }
    return true;
}

void LogFileMmap::flush() {
    if (!isOpen) {
        return;
    }
//    std::unique_lock<std::mutex> lock(fileMutex, std::try_to_lock);
//    while (lock.owns_lock() == false){
//        std::this_thread::sleep_for(std::chrono::milliseconds(1));
//    }
    _flush();
}

void LogFileMmap::_flush() {
    if (!isOpen) {
        return;
    }
    //将mmap中的数据写入logfile
    fwrite(mmapChar + fileStartIndexOffset, 1, (*mmapEndIndex), logFile);
    fflush(logFile);
    (*mmapEndIndex) = 0;
}

void LogFileMmap::end() {
    release();
}

void LogFileMmap::repair(std::string filename) {
    std::string mmapFilename = filename + ".mmap";

    //判断该文件大小,如果文件大小不是filePageMaxSize,则说明文件没有正常关闭,无法修复,直接退出,并删除文件
    FILE *logFile = fopen(mmapFilename.c_str(), "r");
    if (logFile == NULL) {
        return;
    }
    fseek(logFile, 0, SEEK_END);
    long fileSize = ftell(logFile);
    fclose(logFile);
    if ((fileSize % filePageMaxSize != 0) || (fileSize == 0)) {
        remove(filename.c_str());
        remove(mmapFilename.c_str());
        return;
    }

    //fopen filename这个文件,并以追加的方式写入
    logFile = fopen(filename.c_str(), "a");
    int tempfileDescriptor = open(mmapFilename.c_str(), O_RDWR | O_CREAT, 00777);

    char *mmapChar;
    int *mmapEndIndex;
    int intSize = sizeof(int);
    int fileStartIndexOffset = intSize * 1;
    std::size_t logPageMaxSize = 1 * 1024;;
    std::size_t filePageMaxSize = logPageMaxSize + fileStartIndexOffset;

    mmapChar = (char *) mmap(NULL, filePageMaxSize, PROT_READ | PROT_WRITE, MAP_SHARED,
                             tempfileDescriptor, 0);
    close(tempfileDescriptor);
    mmapEndIndex = (int *) mmapChar;

    fwrite(mmapChar + fileStartIndexOffset, 1, (*mmapEndIndex), logFile);
    fflush(logFile);
    fclose(logFile);
    (*mmapEndIndex) = 0;
    munmap(mmapChar, filePageMaxSize);
    remove(mmapFilename.c_str());
}

std::string LogFileMmap::getNowFilename() {
    return this->nowFilename;
}

#pragma mark InsideController

void LogFileMmap::renew() {
//    std::unique_lock<std::mutex> lock(globalCreateFileMutex, std::try_to_lock);
//    while (lock.owns_lock() == false){
//        std::this_thread::sleep_for(std::chrono::milliseconds(1));
//    }
    if (fileDescriptor != -1) {
        release();
    }

    isOpen = false;
    std::string tempNowFilename = createNewFilenameWithTime();
    int tryCount = 0;
    do {
        tempNowFilename = createNewFilenameWithTime();
        mmapFileName = tempNowFilename + ".mmap";

        logFile = fopen(tempNowFilename.c_str(), "a+");
        fileDescriptor = open(mmapFileName.c_str(), O_RDWR | O_CREAT, 00777);

        tryCount++;
        if (tryCount > 100) {
            isOpen = false;
            break;
        }

    } while ((fileDescriptor == -1) or (logFile == NULL));
    if (tryCount <= 100) {

        lseek(fileDescriptor, static_cast<off_t>(LogFileMmap::filePageMaxSize - 1), SEEK_END);
        write(fileDescriptor, "\0", 1);
        mmapChar = (char *) mmap(NULL, LogFileMmap::filePageMaxSize, PROT_READ | PROT_WRITE,
                                 MAP_SHARED,
                                 fileDescriptor, 0);
        close(fileDescriptor);
        mmapEndIndex = (int *) mmapChar;

        this->nowFilename = tempNowFilename;
        isOpen = true;
        if (this->createNewFileResponse != NULL) {
            this->createNewFileResponse(tempNowFilename, this);
        }
    }

    nowFileSize = 0;
}

void LogFileMmap::release() {
    if (!isOpen) {
        return;
    }
    _flush();
    fclose(logFile);
    munmap(mmapChar, LogFileMmap::filePageMaxSize);
    isOpen = false;
}

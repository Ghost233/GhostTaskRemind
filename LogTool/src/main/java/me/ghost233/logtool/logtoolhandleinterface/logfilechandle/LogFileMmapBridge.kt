package me.ghost233.logtool.logtoolhandleinterface.logfilechandle

class LogFileMmapBridge : LogFileBridge() {
    companion object {
        fun repair(filename: String) {
            nativeRepair(filename)
        }

        external fun nativeRepair(filename: String)
    }

    var nativePointer: Long = 0L

    override fun start() {
        nativePointer = nativeInit(
            dirPath,
            filenamePrefix,
            filenameSuffix,
            fileMaxSize,
            fileTimeIntervalWithSecond,
        )
        nativeStart(nativePointer)
    }

    override fun writeLog(data: ByteArray, startIndex: Long, size: Long): Boolean {
        return nativeWriteLog(nativePointer, data, startIndex, size)
    }

    override fun flush() {
        nativeFlush(nativePointer)
    }

    override fun end() {
        nativeEnd(nativePointer)
        destory(nativePointer)
        nativePointer = 0
    }

    override fun renew() {
        nativeRenew(nativePointer)
    }

    override fun createNewFilenameWithTime(): String {
        return nativeCreateNewFilenameWithTime(nativePointer)
    }

    override fun getNowFilename(): String {
        return nativeGetNowFilename(nativePointer)
    }

    fun createNewFileResponse(filename: String) {
        createNewFileResponse(filename, this)
    }

    // JNI
    external fun nativeInit(
        dirPath: String,
        filenamePrefix: String,
        filenameSuffix: String,
        fileMaxSize: Long,
        fileTimeIntervalWithSecond: Long,
    ): Long

    external fun nativeStart(nativePointer: Long)
    external fun nativeWriteLog(
        nativePointer: Long,
        data: ByteArray,
        startIndex: Long,
        size: Long,
    ): Boolean

    external fun nativeFlush(nativePointer: Long)
    external fun nativeEnd(nativePointer: Long)
    external fun nativeRenew(nativePointer: Long)
    external fun nativeCreateNewFilenameWithTime(nativePointer: Long): String
    external fun destory(nativePointer: Long)
    external fun nativeGetNowFilename(nativePointer: Long): String
}
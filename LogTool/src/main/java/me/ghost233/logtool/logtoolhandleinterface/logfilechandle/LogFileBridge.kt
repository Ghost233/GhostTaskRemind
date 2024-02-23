package me.ghost233.logtool.logtoolhandleinterface.logfilechandle

abstract class LogFileBridge {
    abstract fun start()
    abstract fun writeLog(data: ByteArray, startIndex: Long, size: Long): Boolean
    abstract fun flush()
    abstract fun end()
    abstract fun renew()
    abstract fun createNewFilenameWithTime(): String
    abstract fun getNowFilename(): String
    var isOpen: Boolean = false
    var subDirPath: String = ""
    var dirPath: String = ""
    var filenamePrefix: String = ""
    var filenameSuffix: String = ""
    var fileMaxSize: Long = 0
    var fileTimeIntervalWithSecond: Long = 0
    var createNewFileResponse: (String, LogFileBridge) -> Unit = { _, _ -> }
}
package me.ghost233.logtool

import androidx.annotation.Keep
import me.ghost233.logtool.logtoolconfig.LogToolConfigFile
import me.ghost233.logtool.logtoolconfig.LogToolConfigInterface
import me.ghost233.logtool.logtoolhandleinterface.LogToolHandleFileHandle
import kotlin.concurrent.thread

@Keep
class LogTool {
    companion object {
        private const val TAG = "LogTool"
        private val sharedInterface by lazy { LogTool() }
        var IsInit = true

        fun addConfig(config: LogToolConfigInterface) {
            sharedInterface.logConfigArray.add(config)
        }

        fun removeConfig(config: LogToolConfigInterface) {
            sharedInterface.logConfigArray.remove(config)
        }

        fun writeInit(tag: String, msg: String) {
            sharedInterface.writeLog(LogProtocol.LogType.Init, tag, msg)
        }

        fun writeNetwork(tag: String, msg: String) {
            sharedInterface.writeLog(LogProtocol.LogType.Network, tag, msg)
        }

        fun writeDebug(tag: String, msg: String) {
            sharedInterface.writeLog(LogProtocol.LogType.Debug, tag, msg)
        }

        fun writeCrash(tag: String, msg: String) {
            sharedInterface.writeLog(LogProtocol.LogType.CRASH, tag, msg)
        }

        fun writeError(tag: String, msg: String) {
            sharedInterface.writeLog(LogProtocol.LogType.CRASH, tag, msg)
        }

        fun writeLog(logType: LogProtocol.LogType, tag: String, msg: String, timestamp: Long) {
            sharedInterface.writeLog(logType, tag, msg, timestamp)
        }

        fun startUploadLog() {
            sharedInterface.startUploadLog()
        }

        fun LOGTESTDIRECTLY(msg: String) {
            sharedInterface.msg(msg)
        }

        fun setUploadFileBlock(uploadFileBlock: (String) -> Boolean) {
            sharedInterface.uploadFileBlock = uploadFileBlock
        }

        fun setUploadLogTypeBlock(uploadLogTypeBlock: (LogProtocol.LogType) -> Void?) {
            sharedInterface.uploadLogTypeBlock = uploadLogTypeBlock
        }

        fun uploadLog(logType: LogProtocol.LogType) {
            sharedInterface.uploadLogTypeBlock(logType)
        }

        fun uploadAllLog() {
            for (i in 0 until sharedInterface.logConfigArray.size) {
                val config = sharedInterface.logConfigArray[i]
                if (config is LogToolConfigFile) {
                    config.handle.renew()
                    if (config.handle is LogToolHandleFileHandle) {
                        val fileHandle = config.handle as LogToolHandleFileHandle
                        fileHandle.repairAllMMAP()
                    }
                }
            }
            sharedInterface.startUploadLog()
        }
    }

    private var filenameSet = mutableSetOf<String>()

    init {
        if (IsInit) {
            LogHelper.shared
        }
        thread {
            val tempMap: MutableMap<Long, MutableSet<String>> = mutableMapOf()
            while (true) {
                if (this.uploadingMap == null) {
                    this.uploadingMap = mutableMapOf()
                }
                synchronized(this.uploadingMap) {
                    uploadBreak = false
                    for (priority in this.uploadingMap.keys) {
                        if (!tempMap.containsKey(priority)) {
                            tempMap[priority] = mutableSetOf()
                        }
                        tempMap[priority]!!.addAll(this.uploadingMap[priority]!!)
                        this.uploadingMap[priority]!!.clear()
                    }
                }
                var uploadingArrayCount = 0
                val priorityList = tempMap.keys
                priorityList.sortedDescending()
                for (priority in priorityList) {
                    uploadingArrayCount += tempMap[priority]!!.size
                }
//                LogTool.writeDebug(
//                    TAG,
//                    "priorityList: $priorityList uploadingArrayCount: $uploadingArrayCount"
//                )
                if (uploadingArrayCount == 0) {
                    Thread.sleep(1000)
                } else {
                    for (priority in priorityList) {
                        if (uploadBreak) {
                            break
                        }
                        val tempArray = tempMap[priority]!!.toTypedArray()
                        for (filename in tempArray) {
                            if (uploadBreak) {
                                break
                            }
                            if (filenameSet.contains(filename)) {
                                tempMap[priority]!!.remove(filename)
                                writeDebug(TAG, "$filename upload already success")
                                continue
                            }
                            if (this.uploadFileBlock(filename)) {
                                try {
                                    java.io.File(filename).renameTo(java.io.File("$filename.bak"))
                                    filenameSet.add(filename)
                                } catch (e: Exception) {
                                    writeDebug(TAG, "renameTo $filename.bak failed")
                                    e.printStackTrace()
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private lateinit var uploadFileBlock: (String) -> Boolean
    private lateinit var uploadLogTypeBlock: (LogProtocol.LogType) -> Void?
    private var logConfigArray = mutableListOf<LogToolConfigInterface>()

    fun writeLog(logType: LogProtocol.LogType, tag: String, msg: String) {
        for (config in logConfigArray) {
            if (config.logTypeSet.contains(logType)) {
                config.handle.writeLog(logType, tag, msg)
            }
        }
    }

    fun writeLog(logType: LogProtocol.LogType, tag: String, msg: String, timestamp: Long) {
        for (config in logConfigArray) {
            if (config.logTypeSet.contains(logType)) {
                config.handle.writeLog(logType, tag, msg)
            }
        }
    }

    fun msg(msg: String) {
        for (config in logConfigArray) {
            if (config.logTypeSet.contains(LogProtocol.LogType.Debug)) {
                config.handle.msg(msg)
            }
        }
    }

    private var uploadingMap: MutableMap<Long, MutableSet<String>> = mutableMapOf()
    private var uploadBreak = false

    fun startUploadLog() {
        val temp = mutableListOf<LogToolConfigFile>()
        for (config in logConfigArray) {
            if (config is LogToolConfigFile) {
                temp.add(config)
            }
        }
        if (this.uploadingMap == null) {
            this.uploadingMap = mutableMapOf()
        }
        synchronized(this.uploadingMap) {
            for (config in temp) {
                val list = (config.handle as LogToolHandleFileHandle).getFileList()
                if (!this.uploadingMap.containsKey(config.priority)) {
                    this.uploadingMap[config.priority] = mutableSetOf()
                }
                this.uploadingMap[config.priority]!!.addAll(list)
            }
        }
        uploadBreak = true
    }
}
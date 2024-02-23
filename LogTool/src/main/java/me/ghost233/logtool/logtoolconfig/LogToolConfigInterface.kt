package me.ghost233.logtool.logtoolconfig

import me.ghost233.logtool.LogProtocol
import me.ghost233.logtool.logtoolhandleinterface.LogToolHandleInterface

enum class LogToolConfigEnum {
    LogToolConfigEnumUnknown,
    LogToolConfigEnumFile,
    LogToolConfigEnumLog,
}

abstract class LogToolConfigInterface {
    companion object {
        const val TAG = "LogToolConfigInterface"
        val globalHandleLog = Any()
    }

    abstract fun getLogToolConfigEnum(): LogToolConfigEnum
    open lateinit var handle: LogToolHandleInterface
    var logTypeSet = hashSetOf<LogProtocol.LogType>()
    val handleLock = Any()
}
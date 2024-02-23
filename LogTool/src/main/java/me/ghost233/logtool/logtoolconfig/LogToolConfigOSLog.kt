package me.ghost233.logtool.logtoolconfig

import me.ghost233.logtool.logtoolhandleinterface.LogToolHandleInterface
import me.ghost233.logtool.logtoolhandleinterface.LogToolHandleOSLog

class LogToolConfigOSLog : LogToolConfigInterface() {
    override fun getLogToolConfigEnum(): LogToolConfigEnum {
        return LogToolConfigEnum.LogToolConfigEnumLog
    }

    init {
        synchronized(handleLock) {
            handle = LogToolHandleOSLog()
        }
    }

    override var handle: LogToolHandleInterface
        get() {
            if (!field.isInit) {
                field.initWithConfig(this)
            }
            return field
        }
}
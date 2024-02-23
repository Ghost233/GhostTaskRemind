package me.ghost233.logtool.logtoolhandleinterface

import me.ghost233.logtool.LogProtocol
import me.ghost233.logtool.logtoolconfig.LogToolConfigInterface

abstract class LogToolHandleInterface {
    abstract fun initWithConfig(tempConfig: LogToolConfigInterface): LogToolHandleInterface
    abstract fun writeLog(logType: LogProtocol.LogType, tag: String, msg: String)
    abstract fun start()
    abstract fun stop()
    abstract fun renew()
    abstract fun repairAllMMAP()
    abstract fun msg(msg: String)
    var isInit = false;
}
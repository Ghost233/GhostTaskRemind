package me.ghost233.logtool.logtoolhandleinterface

import android.util.Log
import me.ghost233.logtool.LogProtocol
import me.ghost233.logtool.logtoolconfig.LogToolConfigInterface

class LogToolHandleOSLog : LogToolHandleInterface() {
    override fun initWithConfig(tempConfig: LogToolConfigInterface): LogToolHandleInterface {
        this.isInit = true
        return this
    }

    override fun writeLog(logType: LogProtocol.LogType, tag: String, msg: String) {
        if (logType == LogProtocol.LogType.CRASH) {
            Log.e(tag, msg)
        } else {
            Log.i(tag, msg)
        }
    }

    override fun start() {
    }

    override fun stop() {
    }

    override fun repairAllMMAP() {
    }

    override fun renew() {
    }

    override fun msg(msg: String) {
        Log.i("LogTool", msg)
    }
}
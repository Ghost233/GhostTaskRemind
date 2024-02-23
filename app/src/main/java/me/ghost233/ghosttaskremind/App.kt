package me.ghost233.ghosttaskremind

import android.app.Application
import com.blankj.utilcode.util.Utils
import me.ghost233.logtool.LogProtocol
import me.ghost233.logtool.LogTool
import me.ghost233.logtool.logtoolconfig.LogToolConfigOSLog

class App : Application() {
    private val TAG by lazy { this::class.java.simpleName }

    override fun onCreate() {
        super.onCreate()

        initBaseConfig()
        Utils.init(this)
        initLogTool()
        LogTool.writeDebug(TAG, "init")
    }

    private fun initLogTool() {
        val allLogTypes = mutableListOf(
            LogProtocol.LogType.Init,
            LogProtocol.LogType.Debug,
            LogProtocol.LogType.CRASH,
            LogProtocol.LogType.Network,
            LogProtocol.LogType.Unknown,
        )
        val oslogConfig = LogToolConfigOSLog()
        oslogConfig.logTypeSet.addAll(allLogTypes)
        LogTool.addConfig(oslogConfig)
    }

    private fun initBaseConfig() {
        me.ghost233.gbase.BaseConfig.VERSION_NAME = BuildConfig.VERSION_NAME
        me.ghost233.gbase.BaseConfig.VERSION_CODE = BuildConfig.VERSION_CODE
    }
}
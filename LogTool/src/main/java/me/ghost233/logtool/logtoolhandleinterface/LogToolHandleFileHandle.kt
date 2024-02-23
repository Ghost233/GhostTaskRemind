package me.ghost233.logtool.logtoolhandleinterface

import me.ghost233.logtool.LogProtocol
import me.ghost233.logtool.logtoolconfig.LogToolConfigFile
import me.ghost233.logtool.logtoolconfig.LogToolConfigInterface
import me.ghost233.logtool.logtoolhandleinterface.logfilechandle.LogFileBridge
import me.ghost233.logtool.logtoolhandleinterface.logfilechandle.LogFileCTypeBridge
import me.ghost233.logtool.logtoolhandleinterface.logfilechandle.LogFileMmapBridge
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.Executors

class LogToolHandleFileHandle : LogToolHandleInterface() {
    companion object {
        const val TAG = "LogToolHandleFileHandle"

        private val mmapEnable = true;
    }

    private var printerExecutor = Executors.newSingleThreadExecutor()

    private var cHandleBridge: LogFileBridge? = null
    private var _config: LogToolConfigFile? = null

    override fun initWithConfig(tempConfig: LogToolConfigInterface): LogToolHandleInterface {
        this.isInit = true
        _config = tempConfig as LogToolConfigFile
        return this
    }

    override fun repairAllMMAP() {
        val fileList = this.getFileList()
        for (filename in fileList) {
            if (mmapEnable) {
                LogFileMmapBridge.repair(filename)
            } else {
                LogFileCTypeBridge.repair(filename)
            }
        }
    }

    fun getNowFilename(): String {
        if (cHandleBridge == null) {
            return ""
        }
        return cHandleBridge!!.getNowFilename()
    }

    fun getFileList(): ArrayList<String> {
        val array = ArrayList<String>()
        val path = _config!!.dirPath
        val fileManager = java.io.File(path)
        val tempArray = fileManager.list()
        val nowFilename = this.getNowFilename()
        for (fileName in tempArray!!) {
            val tempFilename = path + fileName
            if (tempFilename.endsWith(".txt")) {
                if (tempFilename != nowFilename) {
                    array.add(tempFilename)
                }
            }
        }
        return array
    }

    override fun writeLog(logType: LogProtocol.LogType, tag: String, msg: String) {
        if (cHandleBridge == null) {
            return
        }
        val tempData = getLogData(logType, tag, msg);
        printerExecutor.execute {
            synchronized(this) {
                cHandleBridge!!.writeLog(tempData, 0, tempData.size.toLong())
            }
        }
    }

    private fun getLogData(
        logType: LogProtocol.LogType,
        tag: String,
        msg: String,
    ): ByteArray {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
        val t: String = format.format(Date())
        val logBean = LogProtocol.LogBean.newBuilder()
            .setLogType(logType)
            .setTag(tag)
            .setTime(t)
            .setMessage(msg)
            .setLogLevel(LogProtocol.LogLevel.I)
            .build()
        val output = logBean.toByteArray()
        val tempByteArrayHeader = ByteArray(6)
        tempByteArrayHeader[0] = 0x7F.toByte()
        tempByteArrayHeader[1] = 0x7E.toByte()
        val length = output.size
        tempByteArrayHeader[2] = (length / 256 / 256 / 256 % 256).toByte()
        tempByteArrayHeader[3] = (length / 256 / 256 % 256).toByte()
        tempByteArrayHeader[4] = (length / 256 % 256).toByte()
        tempByteArrayHeader[5] = (length % 256).toByte()

        return tempByteArrayHeader + output
    }

    override fun start() {
        synchronized(this) {
            if (cHandleBridge != null) {
                cHandleBridge!!.end()
                cHandleBridge = null
            }
            if (mmapEnable) {
                cHandleBridge = LogFileMmapBridge()
            } else {
                cHandleBridge = LogFileCTypeBridge()
            }
            cHandleBridge!!.createNewFileResponse = { filename, bridge ->
                _config?.nowFilenameBlock?.invoke(filename)
            }
            cHandleBridge!!.dirPath = _config!!.dirPath
            cHandleBridge!!.filenamePrefix = _config!!.filenamePrefix
            cHandleBridge!!.filenameSuffix = _config!!.filenameSuffix
            cHandleBridge!!.fileMaxSize = _config!!.fileMaxSize
            cHandleBridge!!.fileTimeIntervalWithSecond = _config!!.fileTimeIntervalWithSecond
            cHandleBridge!!.subDirPath = _config!!.subDirPath
            cHandleBridge!!.start()
        }
    }

    override fun stop() {
        if (cHandleBridge == null) {
            return
        }
        synchronized(this) {
            cHandleBridge!!.end()
        }
    }

    override fun renew() {
        if (cHandleBridge == null) {
            return
        }
        synchronized(this) {
            cHandleBridge!!.renew()
        }
    }

    override fun msg(msg: String) {
        if (cHandleBridge == null) {
            return
        }
        val msgData = msg.toByteArray()
        printerExecutor.execute {
            synchronized(this) {
                cHandleBridge!!.writeLog(msgData, 0, msgData.size.toLong())
            }
        }
    }

}
package me.ghost233.logtool.logtoolconfig

import me.ghost233.logtool.logtoolhandleinterface.LogToolHandleFileHandle
import me.ghost233.logtool.logtoolhandleinterface.LogToolHandleInterface

class LogToolConfigFile : LogToolConfigInterface() {

    var priority: Long = 0L
    var dirPath: String = ""
    var subDirPath: String = ""
    var filenamePrefix: String = ""
    var filenameSuffix: String = ""
    var fileMaxSize: Long = 0L
    var fileTimeIntervalWithSecond: Long = 0L

    ////定义一个block类型获取最新的文件名
    //typedef void(^GetNowFilenameBlock)(NSString* _Nonnull);
//    @property(nonatomic, copy) GetNowFilenameBlock nowFilenameBlock;

    var nowFilenameBlock: ((String) -> Unit)? = null

    init {
        synchronized(handleLock) {
            handle = LogToolHandleFileHandle()
        }
    }

    override var handle: LogToolHandleInterface
        get() {
            if (!field.isInit) {
                field.initWithConfig(this)
                field.repairAllMMAP()
            }
            return field
        }

    override fun getLogToolConfigEnum(): LogToolConfigEnum {
        return LogToolConfigEnum.LogToolConfigEnumFile
    }
}
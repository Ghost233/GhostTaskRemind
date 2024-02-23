package me.ghost233.logtool

class LogHelper {
    companion object {
        val shared = LogHelper()
    }

    init {
        System.loadLibrary("logtool")
        nativeInit()
    }

    external fun nativeInit()
}
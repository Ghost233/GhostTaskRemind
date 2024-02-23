package me.ghost233.gstorage

import android.content.Context
import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKVLogLevel
import me.ghost233.logtool.LogTool

class KVUtils {
    companion object {
        private const val TAG = "KVUtils"
        const val ID_DEFAULT = "id_car"
        private val sharedInstance = KVUtils()
        private var defaultMMKV: MMKV? = null
        private var initFlag = false

        const val KEY_FCM_TOKEN = "FCM_TOKEN"
        const val LastConnectBluetoothBean_KEY = "LastConnectBluetoothBean"

        fun initWithContext(context: Context) {
            // Log.d(TAG, "initWithContext")
            sharedInstance.initWithContext(context)
        }

        // PUT
        fun putString(key: String, value: String): Boolean {
            // Log.d(TAG, "putString $key $value")
            if (!initFlag) {
                LogTool.writeCrash(TAG, "DSUtils not init")
                return false
            }
            val result = defaultMMKV?.encode(key, value)
            // defaultMMKV?.sync()
            return result == true
        }

        fun putInt(key: String, value: Int): Boolean {
            if (!initFlag) {
                LogTool.writeCrash(TAG, "DSUtils not init")
                return false
            }
            val result = defaultMMKV?.encode(key, value)
            // Log.d(TAG, "putInt $key $value $result")
            // defaultMMKV?.sync()
            return result == true
        }

        fun putLong(key: String, value: Long): Boolean {
            if (!initFlag) {
                LogTool.writeCrash(TAG, "DSUtils not init")
                return false
            }
            val result = defaultMMKV?.encode(key, value)
            // defaultMMKV?.sync()
            return result == true
        }

        fun putFloat(key: String, value: Float): Boolean {
            if (!initFlag) {
                LogTool.writeCrash(TAG, "DSUtils not init")
                return false
            }
            val result = defaultMMKV?.encode(key, value)
            // defaultMMKV?.sync()
            return result == true
        }

        fun putDouble(key: String, value: Double): Boolean {
            if (!initFlag) {
                LogTool.writeCrash(TAG, "DSUtils not init")
                return false
            }
            val result = defaultMMKV?.encode(key, value)
            // defaultMMKV?.sync()
            return result == true
        }

        fun putBoolean(key: String, value: Boolean): Boolean {
            if (!initFlag) {
                LogTool.writeCrash(TAG, "DSUtils not init")
                return false
            }
            val result = defaultMMKV?.encode(key, value)
            // defaultMMKV?.sync()
            return result == true
        }

        fun putByteArray(key: String, value: ByteArray): Boolean {
            if (!initFlag) {
                LogTool.writeCrash(TAG, "DSUtils not init")
                return false
            }
            val result = defaultMMKV?.encode(key, value)
            // defaultMMKV?.sync()
            return result == true
        }

        fun putStringSet(key: String, value: Set<String>): Boolean {
            if (!initFlag) {
                LogTool.writeCrash(TAG, "DSUtils not init")
                return false
            }
            val result = defaultMMKV?.encode(key, value)
            // defaultMMKV?.sync()
            return result == true
        }

        // REMOVE
        fun remove(key: String) {
            if (!initFlag) {
                LogTool.writeCrash(TAG, "DSUtils not init")
                return
            }
            defaultMMKV?.removeValueForKey(key)
            // defaultMMKV?.sync()
        }

        // CLEAR
        fun clear() {
            // Log.d(TAG, "clear")
            if (!initFlag) {
                LogTool.writeCrash(TAG, "DSUtils not init")
                return
            }
            defaultMMKV?.clearAll()
            // defaultMMKV?.sync()
        }

        // GET
        fun getString(key: String, defaultValue: String): String {
            // Log.d(TAG, "getString $key")
            if (!initFlag) {
                LogTool.writeCrash(TAG, "DSUtils not init")
                return defaultValue
            }
            val result = defaultMMKV?.decodeString(key, defaultValue)
            return result ?: defaultValue
        }

        fun getInt(key: String, defaultValue: Int): Int {
            if (!initFlag) {
                LogTool.writeCrash(TAG, "DSUtils not init")
                return defaultValue
            }
            val result = defaultMMKV?.decodeInt(key, defaultValue)
            // Log.d(TAG, "getInt $key $result")
            return result ?: defaultValue
        }

        fun getLong(key: String, defaultValue: Long): Long {
            if (!initFlag) {
                LogTool.writeCrash(TAG, "DSUtils not init")
                return defaultValue
            }
            val result = defaultMMKV?.decodeLong(key, defaultValue)
            return result ?: defaultValue
        }

        fun getFloat(key: String, defaultValue: Float): Float {
            if (!initFlag) {
                LogTool.writeCrash(TAG, "DSUtils not init")
                return defaultValue
            }
            val result = defaultMMKV?.decodeFloat(key, defaultValue)
            return result ?: defaultValue
        }

        fun getDouble(key: String, defaultValue: Double): Double {
            if (!initFlag) {
                LogTool.writeCrash(TAG, "DSUtils not init")
                return defaultValue
            }
            val result = defaultMMKV?.decodeDouble(key, defaultValue)
            return result ?: defaultValue
        }

        fun getBoolean(key: String, defaultValue: Boolean): Boolean {
            if (!initFlag) {
                LogTool.writeCrash(TAG, "DSUtils not init")
                return defaultValue
            }
            val result = defaultMMKV?.decodeBool(key, defaultValue)
            return result ?: defaultValue
        }

        fun getByteArray(key: String, defaultValue: ByteArray): ByteArray {
            if (!initFlag) {
                LogTool.writeCrash(TAG, "DSUtils not init")
                return defaultValue
            }
            val result = defaultMMKV?.decodeBytes(key, defaultValue)
            return result ?: defaultValue
        }

        fun getStringSet(key: String, defaultValue: Set<String>): Set<String> {
            if (!initFlag) {
                LogTool.writeCrash(TAG, "DSUtils not init")
                return defaultValue
            }
            val result = defaultMMKV?.decodeStringSet(key, defaultValue)
            return result ?: defaultValue
        }

        fun getString(key: String): String? {
            // Log.d(TAG, "getString $key")
            if (!initFlag) {
                LogTool.writeCrash(TAG, "DSUtils not init")
                // Log.d(TAG, "getString $key not init")
                return null
            }
            val result = defaultMMKV?.decodeString(key)
            // Log.d(TAG, "getString $key $result")
            return result
        }

        fun getInt(key: String): Int? {
            if (!initFlag) {
                LogTool.writeCrash(TAG, "DSUtils not init")
                return null
            }
            return defaultMMKV?.decodeInt(key)
        }

        fun getLong(key: String): Long? {
            if (!initFlag) {
                LogTool.writeCrash(TAG, "DSUtils not init")
                return null
            }
            return defaultMMKV?.decodeLong(key)
        }

        fun getFloat(key: String): Float? {
            if (!initFlag) {
                LogTool.writeCrash(TAG, "DSUtils not init")
                return null
            }
            return defaultMMKV?.decodeFloat(key)
        }

        fun getDouble(key: String): Double? {
            if (!initFlag) {
                LogTool.writeCrash(TAG, "DSUtils not init")
                return null
            }

            return defaultMMKV?.decodeDouble(key)
        }

        fun getBoolean(key: String): Boolean? {
            if (!initFlag) {
                LogTool.writeCrash(TAG, "DSUtils not init")
                return null
            }
            return defaultMMKV?.decodeBool(key)
        }

        fun getByteArray(key: String): ByteArray? {
            if (!initFlag) {
                LogTool.writeCrash(TAG, "DSUtils not init")
                return null
            }
            return defaultMMKV?.decodeBytes(key)
        }

        fun getStringSet(key: String): Set<String>? {
            if (!initFlag) {
                LogTool.writeCrash(TAG, "DSUtils not init")
                return null
            }
            return defaultMMKV?.decodeStringSet(key)
        }

        // Key

        fun hasKey(key: String): Boolean {
            if (!initFlag) {
                LogTool.writeCrash(TAG, "DSUtils not init")
                return false
            }
//            defaultMMKV?.allKeys()?.forEach {
//                Log.d(TAG, "hasKey $it")
//            }
            return defaultMMKV?.containsKey(key) ?: false
        }
    }

    fun initWithContext(context: Context) {
        // Log.d(TAG, "initWithContext")
        try {
            if (BuildConfig.DEBUG) {
                MMKV.initialize(context, MMKVLogLevel.LevelDebug)
            } else {
                MMKV.initialize(context)
            }
            defaultMMKV = MMKV.mmkvWithID(ID_DEFAULT, MMKV.MULTI_PROCESS_MODE)
            initFlag = true
        } catch (e: Exception) {
            LogTool.writeCrash(TAG, "initWithContext $e")
        }
    }
}
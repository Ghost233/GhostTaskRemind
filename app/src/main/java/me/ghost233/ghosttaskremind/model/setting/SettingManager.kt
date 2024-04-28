package me.ghost233.ghosttaskremind.model.setting

import kotlinx.coroutines.flow.MutableStateFlow
import me.ghost233.ghosttaskremind.ui.setting.SettingModel
import me.ghost233.ghosttaskremind.ui.setting.SettingStatus
import me.ghost233.gstorage.KVUtils

class SettingManager {
    private val TAG by lazy { this::class.java.simpleName }

    companion object {
        val shared = SettingManager()

        private const val SETTING_KEY_SUFFIX = "SETTING_KEY"

        private const val BOOT_START_KEY = "${SETTING_KEY_SUFFIX}_boot_start"
        private const val FLOAT_WINDOW_KEY = "${SETTING_KEY_SUFFIX}_float_window"
        private const val NOTIFICATION_KEY = "${SETTING_KEY_SUFFIX}_notification"
        private const val WIDGET_KEY = "${SETTING_KEY_SUFFIX}_widget"
    }

    private val bootStartModel = SettingModel("开机启动", SettingStatus.UNKNOWN)
    private val floatWindowModel = SettingModel("悬浮窗", SettingStatus.UNKNOWN)
    private val notificationModel = SettingModel("常驻通知", SettingStatus.UNKNOWN)
    private val widgetModel = SettingModel("桌面小组件", SettingStatus.UNKNOWN)

    var bootStartStatus
        set(value) {
            bootStartModel.status = value
        }
        get() = bootStartModel.status

    var floatWindowStatus
        set(value) {
            floatWindowModel.status = value
        }
        get() = floatWindowModel.status

    var notificationStatus
        set(value) {
            notificationModel.status = value
        }
        get() = notificationModel.status

    var widgetStatus
        set(value) {
            widgetModel.status = value
        }
        get() = widgetModel.status

    private val settingList: MutableList<SettingModel> = mutableListOf(
        bootStartModel,
        floatWindowModel,
        notificationModel,
        widgetModel,
    )
    val settingListFlow = MutableStateFlow(settingList)

    init {
        val bootStartStatus = KVUtils.getInt(BOOT_START_KEY, SettingStatus.UNKNOWN.ordinal)
        val floatWindowStatus = KVUtils.getInt(FLOAT_WINDOW_KEY, SettingStatus.UNKNOWN.ordinal)
        val notificationStatus = KVUtils.getInt(NOTIFICATION_KEY, SettingStatus.UNKNOWN.ordinal)
        val widgetStatus = KVUtils.getInt(WIDGET_KEY, SettingStatus.UNKNOWN.ordinal)

        bootStartModel.status = SettingStatus.fromValue(bootStartStatus)
        floatWindowModel.status = SettingStatus.fromValue(floatWindowStatus)
        notificationModel.status = SettingStatus.fromValue(notificationStatus)
        widgetModel.status = SettingStatus.fromValue(widgetStatus)
    }

    fun saveSetting() {
        KVUtils.putInt(BOOT_START_KEY, bootStartModel.status.value)
        KVUtils.putInt(FLOAT_WINDOW_KEY, floatWindowModel.status.value)
        KVUtils.putInt(NOTIFICATION_KEY, notificationModel.status.value)
        KVUtils.putInt(WIDGET_KEY, widgetModel.status.value)
    }
}
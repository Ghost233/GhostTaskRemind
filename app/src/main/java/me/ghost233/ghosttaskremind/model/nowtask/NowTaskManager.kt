package me.ghost233.ghosttaskremind.model.nowtask

import android.widget.TextView
import com.blankj.utilcode.util.Utils
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.hjq.window.EasyWindow
import me.ghost233.ghosttaskremind.R
import me.ghost233.ghosttaskremind.model.setting.SettingManager
import me.ghost233.ghosttaskremind.model.taskhistory.TaskHistoryManager
import me.ghost233.ghosttaskremind.model.taskhistory.TaskHistoryModel
import me.ghost233.ghosttaskremind.model.tasktype.TaskTypeManager
import me.ghost233.ghosttaskremind.ui.setting.SettingStatus
import me.ghost233.gstorage.KVUtils

class NowTaskManager {
    companion object {
        private val TAG by lazy { NowTaskManager::class.java.simpleName }

        val shared = NowTaskManager()

        const val NOW_TASK_KEY = "now_task"
    }

    private var _nowTaskId: Long = -1
    var nowTaskId
        set(value) {
            _nowTaskId = value
            KVUtils.putLong(NOW_TASK_KEY, value)
            reloadNowTask()
        }
        get() = _nowTaskId

    var nowTaskHistoryModel: TaskHistoryModel? = null

    init {
        _nowTaskId = KVUtils.getLong(NOW_TASK_KEY, -1)
        reloadNowTask()
    }

    private fun reloadNowTask() {
        if (_nowTaskId != -1L) {
            nowTaskHistoryModel = TaskHistoryManager.shared.getTaskHistoryById(_nowTaskId)
        } else {
            nowTaskHistoryModel = null
        }

        nowTaskHistoryModel?.let { nowTaskHistoryModel ->
            TaskTypeManager.shared.getTaskTypeById(nowTaskHistoryModel.taskTypeId)
                ?.let { taskTypeModel ->
                    if (SettingManager.shared.floatWindowStatus == SettingStatus.ON) {
                        XXPermissions.with(Utils.getApp())
                            .permission(Permission.SYSTEM_ALERT_WINDOW)
                            .request { _, allGranted ->
                                if (allGranted) {
                                    EasyWindow.with(Utils.getApp())
                                        .setContentView(R.layout.layout_toast)
                                        .setDraggable()
                                        .setDuration(1000)
                                        // 设置动画样式
                                        //.setAnimStyle(android.R.style.Animation_Translucent)
                                        // 设置外层是否能被触摸
                                        //.setOutsideTouchable(false)
                                        // 设置窗口背景阴影强度
                                        //.setBackgroundDimAmount(0.5f)
                                        .setText(R.id.tv_name, taskTypeModel.name)
                                        .setOnClickListener(
                                            R.id.tv_name,
                                            EasyWindow.OnClickListener<TextView?> { easyWindow: EasyWindow<*>, view: TextView? ->
//                                        val intent =
//                                         easyWindow.startActivity(intent);
                                            })
                                        .show()
                                } else {
                                    SettingManager.shared.floatWindowStatus = SettingStatus.UNKNOWN
                                }
                            }
                    }
                }
        }
    }
}
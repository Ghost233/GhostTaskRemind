package me.ghost233.ghosttaskremind.ui.newtask

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.ghost233.ghosttaskremind.model.taskhistory.TaskHistoryManager
import me.ghost233.ghosttaskremind.model.taskhistory.TaskHistoryModel
import me.ghost233.gstorage.KVUtils

class NewTaskViewModel : ViewModel() {

    val isRunning = MutableLiveData<Boolean>().apply {
        value = KVUtils.getBoolean("task_running", false)
    }

    val nowRunningTaskId = MutableLiveData<Int>().apply {
        value = KVUtils.getInt("task_running_id", -1)
    }

    fun startNewTask(taskTypeId: Long) {
        val taskHistoryModel = TaskHistoryModel()
        taskHistoryModel.taskTypeId = taskTypeId
        taskHistoryModel.startTimestamp = System.currentTimeMillis()
        TaskHistoryManager.shared.addTaskHistory(taskHistoryModel)

        KVUtils.putBoolean("task_running", true)
        isRunning.value = true
    }

    fun stopTask() {
        KVUtils.putBoolean("task_running", false)
        isRunning.value = false
    }
}
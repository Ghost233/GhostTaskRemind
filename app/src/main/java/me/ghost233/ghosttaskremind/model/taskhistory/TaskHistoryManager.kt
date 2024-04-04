package me.ghost233.ghosttaskremind.model.taskhistory

import androidx.room.Room
import androidx.room.RoomDatabase
import com.blankj.utilcode.util.Utils
import com.tencent.wcdb.room.db.WCDBOpenHelperFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.ghost233.logtool.LogTool

class TaskHistoryManager {
    private val TAG by lazy { this::class.java.simpleName }

    companion object {
        val shared = TaskHistoryManager()
        private const val TASK_HISTORY = "task_history"
    }

    private val taskHistoryDatabase: TaskHistoryDatabase by lazy {
        val factory = WCDBOpenHelperFactory()
            .writeAheadLoggingEnabled(true) // 打开WAL以及读写并发，可以省略让Room决定是否要打开
            .asyncCheckpointEnabled(true) // 打开异步Checkpoint优化，不需要可以省略
        Room.databaseBuilder(Utils.getApp(), TaskHistoryDatabase::class.java, "$TAG.db")
            .openHelperFactory(factory)   // 重要：使用WCDB打开Room
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                    super.onCreate(db)
                    LogTool.writeDebug(TAG, "TaskHistoryDAO onCreate")
                }
            })
            .build()
    }
    private val taskHistoryDAO: TaskHistoryDAO by lazy {
        taskHistoryDatabase.mTaskHistoryDatabase()
    }

    private val taskHistory = mutableListOf<TaskHistoryModel>()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            taskHistory.addAll(taskHistoryDAO.getTaskHistoryList())
        }
    }

    fun addTaskHistory(taskHistoryModel: TaskHistoryModel) {
        CoroutineScope(Dispatchers.IO).launch {
            taskHistoryDAO.insertTaskHistoryModel(taskHistoryModel)
            taskHistory.add(taskHistoryModel)
        }
    }

    fun getTaskHistoryList(): List<TaskHistoryModel> {
        return taskHistory
    }

    fun getTaskHistoryById(id: Long): TaskHistoryModel? {
        return taskHistory.find { it.id == id }
    }

    fun updateTaskHistoryModel(taskHistoryModel: TaskHistoryModel) {
        CoroutineScope(Dispatchers.IO).launch {
            taskHistoryDAO.updateTaskHistoryModel(taskHistoryModel)
            val index = taskHistory.indexOfFirst { it.id == taskHistoryModel.id }
            if (index != -1) {
                taskHistory[index] = taskHistoryModel
            }
        }
    }
}
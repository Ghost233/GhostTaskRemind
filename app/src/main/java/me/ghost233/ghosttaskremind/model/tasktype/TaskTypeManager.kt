package me.ghost233.ghosttaskremind.model.tasktype

import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import androidx.room.RoomDatabase
import com.blankj.utilcode.util.Utils
import com.tencent.wcdb.room.db.WCDBOpenHelperFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.ghost233.logtool.LogTool


class TaskTypeManager {
    private val TAG by lazy { this::class.java.simpleName }

    companion object {
        val shared = TaskTypeManager()

        private const val TASK_TYPES = "task_types"
        private const val TASK_NOW_INDEX = "task_now_index"
    }

    private val taskTypeDatabase: TaskTypeDatabase by lazy {
        val factory = WCDBOpenHelperFactory()
            .writeAheadLoggingEnabled(true) // 打开WAL以及读写并发，可以省略让Room决定是否要打开
            .asyncCheckpointEnabled(true) // 打开异步Checkpoint优化，不需要可以省略
        Room.databaseBuilder(Utils.getApp(), TaskTypeDatabase::class.java, "$TAG.db")
            .openHelperFactory(factory)   // 重要：使用WCDB打开Room
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                    super.onCreate(db)
                    LogTool.writeDebug(TAG, "TaskTypeDatabase onCreate")
                    CoroutineScope(Dispatchers.IO).launch {
                        val taskTypeModel1 = TaskTypeModel()
                        taskTypeModel1.name = "学习"
                        taskTypeModel1.state = 1
                        taskTypeDAO.insertTaskTypeModel(taskTypeModel1)
                        val taskTypeModel2 = TaskTypeModel()
                        taskTypeModel2.name = "工作"
                        taskTypeModel2.state = 1
                        taskTypeDAO.insertTaskTypeModel(taskTypeModel2)
                    }
                }
            })
            .build()
    }
    private val taskTypeDAO: TaskTypeDAO by lazy {
        taskTypeDatabase.mTaskTypeDatabase()
    }

    //    private val _taskTypeList =
//        MutableLiveData<MutableList<TaskTypeModel>>().apply { value = mutableListOf() }
    private val _taskTypeList = mutableListOf<TaskTypeModel>()
    val taskTypeList = MutableLiveData<List<TaskTypeModel>>().apply { value = _taskTypeList }

    init {
        taskTypeDAO
        LogTool.writeDebug(TAG, "TaskTypeManager init")
        CoroutineScope(Dispatchers.IO).launch {
            _taskTypeList.addAll(_getTaskTypeList())
        }
    }

    private suspend fun _getTaskTypeList(): List<TaskTypeModel> = withContext(Dispatchers.IO) {
        return@withContext taskTypeDAO.getTaskTypeList()
    }

    fun getTaskTypeList(): List<TaskTypeModel> = _taskTypeList

    suspend fun addTaskType(name: String): Long = withContext(Dispatchers.IO) {
        val taskTypeModel = TaskTypeModel()
        taskTypeModel.name = name
        taskTypeModel.state = 1
        val result = taskTypeDAO.insertTaskTypeModel(taskTypeModel)
        taskTypeModel.id = result
        _taskTypeList.add(taskTypeModel)
        return@withContext result
    }

    suspend fun disableTaskType(id: Long) = withContext(Dispatchers.IO) {
        return@withContext taskTypeDAO.disableTaskType(id)
    }

    fun getTaskTypeById(taskTypeId: Long): TaskTypeModel? {
        return _taskTypeList.find { it.id == taskTypeId }
    }
}
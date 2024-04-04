package me.ghost233.ghosttaskremind.model.tasktype

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TaskTypeModel::class], version = 1)
abstract class TaskTypeDatabase : RoomDatabase() {
    abstract fun mTaskTypeDatabase(): TaskTypeDAO
}
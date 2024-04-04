package me.ghost233.ghosttaskremind.model.taskhistory

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TaskHistoryModel::class], version = 1)
abstract class TaskHistoryDatabase : RoomDatabase() {
    abstract fun mTaskHistoryDatabase(): TaskHistoryDAO
}
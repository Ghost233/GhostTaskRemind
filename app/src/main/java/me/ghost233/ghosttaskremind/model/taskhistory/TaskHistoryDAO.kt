package me.ghost233.ghosttaskremind.model.taskhistory

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskHistoryDAO {
    @Query("SELECT * FROM task_history")
    fun getTaskHistoryList(): List<TaskHistoryModel>

    @Query("SELECT * FROM task_history WHERE id = :id")
    fun getTaskHistoryById(id: Int): TaskHistoryModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTaskHistoryModel(taskHistoryModel: TaskHistoryModel): Long

    @Update
    fun updateTaskHistoryModel(taskHistoryModel: TaskHistoryModel): Int
}
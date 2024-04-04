package me.ghost233.ghosttaskremind.model.taskhistory

import androidx.databinding.BaseObservable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_history")
class TaskHistoryModel : BaseObservable() {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0

    @ColumnInfo(name = "task_type_id")
    var taskTypeId: Long = 0

    @ColumnInfo(name = "start_time")
    var startTimestamp: Long = 0L

    @ColumnInfo(name = "end_time")
    var endTimestamp: Long = 0L
}
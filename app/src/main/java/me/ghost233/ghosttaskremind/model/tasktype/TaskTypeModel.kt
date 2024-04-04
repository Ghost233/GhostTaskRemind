package me.ghost233.ghosttaskremind.model.tasktype

import androidx.databinding.BaseObservable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_type")
class TaskTypeModel : BaseObservable() {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0

    @ColumnInfo(name = "name")
    var name: String = ""

    //    0关闭, 1启用
    @ColumnInfo(name = "state")
    var state: Int = 0

    override fun toString(): String {
        return "TaskTypeModel(id=$id, name='$name', state=$state)"
    }
}
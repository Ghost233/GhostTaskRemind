package me.ghost233.ghosttaskremind.model.tasktype

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface TaskTypeDAO {
    @Query("SELECT * FROM task_type where state = 1")
    fun getTaskTypeList(): List<TaskTypeModel>

    /**
     * 插入数据，onConflict = OnConflictStrategy.REPLACE表明若存在主键相同的情况则直接覆盖
     * 返回的long表示的是插入项新的id
     * @param person
     * @return
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTaskTypeModel(taskTypeModel: TaskTypeModel): Long

    /**
     * 更新数据，这意味着可以指定id然后传入新的person对象进行更新
     * 返回的long表示更新的行数
     * @param person
     * @return
     */
    @Update
    fun updateTaskTypeModel(taskTypeModel: TaskTypeModel): Int

    /**
     * 删除数据，根据传入实体的主键进行数据的删除。
     * 也可以返回long型数据，表明从数据库中删除的行数
     * @param person
     * @return
     */
    @Delete
    fun deleteTaskTypeModel(taskTypeModel: TaskTypeModel): Int

    @Query("UPDATE task_type SET state = 0 WHERE id = :id")
    fun disableTaskType(id: Long): Int
}
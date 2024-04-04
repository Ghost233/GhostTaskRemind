package me.ghost233.ghosttaskremind.ui.tasklist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import me.ghost233.ghosttaskremind.databinding.LayoutTaskListRecycleViewSubviewBinding
import me.ghost233.ghosttaskremind.model.taskhistory.TaskHistoryModel
import me.ghost233.ghosttaskremind.model.tasktype.TaskTypeManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskListFragmentAdapter : BaseQuickAdapter<TaskHistoryModel, TaskListFragmentAdapter.VH>() {
    class VH(
        parent: ViewGroup,
        val binding: LayoutTaskListRecycleViewSubviewBinding = LayoutTaskListRecycleViewSubviewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

    override fun onBindViewHolder(holder: VH, position: Int, item: TaskHistoryModel?) {
        item?.let {
            val taskType = TaskTypeManager.shared.getTaskTypeById(it.taskTypeId)
            holder.binding.tvTaskTypeName.text = taskType?.name
            val costTime = it.endTimestamp - it.startTimestamp
            val costTimeString = timeToDate(costTime)
            holder.binding.tvCostTime.text = costTimeString
            holder.binding.tvStartTime.text = timestampToDate(it.startTimestamp)
            holder.binding.tvEndTime.text = timestampToDate(it.endTimestamp)
        }
    }

    fun timeToDate(time: Long): String {
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return sdf.format(Date(time))
    }

    fun timestampToDate(time: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date(time))
    }
}
package me.ghost233.ghosttaskremind.ui.newtask

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import me.ghost233.ghosttaskremind.databinding.LayoutAddNewTaskRecycleViewSubviewBinding
import me.ghost233.ghosttaskremind.model.tasktype.TaskTypeModel

class NewTaskFragmentAdapter : BaseQuickAdapter<TaskTypeModel, NewTaskFragmentAdapter.VH>() {
    class VH(
        parent: ViewGroup,
        val binding: LayoutAddNewTaskRecycleViewSubviewBinding = LayoutAddNewTaskRecycleViewSubviewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

    override fun onBindViewHolder(holder: VH, position: Int, item: TaskTypeModel?) {
        holder.binding.tvName.text = item?.name
    }
}
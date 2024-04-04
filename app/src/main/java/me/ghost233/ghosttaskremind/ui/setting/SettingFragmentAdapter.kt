package me.ghost233.ghosttaskremind.ui.setting

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import me.ghost233.ghosttaskremind.databinding.LayoutSettingRecycleViewSubviewBinding

class SettingFragmentAdapter : BaseQuickAdapter<SettingModel, SettingFragmentAdapter.VH>() {
    class VH(
        parent: ViewGroup,
        val binding: LayoutSettingRecycleViewSubviewBinding = LayoutSettingRecycleViewSubviewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

    override fun onBindViewHolder(holder: VH, position: Int, item: SettingModel?) {
        holder.binding.tvName.text = item?.name
        when (item?.status) {
            SettingStatus.ON -> {
                holder.binding.tvStatus.visibility = View.VISIBLE
                holder.binding.tvStatus.text = "✓"
            }

            SettingStatus.OFF -> {
                holder.binding.tvStatus.visibility = View.VISIBLE
                holder.binding.tvStatus.text = "×"
            }

            SettingStatus.UNKNOWN -> {
                holder.binding.tvStatus.visibility = View.VISIBLE
                holder.binding.tvStatus.text = "?"

            }

            SettingStatus.None -> {
                holder.binding.tvStatus.visibility = View.GONE
            }

            null -> {
            }
        }
    }
}
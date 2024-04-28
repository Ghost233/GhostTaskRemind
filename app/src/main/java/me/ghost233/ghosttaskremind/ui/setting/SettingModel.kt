package me.ghost233.ghosttaskremind.ui.setting

import androidx.recyclerview.widget.DiffUtil

enum class SettingStatus(val value: Int) {
    UNKNOWN(0),
    ON(1),
    OFF(2),
    None(99),
    ;

    companion object {
        fun fromValue(value: Int): SettingStatus {
            return when (value) {
                0 -> UNKNOWN
                1 -> ON
                2 -> OFF
                else -> None
            }
        }
    }
}

data class SettingModel(
    val name: String,
    var status: SettingStatus,
)

class SettingModelDiffCallback : DiffUtil.ItemCallback<SettingModel>() {
    override fun areItemsTheSame(oldItem: SettingModel, newItem: SettingModel): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: SettingModel, newItem: SettingModel): Boolean {
        if (oldItem.status != newItem.status) {
            return false
        }
        if (oldItem.name != newItem.name) {
            return false
        }
        return true
    }
}
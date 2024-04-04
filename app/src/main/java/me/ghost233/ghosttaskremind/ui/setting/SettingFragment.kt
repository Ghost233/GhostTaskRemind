package me.ghost233.ghosttaskremind.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import me.ghost233.ghosttaskremind.databinding.FragmentSettingBinding
import me.ghost233.ghosttaskremind.model.setting.SettingManager

class SettingFragment : Fragment() {

    private var binding: FragmentSettingBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val settingViewModel =
            ViewModelProvider(this).get(SettingViewModel::class.java)

        val tempBinding = FragmentSettingBinding.inflate(inflater, container, false)
        binding = tempBinding

        val adapter = SettingFragmentAdapter()
        SettingManager.shared.settingListLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        }

        adapter.setOnItemClickListener { _, _, position ->
            when (position) {
                0 -> {
                }

                1 -> {
                    XXPermissions.with(this)
                        .permission(Permission.SYSTEM_ALERT_WINDOW)
                        .request { _, allGranted ->
                            if (allGranted) {
                                when (SettingManager.shared.floatWindowStatus) {
                                    SettingStatus.UNKNOWN -> {
                                        SettingManager.shared.floatWindowStatus = SettingStatus.ON
                                    }

                                    SettingStatus.ON -> {
                                        SettingManager.shared.floatWindowStatus = SettingStatus.OFF
                                    }

                                    SettingStatus.OFF -> {
                                        SettingManager.shared.floatWindowStatus = SettingStatus.ON
                                    }

                                    SettingStatus.None -> {

                                    }
                                }
                            } else {
                                SettingManager.shared.floatWindowStatus = SettingStatus.UNKNOWN
                            }
                        }
                }

                2 -> {
                    XXPermissions.with(this)
                        .permission(Permission.POST_NOTIFICATIONS)
                        .request { _, allGranted ->
                            if (allGranted) {
                                when (SettingManager.shared.notificationStatus) {
                                    SettingStatus.UNKNOWN -> {
                                        SettingManager.shared.notificationStatus = SettingStatus.ON
                                    }

                                    SettingStatus.ON -> {
                                        SettingManager.shared.notificationStatus = SettingStatus.OFF
                                    }

                                    SettingStatus.OFF -> {
                                        SettingManager.shared.notificationStatus = SettingStatus.ON
                                    }

                                    SettingStatus.None -> {

                                    }
                                }
                            } else {
                                SettingManager.shared.notificationStatus =
                                    SettingStatus.UNKNOWN
                            }
                        }
                }


                3 -> {
                }
            }
        }

        tempBinding.recycleviewRoot.layoutManager = LinearLayoutManager(context)
        tempBinding.recycleviewRoot.adapter = adapter

        return tempBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
package me.ghost233.ghosttaskremind.ui.newtask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.ghost233.ghosttaskremind.databinding.FragmentNewTaskBinding
import me.ghost233.ghosttaskremind.model.tasktype.TaskTypeManager
import me.ghost233.logtool.LogTool

class NewTaskFragment : Fragment() {
    private val TAG by lazy { this::class.java.simpleName }

    private var binding: FragmentNewTaskBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val newTaskViewModel = ViewModelProvider(this).get(NewTaskViewModel::class.java)

        val tempBinding = FragmentNewTaskBinding.inflate(inflater, container, false)
        binding = tempBinding
        val root: View = tempBinding.root

        CoroutineScope(Dispatchers.IO).launch {
            newTaskViewModel.isRunning.collect {
                LogTool.writeDebug(TAG, "isRunning: $it")
                binding?.let { binding ->
                    if (it) {
                        binding.layoutAddNewTask.visibility = View.GONE
                        binding.layoutRunningTask.visibility = View.VISIBLE
                    } else {
                        binding.layoutAddNewTask.visibility = View.VISIBLE
                        binding.layoutRunningTask.visibility = View.GONE
                    }
                }
            }
        }

        tempBinding.recycleviewAddNewTask.layoutManager = GridLayoutManager(context, 4)
        val adapter = NewTaskFragmentAdapter()
        tempBinding.recycleviewAddNewTask.adapter = adapter
        adapter.submitList(TaskTypeManager.shared.getTaskTypeList())
        adapter.setOnItemClickListener { _, _, position ->
            adapter.getItem(position)?.let { taskType ->
                newTaskViewModel.startNewTask(taskType.id)
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            TaskTypeManager.shared.taskTypeList.collect {
                adapter.submitList(it)
                for (i in it) {
                    LogTool.writeDebug(TAG, "$i")
                }
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
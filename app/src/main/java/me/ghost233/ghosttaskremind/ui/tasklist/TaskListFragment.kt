package me.ghost233.ghosttaskremind.ui.tasklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import me.ghost233.ghosttaskremind.databinding.FragmentTaskListBinding

class TaskListFragment : Fragment() {

    private var _binding: FragmentTaskListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val taskListViewModel = ViewModelProvider(this).get(TaskListViewModel::class.java)

        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val adapter = TaskListFragmentAdapter()
//        adapter.submitList(taskListViewModel.taskHistoryList)
        binding.recycleView.layoutManager = GridLayoutManager(context, 4)
        binding.recycleView.adapter = adapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

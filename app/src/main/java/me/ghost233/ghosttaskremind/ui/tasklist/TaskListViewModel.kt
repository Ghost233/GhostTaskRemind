package me.ghost233.ghosttaskremind.ui.tasklist

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class TaskListViewModel : ViewModel() {

    val text = MutableStateFlow("This is home Fragment")
}
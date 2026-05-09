package com.example.lab08.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab08.data.Task
import com.example.lab08.data.TaskDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel(
    private val dao: TaskDao
) : ViewModel() {

    private val _tasks =
        MutableStateFlow<List<Task>>(emptyList())

    val tasks: StateFlow<List<Task>> = _tasks

    init {
        loadTasks()
    }

    fun loadTasks() {
        viewModelScope.launch {
            _tasks.value = dao.getAllTasks()
        }
    }

    fun addTask(description: String) {

        if (description.isBlank()) return

        viewModelScope.launch {

            dao.insertTask(
                Task(description = description)
            )

            loadTasks()
        }
    }

    fun toggleTaskCompletion(task: Task) {

        viewModelScope.launch {

            dao.updateTask(
                task.copy(
                    isCompleted = !task.isCompleted
                )
            )

            loadTasks()
        }
    }

    fun deleteTask(task: Task) {

        viewModelScope.launch {

            dao.deleteTask(task)

            loadTasks()
        }
    }

    fun deleteAllTasks() {

        viewModelScope.launch {

            dao.deleteAllTasks()

            loadTasks()
        }
    }
}
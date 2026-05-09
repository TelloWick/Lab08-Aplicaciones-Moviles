package com.example.lab08.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab08.data.dao.TaskDao
import com.example.lab08.data.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel(private val dao: TaskDao) : ViewModel() {

    // Estado de la lista de tareas
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())

    val tasks: StateFlow<List<Task>> = _tasks

    init {
        // Cargar tareas al iniciar
        viewModelScope.launch {
            _tasks.value = dao.getAllTasks()
        }
    }

    // Agregar tarea
    fun addTask(description: String) {

        val newTask = Task(description = description)

        viewModelScope.launch {
            dao.insertTask(newTask)
            _tasks.value = dao.getAllTasks()
        }
    }

    // Cambiar estado de completado
    fun toggleTaskCompletion(task: Task) {

        viewModelScope.launch {

            val updatedTask =
                task.copy(isCompleted = !task.isCompleted)

            dao.updateTask(updatedTask)

            _tasks.value = dao.getAllTasks()
        }
    }

    // Eliminar tareas
    fun deleteAllTasks() {

        viewModelScope.launch {

            dao.deleteAllTasks()

            _tasks.value = emptyList()
        }
    }
}
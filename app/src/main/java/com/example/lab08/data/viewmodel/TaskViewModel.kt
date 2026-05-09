package com.example.lab08.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab08.data.dao.TaskDao
import com.example.lab08.data.model.Task
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(private val dao: TaskDao) : ViewModel() {

    // Estado de la UI: Expone la lista de tareas de forma reactiva (UDF)
    val tasks = dao.getAllTasks().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Función para añadir una tarea
    fun addTask(description: String) {
        if (description.isNotBlank()) {
            viewModelScope.launch {
                dao.insertTask(Task(description = description))
            }
        }
    }

    // Función para marcar como completada/pendiente (Punto adicional 1 - Editar)
    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            dao.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }

    // Función para eliminar una tarea (Punto adicional 2)
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            dao.deleteTask(task)
        }
    }

    // Función para eliminar todo
    fun deleteAllTasks() {
        viewModelScope.launch {
            dao.deleteAllTasks()
        }
    }
}
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

    // LISTA DE TAREAS
    private val _tasks =
        MutableStateFlow<List<Task>>(emptyList())

    val tasks: StateFlow<List<Task>> = _tasks

    // FILTROS
    private val _filter =
        MutableStateFlow("Todas")

    val filter: StateFlow<String> = _filter

    init {
        loadTasks()
    }

    // CARGAR TAREAS
    fun loadTasks() {

        viewModelScope.launch {

            _tasks.value =
                dao.getAllTasks()
        }
    }

    // AGREGAR TAREA
    fun addTask(
        description: String,
        priority: String
    ) {

        if (description.isBlank()) return

        viewModelScope.launch {

            dao.insertTask(
                Task(
                    description = description,
                    priority = priority
                )
            )

            loadTasks()
        }
    }

    // COMPLETAR TAREA
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

    // EDITAR TAREA
    fun editTask(
        task: Task,
        newDescription: String,
        newPriority: String
    ) {

        if (newDescription.isBlank()) return

        viewModelScope.launch {

            dao.updateTask(
                task.copy(
                    description = newDescription,
                    priority = newPriority
                )
            )

            loadTasks()
        }
    }

    // ELIMINAR TAREA
    fun deleteTask(task: Task) {

        viewModelScope.launch {

            dao.deleteTask(task)

            loadTasks()
        }
    }

    // ELIMINAR TODAS
    fun deleteAllTasks() {

        viewModelScope.launch {

            dao.deleteAllTasks()

            loadTasks()
        }
    }

    // CAMBIAR FILTRO
    fun changeFilter(
        newFilter: String
    ) {

        _filter.value = newFilter
    }
}
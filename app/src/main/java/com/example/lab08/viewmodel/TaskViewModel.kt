package com.example.lab08.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab08.data.FirebaseManager
import com.example.lab08.data.Task
import com.example.lab08.data.TaskDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel(
    private val dao: TaskDao
) : ViewModel() {

    // FIREBASE
    private val firebaseManager =
        FirebaseManager()

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

            val task = Task(
                description = description,
                priority = priority
            )

            dao.insertTask(task)

            // GUARDAR EN FIREBASE
            firebaseManager.saveTask(task)

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

            val updatedTask = task.copy(
                description = newDescription,
                priority = newPriority
            )

            dao.updateTask(updatedTask)

            // ACTUALIZAR EN FIREBASE
            firebaseManager.saveTask(updatedTask)

            loadTasks()
        }
    }

    // ELIMINAR TAREA
    fun deleteTask(task: Task) {

        viewModelScope.launch {

            dao.deleteTask(task)

            // ELIMINAR EN FIREBASE
            firebaseManager.deleteTask(task)

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
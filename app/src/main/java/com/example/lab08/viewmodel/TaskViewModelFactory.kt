package com.example.lab08.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lab08.data.TaskDao

class TaskViewModelFactory(
    private val dao: TaskDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        return TaskViewModel(dao) as T
    }
}
package com.example.lab08

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.lab08.data.TaskDatabase
import com.example.lab08.ui.TaskScreen
import com.example.lab08.ui.theme.Lab08Theme
import com.example.lab08.viewmodel.TaskViewModel
import com.example.lab08.viewmodel.TaskViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val database = Room.databaseBuilder(
            applicationContext,
            TaskDatabase::class.java,
            "task_database"
        )
            .fallbackToDestructiveMigration()
            .build()

        val dao = database.taskDao()

        setContent {

            Lab08Theme {

                val viewModel: TaskViewModel = viewModel(
                    factory = TaskViewModelFactory(dao)
                )

                TaskScreen(viewModel)
            }
        }
    }
}
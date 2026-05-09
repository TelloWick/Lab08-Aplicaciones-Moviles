package com.example.lab08

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.lab08.data.database.TaskDatabase
import com.example.lab08.ui.theme.Lab08Theme
import com.example.lab08.data.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab08Theme {
                // Crear la base de datos
                val db = Room.databaseBuilder(
                    applicationContext,
                    TaskDatabase::class.java,
                    "task_db"
                ).build()

                // Obtener DAO
                val taskDao = db.taskDao()

                // Crear ViewModel
                val viewModel = TaskViewModel(taskDao)

                // Mostrar pantalla
                TaskScreen(viewModel)
            }
        }
    }
}

@Composable
fun TaskScreen(viewModel: TaskViewModel) {

    // Observar tareas
    val tasks by viewModel.tasks.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    // Estado del TextField
    var newTaskDescription by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // Campo texto
        TextField(
            value = newTaskDescription,

            onValueChange = {
                newTaskDescription = it
            },

            label = {
                Text("Nueva tarea")
            },

            modifier = Modifier.fillMaxWidth()
        )

        // Botón agregar
        Button(

            onClick = {

                if (newTaskDescription.isNotEmpty()) {

                    viewModel.addTask(newTaskDescription)

                    newTaskDescription = ""
                }
            },

            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)

        ) {

            Text("Agregar tarea")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar tareas
        tasks.forEach { task ->

            Row(
                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                Text(text = task.description)

                Button(
                    onClick = {
                        viewModel.toggleTaskCompletion(task)
                    }
                ) {

                    Text(
                        if (task.isCompleted)
                            "Completada"
                        else
                            "Pendiente"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Eliminar tareas
        Button(

            onClick = {

                coroutineScope.launch {

                    viewModel.deleteAllTasks()
                }
            },

            modifier = Modifier.fillMaxWidth()

        ) {

            Text("Eliminar todas las tareas")
        }
    }
}


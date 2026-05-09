package com.example.lab08.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lab08.data.Task
import com.example.lab08.viewmodel.TaskViewModel

@Composable
fun TaskScreen(viewModel: TaskViewModel) {

    val tasks by viewModel.tasks.collectAsState()

    var newTask by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Mis Tareas",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = newTask,
            onValueChange = {
                newTask = it
            },
            label = {
                Text("Nueva tarea")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {

                if (newTask.isNotBlank()) {

                    viewModel.addTask(newTask)

                    newTask = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar tarea")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {

            items(tasks) { task ->

                TaskItem(
                    task = task,
                    onToggle = {
                        viewModel.toggleTaskCompletion(task)
                    },
                    onDelete = {
                        viewModel.deleteTask(task)
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                viewModel.deleteAllTasks()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Eliminar todas")
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = task.description,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text =
                    if (task.isCompleted)
                        "Completada"
                    else
                        "Pendiente"
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row {

                Button(
                    onClick = onToggle
                ) {

                    Text(
                        if (task.isCompleted)
                            "Marcar pendiente"
                        else
                            "Completar"
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedButton(
                    onClick = onDelete
                ) {
                    Text("Eliminar")
                }
            }
        }
    }
}
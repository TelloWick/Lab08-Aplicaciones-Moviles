package com.example.lab08.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.lab08.data.Task
import com.example.lab08.viewmodel.TaskViewModel

@Composable
fun TaskScreen(viewModel: TaskViewModel) {

    val tasks by viewModel.tasks.collectAsState()
    val filter by viewModel.filter.collectAsState()

    var newTask by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf("Media") }

    var editingTask by remember { mutableStateOf<Task?>(null) }
    var editText by remember { mutableStateOf("") }
    var editPriority by remember { mutableStateOf("Media") }

    val filteredTasks = when (filter) {
        "Pendientes" -> tasks.filter { !it.isCompleted }
        "Done" -> tasks.filter { it.isCompleted }
        else -> tasks
    }

    Surface(
        color = Color(0xFFF8F8F8),
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text(
                text = "Todoist Tasks",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF202020)
            )

            Text(
                text = "Organiza tus tareas con prioridades",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = newTask,
                onValueChange = { newTask = it },
                label = { Text("Nueva tarea") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Prioridad",
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                PriorityButton("Alta", selectedPriority) {
                    selectedPriority = "Alta"
                }

                PriorityButton("Media", selectedPriority) {
                    selectedPriority = "Media"
                }

                PriorityButton("Baja", selectedPriority) {
                    selectedPriority = "Baja"
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    viewModel.addTask(newTask, selectedPriority)
                    newTask = ""
                    selectedPriority = "Media"
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE44232)
                )
            ) {
                Text("Agregar tarea")
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Filtros",
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterButton("Todas", filter) {
                    viewModel.changeFilter("Todas")
                }

                FilterButton("Pendientes", filter) {
                    viewModel.changeFilter("Pendientes")
                }

                FilterButton("Done", filter) {
                    viewModel.changeFilter("Done")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(filteredTasks) { task ->
                    TaskItem(
                        task = task,
                        onToggle = {
                            viewModel.toggleTaskCompletion(task)
                        },
                        onDelete = {
                            viewModel.deleteTask(task)
                        },
                        onEdit = {
                            editingTask = task
                            editText = task.description
                            editPriority = task.priority
                        }
                    )
                }
            }

            OutlinedButton(
                onClick = {
                    viewModel.deleteAllTasks()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Eliminar todas")
            }
        }
    }

    if (editingTask != null) {
        AlertDialog(
            onDismissRequest = {
                editingTask = null
            },
            title = {
                Text("Editar tarea")
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = editText,
                        onValueChange = { editText = it },
                        label = { Text("Descripción") },
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Prioridad")

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        PriorityButton("Alta", editPriority) {
                            editPriority = "Alta"
                        }

                        PriorityButton("Media", editPriority) {
                            editPriority = "Media"
                        }

                        PriorityButton("Baja", editPriority) {
                            editPriority = "Baja"
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.editTask(
                            editingTask!!,
                            editText,
                            editPriority
                        )
                        editingTask = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE44232)
                    )
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        editingTask = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun TaskItem(
    task: Task,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = task.description,
                textDecoration =
                    if (task.isCompleted)
                        TextDecoration.LineThrough
                    else
                        TextDecoration.None,
                color =
                    if (task.isCompleted)
                        Color.Gray
                    else
                        Color.Black,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Estado: ${if (task.isCompleted) "Completada" else "Pendiente"}",
                color = Color.DarkGray
            )

            Text(
                text = "Prioridad: ${task.priority}",
                color = when (task.priority) {
                    "Alta" -> Color.Red
                    "Media" -> Color(0xFFFF9800)
                    else -> Color(0xFF4CAF50)
                },
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                Button(
                    onClick = onToggle,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE44232)
                    )
                ) {
                    Text(
                        if (task.isCompleted)
                            "Pendiente"
                        else
                            "Completar"
                    )
                }

                OutlinedButton(
                    onClick = onEdit,
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Editar")
                }

                OutlinedButton(
                    onClick = onDelete,
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Eliminar")
                }
            }
        }
    }
}

@Composable
fun FilterButton(
    text: String,
    selectedFilter: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor =
                if (selectedFilter == text)
                    Color(0xFFE44232)
                else
                    Color(0xFFBDBDBD)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(text)
    }
}

@Composable
fun PriorityButton(
    text: String,
    selectedPriority: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = if (selectedPriority == text) "✓ $text" else text
        )
    }
}
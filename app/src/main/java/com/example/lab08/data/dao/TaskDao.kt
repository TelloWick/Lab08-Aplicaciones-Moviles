package com.example.lab08.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.lab08.data.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    // Obtener todas las tareas (Usamos Flow para actualizaciones en tiempo real)
    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<Task>>

    // Insertar una nueva tarea
    @Insert
    suspend fun insertTask(task: Task)

    // Actualizar una tarea (Punto adicional 1: Editar tareas)
    @Update
    suspend fun updateTask(task: Task)

    // Eliminar una tarea individual (Punto adicional 2)
    @Delete
    suspend fun deleteTask(task: Task)

    // Eliminar todas las tareas
    @Query("DELETE FROM tasks")
    suspend fun deleteAllTasks()
}
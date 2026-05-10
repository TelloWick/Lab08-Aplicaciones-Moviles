package com.example.lab08.data

import com.google.firebase.firestore.FirebaseFirestore

class FirebaseManager {

    private val db =
        FirebaseFirestore.getInstance()

    private val tasksCollection =
        db.collection("tasks")

    fun saveTask(task: Task) {

        tasksCollection
            .document(task.id.toString())
            .set(task)
    }

    fun deleteTask(task: Task) {

        tasksCollection
            .document(task.id.toString())
            .delete()
    }
}
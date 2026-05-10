package com.example.lab08.data

import com.google.firebase.firestore.FirebaseFirestore

class FirebaseManager {

    private val db =
        FirebaseFirestore.getInstance()

    private val tasksCollection =
        db.collection("tasks")

    fun saveTask(task: Task) {

        // GENERAR ID AUTOMÁTICO EN FIREBASE
        tasksCollection.add(task)
    }

    fun deleteTask(task: Task) {

        tasksCollection
            .whereEqualTo("description", task.description)
            .get()
            .addOnSuccessListener { result ->

                for (document in result) {

                    tasksCollection
                        .document(document.id)
                        .delete()
                }
            }
    }
}
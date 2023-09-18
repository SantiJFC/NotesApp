package com.example.notesapp.repository

import com.example.notesapp.models.Notes
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


const val NOTES_COLLECTION_REF = "notes"

class StorageRepository() {
    fun user() = Firebase.auth.currentUser
    fun hasUser(): Boolean = Firebase.auth.currentUser != null

    fun getUserId(): String = Firebase.auth.currentUser?.uid.orEmpty()

    //Permite cargar los datos de la base de datos
    private val notesRef: CollectionReference =
        Firebase.firestore.collection(NOTES_COLLECTION_REF)


    fun getUserNotes(
        userId: String
    ): Flow<Resources<List<Notes>>> = callbackFlow {
        var snapshotStateListener: ListenerRegistration? = null

        try {
            snapshotStateListener = notesRef
                .orderBy("timestamp")
                .whereEqualTo("userId", userId)
                .addSnapshotListener { snapshot, e ->
                    val response = if (snapshot != null) {
                        //Se necesita devolver los datos del json de firebase que se modifica
                        val notes = snapshot.toObjects(Notes::class.java)
                        Resources.Success(data = notes)

                    } else {
                        Resources.Error(throwable = e?.cause)

                    }
                    trySend(response)

                }

        } catch (e: Exception) {
            trySend(Resources.Error(e.cause))
            e.printStackTrace()
        }
        awaitClose {
            snapshotStateListener?.remove()
        }
    }

    //Detalles de la nota (Opercaciones CRUD)
    fun getNote(
        noteId: String,
        onError: (Throwable?) -> Unit,
        onSuccess: (Notes?) -> Unit
    ) {
        //Se necesita capturar las referencias de las notas
        notesRef
            .document(noteId)
            .get()
            .addOnSuccessListener {
                onSuccess.invoke(it?.toObject(Notes::class.java))
            }
            .addOnFailureListener{ result ->
                onError.invoke(result.cause)
            }
    }

    fun addNote(
        userId: String,
        title: String,
        description: String,
        timestamp: Timestamp,
        color: Int = 0,
        onComplete: (Boolean) -> Unit,
    ) {
        val documentId = notesRef.document().id
        val note =
            Notes(userId, title, description, timestamp, colorIndex = color, documentId = documentId)
        notesRef
            .document(documentId)
            .set(note)
            .addOnCompleteListener { result -> onComplete.invoke(result.isSuccessful) }
    }

    //Eliminar la nota
    fun deleteNote(noteId:String, onComplete: (Boolean) -> Unit) {
        notesRef.document(noteId)
            .delete()
            .addOnCompleteListener {
                onComplete.invoke(it.isSuccessful)
            }
    }

    fun updateNote(
        title: String,
        note: String,
        color: Int,
        noteId: String,
        onResult: (Boolean) -> Unit
    ) {
        //Cargo los datos con un hashMap para hacer más fácil la llamada
        val updateData = hashMapOf<String, Any>(
            "colorIndex" to color,
            "description" to note,
            "title" to title,
        )
        notesRef.document(noteId)
            .update(updateData)
            .addOnCompleteListener {
                onResult(it.isSuccessful)
            }
    }

    fun signOut()= Firebase.auth.signOut()
}

sealed class Resources<T>(
    val data: T? = null,
    val throwable: Throwable? = null,
) {
    //Estas clases permiten gestionar el estado de consulta de deatos
    class Loading<T> : Resources<T>()
    class Success<T>(data: T?) : Resources<T>(data = data)
    class Error<T>(throwable: Throwable?) : Resources<T>(throwable = throwable)
}
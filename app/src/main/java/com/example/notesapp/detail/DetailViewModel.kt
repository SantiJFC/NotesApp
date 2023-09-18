package com.example.notesapp.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.notesapp.models.Notes
import com.example.notesapp.repository.StorageRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser


class DetailViewModel(
    private val repository: StorageRepository = StorageRepository()
) : ViewModel() {
    var detailUiState by mutableStateOf(DetailUiState())
        private set

    //Añado un nodo para ayudar a Firebase a guardar varias variables. Se comprueba que el usurio esté logeado
    private val hasUser: Boolean
        get() = repository.hasUser()

    //Se llama al usuario de Firebase para saber la id del usuario
    private val user: FirebaseUser?
        get() = repository.user()

    //Detalle del evento de actualización de estado
    fun onColorChange(colorIndex: Int) {
        detailUiState = detailUiState.copy(colorIndex = colorIndex)
    }

    fun onTitleChange(title: String) {
        detailUiState = detailUiState.copy(title = title)
    }

    fun onNoteChange(note: String) {
        detailUiState = detailUiState.copy(note = note)
    }

    fun addNote() {
        if (hasUser) {
            repository.addNote(
                //Se pasa una excepción si el ID del usuario es null
                userId = user!!.uid,
                title = detailUiState.title,
                description = detailUiState.note,
                color = detailUiState.colorIndex,
                timestamp = Timestamp.now()
            ) {
                //Añade los datos a Firestore
                detailUiState = detailUiState.copy(noteAddedStatus = it)
            }
        }
    }

    fun setEditFields(note: Notes) {
        detailUiState = detailUiState.copy(
            colorIndex = note.colorIndex,
            title = note.title,
            note = note.description
        )
    }

    fun getNote(noteId: String) {
        repository.getNote(
            noteId = noteId,
            onError = {}
        ) {
            detailUiState = detailUiState.copy(selectedNote = it)
            //Así reasigno la edición del elemento por sí mismo
            detailUiState.selectedNote?.let { it1 -> setEditFields(it1) }
        }
    }

    fun updateNote(
        noteId: String,
    ) {
        repository.updateNote(
            title = detailUiState.title,
            note = detailUiState.note,
            noteId = noteId,
            color = detailUiState.colorIndex,
        ) {
            detailUiState = detailUiState.copy(updateNoteStatus = it)
        }
    }

    //Elimino el estado de la nota
    fun resetNoteAddedStatus() {
        detailUiState = detailUiState.copy(
            noteAddedStatus = false,
            updateNoteStatus = false
        )
    }

    //Creo una nueva interfaz de usuario
    fun resetState(){
        detailUiState = DetailUiState()
    }
}

data class DetailUiState(
    val colorIndex: Int = 0,
    val title: String = "",
    val note: String = "",
    val noteAddedStatus: Boolean = false,
    val updateNoteStatus: Boolean = false,
    val selectedNote: Notes? = null
)
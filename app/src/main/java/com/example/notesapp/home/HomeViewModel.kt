package com.example.notesapp.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.models.Notes
import com.example.notesapp.repository.Resources
import com.example.notesapp.repository.StorageRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class HomeViewModel(
    private val repository: StorageRepository =StorageRepository()
) : ViewModel() {
    var homeUiState by mutableStateOf(HomeUiState())
    val user = repository.user()
    val hasUser: Boolean
        get() = repository.hasUser()

    private val userId: String
        get() = repository.getUserId()

    fun loadNotes() {
        if (hasUser) {
            if (userId.isNotBlank()) {
                getUserNotes(userId)
            } else {
                homeUiState =
                    homeUiState.copy(notesList = Resources.Error(throwable = Throwable(message = "El usuario no está iniciado")))
            }

        }
    }

    private fun getUserNotes(userId: String) = viewModelScope.launch {
        repository.getUserNotes(userId).collect() {
            homeUiState = homeUiState.copy(notesList = it)
        }
    }

    fun deleteNote(noteId:String) = repository.deleteNote(noteId) {
        homeUiState = homeUiState.copy(noteDeletedStatus = it)
    }

    fun signOut() = repository.signOut()
}

data class HomeUiState(
    val notesList: Resources<List<Notes>> = Resources.Loading(),
    val noteDeletedStatus: Boolean = false

    )
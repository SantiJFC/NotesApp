package com.example.notesapp.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.repository.AuthRepository
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {
    val currentUser = repository.currentUser
    val hasUser: Boolean
        get() = repository.hasUser()

    var loginUiState by mutableStateOf(LoginUiState())
        private set

    fun onUserNameChange(userName: String) {
        loginUiState = loginUiState.copy(userName = userName)
    }

    fun onPasswordNameChange(password: String) {
        loginUiState = loginUiState.copy(password = password)
    }

    fun onUserNameChangeSignup(userName: String) {
        loginUiState = loginUiState.copy(userNameSignUp = userName)
    }

    fun onPasswordChangeSignup(password: String) {
        loginUiState = loginUiState.copy(passwordSignUp = password)
    }

    fun onConfirmPasswordChange(password: String) {
        loginUiState = loginUiState.copy(confirmPasswordSignUp = password)
    }

    //Compruebo si los datos son válidos
    private fun validateLoginForm() =
        loginUiState.userName.isNotBlank() &&
                loginUiState.password.isNotBlank()

    private fun validateSignupForm() =
        loginUiState.userNameSignUp.isNotBlank() &&
                loginUiState.passwordSignUp.isNotBlank() &&
                loginUiState.confirmPasswordSignUp.isNotBlank()

    private val auth: FirebaseAuth = Firebase.auth
    var googleSignInSuccess by mutableStateOf(false)
        private set

    //Datos introducidos de los usuarios
    fun createUser(context: Context) = viewModelScope.launch {
        try {
            if (!validateSignupForm()) {
                throw IllegalArgumentException("El email y el password no pueden estar vacíos")
            }
            loginUiState = loginUiState.copy(isLoading = true)

            if (loginUiState.passwordSignUp
                != loginUiState.confirmPasswordSignUp
            ) {
                throw IllegalArgumentException(
                    "La contraseña no coincide"
                )
            }
            loginUiState = loginUiState.copy(signUpError = null)
            //Creación de usuarios
            repository.createUser(
                loginUiState.userNameSignUp,
                loginUiState.passwordSignUp
            )
            { isSuccessful ->
                if (isSuccessful) {
                    Toast.makeText(
                        context,
                        "Acceso correcto",
                        Toast.LENGTH_SHORT
                    ).show()
                    loginUiState = loginUiState.copy(isSuccessLogin = true)
                } else {
                    Toast.makeText(
                        context,
                        "Acceso incorrecto",
                        Toast.LENGTH_SHORT).show()
                    loginUiState = loginUiState.copy(isSuccessLogin = false)
                }
            }
        } catch (e: Exception) {
            loginUiState = loginUiState.copy(signUpError = e.localizedMessage)
            e.printStackTrace()
        } finally {
            loginUiState = loginUiState.copy(isLoading = false)
        }
    }

    fun loginUser(context: Context) = viewModelScope.launch {
        try {
            if (!validateLoginForm()) {
                throw IllegalArgumentException("El email y el password no pueden estar vacíos")
            }
            loginUiState = loginUiState.copy(isLoading = true)
            loginUiState = loginUiState.copy(loginError = null)

            //Acceso usuarios
            repository.login(
                loginUiState.userName,
                loginUiState.password)
            { isSuccessful ->
                if (isSuccessful) {
                    Toast.makeText(context, "Acceso correcto", Toast.LENGTH_SHORT).show()
                    loginUiState = loginUiState.copy(isSuccessLogin = true)
                } else {
                    Toast.makeText(context, "Acceso incorrecto", Toast.LENGTH_SHORT).show()
                    loginUiState = loginUiState.copy(isSuccessLogin = false)
                }
            }
        } catch (e: Exception) {
            loginUiState = loginUiState.copy(loginError = e.localizedMessage)
            e.printStackTrace()
        } finally {
            loginUiState = loginUiState.copy(isLoading = false)
        }
    }
    fun signInWithGoogleCredential(credential: AuthCredential, home: () -> Unit) =
        viewModelScope.launch {
            try {
                auth.signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("NotesApp", "LogIn con Google Exitoso!")
                            googleSignInSuccess = true
                            home()
                            googleSignInSuccess = false //Resetea el acceso

                        }
                    }
                    .addOnFailureListener {
                        Log.d("NotesApp", "Fallo al iniciar sesión con Google")
                    }
            } catch (ex: Exception) {
                Log.d("NotesApp", "Excepción al iniciar sesión con Google ${ex.localizedMessage}")
            }
        }
}

data class LoginUiState(
    val userName: String = "",
    val password: String = "",
    val userNameSignUp: String = "",
    val passwordSignUp: String = "",
    val confirmPasswordSignUp: String = "",
    val isLoading: Boolean = false,
    val isSuccessLogin: Boolean = false,
    val signUpError: String? = null,
    val loginError: String? = null,

    )
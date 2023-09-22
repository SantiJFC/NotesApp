package com.example.notesapp.login

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.notesapp.NestedRoutes
import com.example.notesapp.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

private const val EMAIL_VALIDATION_REGEX = "^(.+)@(.+).(.+)\$"

@Composable
fun LoginScreen(
    navController: NavController = rememberNavController(),
    loginViewModel: LoginViewModel? = null,
    onNavToHomePage: () -> Unit,
    onNavToSignUpPage: () -> Unit,
) {
    LaunchedEffect(loginViewModel?.googleSignInSuccess) {
        if (loginViewModel?.googleSignInSuccess == true) {
            navController.popBackStack(NestedRoutes.Login.name, inclusive = false)
        }
    }
    val loginUiState = loginViewModel?.loginUiState
    val isError = loginUiState?.loginError != null

    // Necesario el ID de Google para poder hacer Login
    val context = LocalContext.current
    val showPassword = remember { mutableStateOf(false) }

    fun isValidEmail(email: String): Boolean {
        return EMAIL_VALIDATION_REGEX.toRegex().matches(email)
    }

    // Gestion de Google
    val token =
        "202036634352-k09ebr35rpt63i93pjo7mbi106d75kmc.apps.googleusercontent.com"
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            loginViewModel?.signInWithGoogleCredential(credential) {
                navController.popBackStack(NestedRoutes.Login.name, inclusive = true)
            }
        } catch (ex: Exception) {
            Log.d("Notes App", "GoogleSignIn falló")
        }
    }

    // Contenido de la pantalla
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isScreenLandscape(LocalContext.current)) {
            // Orientación apaisada: imagen a la izquierda, datos a la derecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.notes_logo_app_blanco),
                    contentDescription = "Imagen inicio sesión",
                    modifier = Modifier
                        .size(230.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp, Color.Green, RoundedCornerShape(26.dp))
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = "Login",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary,
                    )

                    if (isError) {
                        Text(
                            text = loginUiState?.loginError ?: "Error desconocido",
                            color = Color.Red
                        )
                    }

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        value = loginUiState?.userName ?: "",
                        onValueChange = { loginViewModel?.onUserNameChange(it) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null
                            )
                        },
                        label = {
                            Text("Email")
                        },
                        trailingIcon = {
                            if (loginUiState?.userName?.isNotEmpty() == true) {
                                IconButton(onClick = { loginViewModel?.onUserNameChange("") }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_close_24),
                                        contentDescription = "Borrar usuario"
                                    )
                                }
                            }
                        },
                        isError = isError || (loginUiState?.userName?.isNotBlank() == true && !isValidEmail(
                            loginUiState.userName
                        ))
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        value = loginUiState?.password ?: "",
                        onValueChange = { loginViewModel?.onPasswordNameChange(it) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null
                            )
                        },
                        label = {
                            Text("Contraseña")
                        },

                        trailingIcon = {
                            if (showPassword.value) {
                                IconButton(onClick = { showPassword.value = false }) {
                                    Icon(
                                        imageVector = Icons.Filled.VisibilityOff,
                                        contentDescription = stringResource(id = R.string.oculta_contrasena)
                                    )
                                }
                            } else {
                                IconButton(onClick = { showPassword.value = true }) {
                                    Icon(
                                        imageVector = Icons.Filled.VisibilityOff,
                                        contentDescription = stringResource(id = R.string.mostrar_contrasena)
                                    )
                                }
                            }
                        },
                        visualTransformation =
                        if (showPassword.value) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        isError = isError
                    )

                    Button(onClick = { loginViewModel?.loginUser(context) }) {
                        Text(text = "Iniciar sesión")
                    }

                    Spacer(modifier = Modifier.size(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(modifier = Modifier, text = "¿No tienes cuenta?")
                        Spacer(modifier = Modifier.size(8.dp))
                        TextButton(onClick = { onNavToSignUpPage.invoke() }) {
                            Text(
                                text = "Regístrate",
                                color = Color.Red,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable {
                                val opciones = GoogleSignInOptions
                                    .Builder(
                                        GoogleSignInOptions.DEFAULT_SIGN_IN
                                    )
                                    .requestIdToken(token)
                                    .requestEmail()
                                    .build()
                                val googleSingInCliente = GoogleSignIn.getClient(context, opciones)
                                launcher.launch(googleSingInCliente.signInIntent)
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_google),
                            contentDescription = "Login con Google",
                            modifier = Modifier
                                .padding(10.dp)
                                .size(40.dp)
                        )
                        Text(
                            text = "Login con Google",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (loginUiState?.isLoading == true) {
                            CircularProgressIndicator()
                        }
                        LaunchedEffect(key1 = loginViewModel?.hasUser) {
                            if (loginViewModel?.hasUser == true) {
                                onNavToHomePage.invoke()
                            }
                        }
                    }
                }
            }
        } else {
            // Orientación vertical (predeterminada)
            Text(
                text = "Login",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary,
            )
            if (isError) {
                Text(
                    text = loginUiState?.loginError ?: "Error desconocido",
                    color = Color.Red
                )
            }
            Image(
                painter = painterResource(R.drawable.notes_logo_app_blanco),
                contentDescription = "Imagen inicio sesión",
                modifier = Modifier
                    .size(230.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, Color.Green, RoundedCornerShape(26.dp))
            )
            Spacer(modifier = Modifier.height(15.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                value = loginUiState?.userName ?: "",
                onValueChange = { loginViewModel?.onUserNameChange(it) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null
                    )
                },
                label = {
                    Text("Email")
                },
                trailingIcon = {
                    if (loginUiState?.userName?.isNotEmpty() == true) {
                        IconButton(onClick = { loginViewModel?.onUserNameChange("") }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_close_24),
                                contentDescription = "Borrar usuario"
                            )
                        }
                    }
                },
                isError = isError || (loginUiState?.userName?.isNotBlank() == true && !isValidEmail(
                    loginUiState.userName
                ))
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                value = loginUiState?.password ?: "",
                onValueChange = { loginViewModel?.onPasswordNameChange(it) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null
                    )
                },
                label = {
                    Text("Contraseña")
                },

                trailingIcon = {
                    if (showPassword.value) {
                        IconButton(onClick = { showPassword.value = false }) {
                            Icon(
                                imageVector = Icons.Filled.VisibilityOff,
                                contentDescription = stringResource(id = R.string.oculta_contrasena)
                            )
                        }
                    } else {
                        IconButton(onClick = { showPassword.value = true }) {
                            Icon(
                                imageVector = Icons.Filled.VisibilityOff,
                                contentDescription = stringResource(id = R.string.mostrar_contrasena)
                            )
                        }
                    }
                },
                visualTransformation =
                if (showPassword.value) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                isError = isError
            )
            Button(onClick = { loginViewModel?.loginUser(context) }) {
                Text(text = "Iniciar sesión")
            }

            Spacer(modifier = Modifier.size(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(modifier = Modifier, text = "¿No tienes cuenta?")
                Spacer(modifier = Modifier.size(8.dp))
                TextButton(onClick = { onNavToSignUpPage.invoke() }) {
                    Text(text = "Regístrate", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {
                        val opciones = GoogleSignInOptions
                            .Builder(
                                GoogleSignInOptions.DEFAULT_SIGN_IN
                            )
                            .requestIdToken(token)
                            .requestEmail()
                            .build()
                        val googleSingInCliente = GoogleSignIn.getClient(context, opciones)
                        launcher.launch(googleSingInCliente.signInIntent)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center

            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Login con Google",
                    modifier = Modifier
                        .padding(10.dp)
                        .size(40.dp)
                )
                Text(
                    text = "Login con Google",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                if (loginUiState?.isLoading == true) {
                    CircularProgressIndicator()
                }
                LaunchedEffect(key1 = loginViewModel?.hasUser) {
                    if (loginViewModel?.hasUser == true) {
                        onNavToHomePage.invoke()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val fakeNavController = rememberNavController()

    val mimicLoginViewModel: LoginViewModel? = null

    LoginScreen(
        navController = fakeNavController,
        loginViewModel = mimicLoginViewModel,
        onNavToHomePage = {},
        onNavToSignUpPage = {}
    )
}

private fun isScreenLandscape(context: Context): Boolean {
    val resources = context.resources
    val configuration = resources.configuration
    return configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}
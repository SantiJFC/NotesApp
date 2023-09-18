package com.example.notesapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.notesapp.R
import com.example.notesapp.NestedRoutes
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    LaunchedEffect(key1 = true) {
        delay(5000) //Con el SplashScreen y el delay permito que la conexión y actualización de datos se realice en segundo plano
        // Despues de 5 segundos, navegue a la pantalla principal
        if (FirebaseAuth.getInstance().currentUser != null) {
            // Si el usuario está logueado, navega directamente a Home
            navController.navigate(NestedRoutes.Main.name) {
                popUpTo(NestedRoutes.Splash.name) { inclusive = true }
            }
        } else {
            //Si el usuario no está logeado, navega a la pantalla de login
            navController.navigate(NestedRoutes.Login.name) {
                popUpTo(NestedRoutes.Splash.name) { inclusive = true }
            }
        }
    }
    Splash()
}

@Composable
fun Splash() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.black)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.notes_logo_app),
            contentDescription = "Logo NoteApp",
            Modifier.size(400.dp, 400.dp))
        Text(
            text = "Notes App",
            color = Color.White,
            fontSize = 50.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily.Cursive
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SpalshScreenPreview() {
    Splash()
}
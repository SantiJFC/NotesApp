package com.example.notesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notesapp.detail.DetailViewModel
import com.example.notesapp.home.HomeViewModel
import com.example.notesapp.login.LoginViewModel
import com.example.notesapp.ui.theme.NotesAppTheme
import com.google.firebase.analytics.FirebaseAnalytics



private lateinit var firebaseAnalytics: FirebaseAnalytics

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Lanzamiento de eventos personalizados en google Analytics
        val firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message","Integración Firebase completa")
        firebaseAnalytics.logEvent("InitScreen",bundle)

        setContent {

            val loginViewModel = viewModel(modelClass = LoginViewModel::class.java)
            val homeViewModel = viewModel(modelClass = HomeViewModel::class.java)
            val detailViewModel = viewModel(modelClass = DetailViewModel::class.java)

            NotesAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation(
                        loginViewModel = loginViewModel,
                        detailViewModel = detailViewModel,
                        homeViewModel = homeViewModel
                    )
                }
            }
        }
    }
}
package com.example.notesapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.notesapp.detail.DetailScreen
import com.example.notesapp.detail.DetailViewModel
import com.example.notesapp.home.Home
import com.example.notesapp.home.HomeViewModel
import com.example.notesapp.login.LoginScreen
import com.example.notesapp.login.LoginViewModel
import com.example.notesapp.login.SignUpScreen
import com.example.notesapp.screens.SplashScreen

enum class LoginRoutes {
    Signup,
    SignIn
}

enum class HomeRoutes {
    Home,
    Detail
}

enum class NestedRoutes {
    Splash,
    Main,
    Login
}

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    loginViewModel: LoginViewModel,
    detailViewModel: DetailViewModel,
    homeViewModel: HomeViewModel,

) {
    NavHost(
        navController = navController,
        startDestination = NestedRoutes.Splash.name // Se inicia en el SplashScreen
    ) {
        // Se añade el SplashScreen como primera ventana
        composable(route = NestedRoutes.Splash.name) {
            SplashScreen(navController = navController)
        }

        // Se añaden las ventanas para hacer las transiciones
        authGraph(navController, loginViewModel)
        homeGraph(navController = navController, detailViewModel, homeViewModel)
    }

}

fun NavGraphBuilder.authGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel
) {
    navigation(
        startDestination = LoginRoutes.SignIn.name,
        route = NestedRoutes.Login.name
    ) {
        composable(route = LoginRoutes.SignIn.name) {
            LoginScreen(
                onNavToHomePage = {
                    navController.navigate(NestedRoutes.Main.name) {
                        launchSingleTop = true //Permite generar una instancia
                        popUpTo(route = LoginRoutes.SignIn.name) {
                            inclusive = true
                        }
                    }
                },
                loginViewModel = loginViewModel
            ) {
                navController.navigate(LoginRoutes.Signup.name) {
                    launchSingleTop = true
                    popUpTo(LoginRoutes.Signup.name) {
                        inclusive = true
                    }
                }
            }
        }

        composable(route = LoginRoutes.Signup.name) {
            SignUpScreen(
                onNavToHomePage = {
                    navController.navigate(NestedRoutes.Main.name) {
                        popUpTo(LoginRoutes.Signup.name) {
                            inclusive = true
                        }
                    }
                },
                loginViewModel = loginViewModel

            ) {

                navController.navigate(LoginRoutes.SignIn.name)
            }
        }

    }
}

fun NavGraphBuilder.homeGraph(
    navController: NavHostController,
    detailViewModel: DetailViewModel,
    homeViewModel: HomeViewModel
) {
    navigation(
        startDestination = HomeRoutes.Home.name,
        route = NestedRoutes.Main.name
    ) {
        composable(route = HomeRoutes.Home.name) {
            Home(
                homeViewModel = homeViewModel,
                onNoteClick = { noteId ->
                    navController.navigate(HomeRoutes.Detail.name + "?id=$noteId") {
                        launchSingleTop = true
                    }
                },
                navToDetailPage = {
                    navController.navigate(HomeRoutes.Detail.name)
                }
            ) {
                //Operación para la salida del logeo
                navController.navigate(NestedRoutes.Login.name) {
                    launchSingleTop = true
                    popUpTo(0) {
                        inclusive = true
                    }
                }
            }
        }

        composable(
            route = HomeRoutes.Detail.name + "?id={id}", arguments = listOf(navArgument("id") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) { entry ->
            DetailScreen(
                detailViewModel = detailViewModel,
                noteId = entry.arguments?.getString("id") as String,
            ) {
                navController.navigateUp()

            }

        }
    }

}
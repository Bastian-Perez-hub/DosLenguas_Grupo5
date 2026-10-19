package com.example.doslenguasapp.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.doslenguasapp.ui.components.AppDrawerContent
import com.example.doslenguasapp.ui.screens.DetailScreen
import com.example.doslenguasapp.ui.screens.EditProfileScreen
import com.example.doslenguasapp.ui.screens.HomeScreen
import com.example.doslenguasapp.ui.screens.LoginScreen
import com.example.doslenguasapp.ui.screens.MyPhrasesScreen
import com.example.doslenguasapp.ui.screens.ProfileScreen
import com.example.doslenguasapp.ui.screens.RegisterScreen
import kotlinx.coroutines.launch

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Ruta en la que estamos ahora; sirve para marcar la opción activa del menú.
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // El menú solo se puede abrir (también deslizando) en estas pantallas.
    val drawerRoutes = listOf(Routes.HOME, Routes.MY_PHRASES, Routes.PROFILE)

    // Ir a una pantalla del menú sin apilar pantallas repetidas.
    fun goToMainDestination(route: String) {
        navController.navigate(route) {
            popUpTo(Routes.HOME)
            launchSingleTop = true
        }
    }

    val openMenu: () -> Unit = { scope.launch { drawerState.open() } }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = currentRoute in drawerRoutes,
        drawerContent = {
            AppDrawerContent(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    scope.launch { drawerState.close() }
                    goToMainDestination(route)
                },
                onLogout = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Routes.LOGIN
        ) {
            composable(Routes.LOGIN) {
                LoginScreen(
                    onGoToRegister = { navController.navigate(Routes.REGISTER) },
                    onLoginSuccess = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.REGISTER) {
                RegisterScreen(
                    onGoToLogin = { navController.popBackStack() },
                    onRegisterSuccess = { navController.popBackStack() }
                )
            }

            composable(Routes.HOME) {
                HomeScreen(
                    onMenuClick = openMenu,
                    onGoToMyPhrases = { goToMainDestination(Routes.MY_PHRASES) },
                    onOpenPhrase = { id -> navController.navigate(Routes.detail(id)) }
                )
            }

            composable(Routes.MY_PHRASES) {
                MyPhrasesScreen(
                    onMenuClick = openMenu,
                    onGoToHome = { goToMainDestination(Routes.HOME) }
                )
            }

            composable(Routes.PROFILE) {
                ProfileScreen(
                    onMenuClick = openMenu,
                    onLogout = {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.HOME) { inclusive = true }
                        }
                    },
                    onEditProfile = { navController.navigate(Routes.EDIT_PROFILE) }
                )
            }

            composable(Routes.EDIT_PROFILE) {
                EditProfileScreen(
                    onBack = { navController.popBackStack() },
                    onSaved = { navController.popBackStack() }
                )
            }

            composable(
                route = Routes.DETAIL,
                arguments = listOf(navArgument("phraseId") { type = NavType.IntType })
            ) {
                DetailScreen(onBack = { navController.popBackStack() })
            }

        }
    }
}
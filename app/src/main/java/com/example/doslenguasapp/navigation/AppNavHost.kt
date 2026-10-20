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
import com.example.doslenguasapp.ui.screens.*
import kotlinx.coroutines.launch
import com.example.doslenguasapp.utils.SessionManager

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    onSpeak: (String) -> Unit = {}
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val drawerRoutes = listOf(Routes.HOME, Routes.MY_PHRASES, Routes.PROFILE, Routes.FLASHCARDS)

    fun goToMainDestination(route: String) {
        navController.navigate(route) {
            popUpTo(Routes.HOME) { saveState = true }
            launchSingleTop = true
            restoreState = true
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
                    SessionManager.logout()
                    scope.launch { drawerState.close() }
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = if (SessionManager.isLoggedIn()) Routes.HOME else Routes.LOGIN
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
                    onGoToHome = { goToMainDestination(Routes.HOME) },
                    onOpenPhrase = { id -> navController.navigate(Routes.detail(id)) }
                )
            }

            composable(Routes.FLASHCARDS) {
                FlashcardsScreen(
                    onMenuClick = openMenu,
                    onSpeak = onSpeak
                )
            }

            composable(Routes.PROFILE) {
                ProfileScreen(
                    onMenuClick = openMenu,
                    onLogout = {
                        SessionManager.logout()
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(0) { inclusive = true }
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
                DetailScreen(
                    onBack = { navController.popBackStack() },
                    onSpeakTurkish = onSpeak
                )
            }
        }
    }
}

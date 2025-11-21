package com.kin.easynotes.presentation.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import coil.util.Logger
import com.kin.easynotes.presentation.screens.settings.settings.lock.LockScreen
import com.kin.easynotes.presentation.screens.edit.EditNoteView
import com.kin.easynotes.presentation.screens.home.HomeView
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel
import com.kin.easynotes.presentation.screens.terms.TermsScreen
import com.kin.easynotes.presentation.screens.voice_notes.VoiceNotesScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@Composable
fun AppNavHost(settingsModel: SettingsViewModel,navController: NavHostController = rememberNavController(), noteId: Int, defaultRoute: String) {

    val activity = (LocalContext.current as? Activity)
    val startRoute = if (defaultRoute != NavRoutes.LockScreen.route && noteId != -1) NavRoutes.Edit.route else defaultRoute

    NavHost(navController, startDestination = startRoute) {
        animatedComposable(NavRoutes.Home.route) {
            HomeView(
                onSettingsClicked = { navController.navigate(NavRoutes.Settings.route) },
                onNoteClicked = { id, encrypted ->
                    navController.navigate(
                        NavRoutes.Edit.createRoute(
                            id,
                            encrypted
                        )
                    )
                },
                onVoiceNotesClicked = { navController.navigate(NavRoutes.VoiceNotes.route) },
                settingsModel = settingsModel
            )
        }

        animatedComposable(NavRoutes.Terms.route) {
            TermsScreen(
                settingsModel
            )
        }

        animatedComposable(NavRoutes.LockScreen.route) { backStackEntry ->
            val actionString = backStackEntry.arguments?.getString("type") ?: null
            val action = if (actionString == "null" || actionString == null) {
                null
            } else {
                ActionType.valueOf(actionString)
            }
            println(actionString)
            println(action)
            LockScreen(
                settingsViewModel = settingsModel,
                navController = navController,
                action = action,
            )
        }

        animatedComposable(NavRoutes.Edit.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
            val encrypted = backStackEntry.arguments?.getString("encrypted").toBoolean()
            EditNoteView(
                settingsViewModel = settingsModel,
                id = if (noteId == -1) id else noteId,
                encrypted = encrypted,
                isWidget = noteId != -1
            ) {
                if (noteId == -1) {
                    navController.navigateUp()
                } else {
                    activity?.finish()
                }
            }
        }

        animatedComposable(NavRoutes.VoiceNotes.route) {
            VoiceNotesScreen()
        }

        settingScreens.forEach { (route, screen) ->
            if (route == NavRoutes.Settings.route) {
                slideInComposable(route) {
                    screen(settingsModel, navController)
                }
            } else {
                animatedComposable(route) {
                    screen(settingsModel, navController)
                }
            }
        }
    }
}

suspend fun getDefaultRoute(
    settingsModel: SettingsViewModel,
    noteId: Int
): String {
    val routeFlow = MutableStateFlow<String?>(null)
    runBlocking {
            val route = when {
                settingsModel.settings.value.passcode != null -> NavRoutes.LockScreen.route
                !settingsModel.settings.value.termsOfService -> NavRoutes.Terms.route
                noteId == -1 -> NavRoutes.Home.route
                else -> NavRoutes.Edit.createRoute(noteId, false)
            }
            routeFlow.value = route
        }

    return routeFlow.filterNotNull().first()
}

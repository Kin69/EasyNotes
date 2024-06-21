package com.kin.easynotes.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.kin.easynotes.presentation.screens.edit.EditNoteView
import com.kin.easynotes.presentation.screens.home.HomeView
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel

@Composable
fun AppNavHost(settingsModel: SettingsViewModel, startDestination: String, navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = startDestination) {
        animatedComposable(NavRoutes.Home.route) {
            HomeView(
                onSettingsClicked = { navController.navigate(NavRoutes.Settings.route) },
                onNoteClicked = { id -> navController.navigate(NavRoutes.Edit.createRoute(id)) },
                settingsModel = settingsModel
            )
        }

        animatedComposable(NavRoutes.Edit.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
            EditNoteView(
                settingsViewModel = settingsModel,
                id = id
            ) {
                navController.navigateUp()
            }
        }

        settingScreens.forEach { (route, screen) ->
            if (route == NavRoutes.Settings.route) {
                slideInComposable(route) {
                    screen(settingsModel,navController)
                }
            } else {
                animatedComposable(route) {
                    screen(settingsModel,navController)
                }
            }
        }
    }
}

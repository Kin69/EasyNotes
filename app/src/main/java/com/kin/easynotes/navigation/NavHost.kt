package com.kin.easynotes.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.kin.easynotes.presentation.screens.edit.EditNoteView
import com.kin.easynotes.presentation.screens.home.HomeView
import com.kin.easynotes.presentation.screens.settings.SettingsView
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel


@Composable
fun AppNavHost(settingsModel: SettingsViewModel, startDestination : String, navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = startDestination) {
        animatedComposable(route = NavRoutes.Home.route) {
            HomeView(navController = navController)
        }
        animatedComposable(route = NavRoutes.Edit.route + "/{id}", arguments = NavRoutes.Edit.navArguments) { entry ->
            val id = entry.arguments?.getInt("id") ?: 0
            EditNoteView(id = id) {
                navController.navigateUp()
            }
        }
        slideInComposable(route = NavRoutes.Settings.route) {
            SettingsView(navController = navController,settingsModel)
        }
    }
}

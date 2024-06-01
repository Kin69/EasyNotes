package com.kin.easynotes.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.kin.easynotes.presentation.screens.edit.EditNoteView
import com.kin.easynotes.presentation.screens.home.HomeView
import com.kin.easynotes.presentation.screens.search.SearchScreen
import com.kin.easynotes.presentation.screens.settings.SettingsView
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppNavHost(settingsModel: SettingsViewModel, startDestination : String, navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = startDestination) {
        animatedComposable(route = NavRoutes.Home.route) {
            HomeView(navController = navController, settingsModel)
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
        animatedComposable(route = NavRoutes.Search.route) {
            SearchScreen(navController = navController)
        }
    }
}

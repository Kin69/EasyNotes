package com.kin.easynotes.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.kin.easynotes.presentation.screens.edit.EditNoteView
import com.kin.easynotes.presentation.screens.home.HomeView
import com.kin.easynotes.presentation.screens.search.SearchScreen
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel

@Composable
fun AppNavHost(settingsModel: SettingsViewModel, startDestination: String, navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = startDestination) {
        animatedComposable(NavRoutes.Home.route) {
            HomeView(
                onSettingsClicked = { navController.navigate(NavRoutes.Settings.route) },
                onSearchClicked = { navController.navigate(NavRoutes.Search.route) },
                onNoteClicked = { id -> navController.navigate(NavRoutes.Edit.createRoute(id)) }
            )
        }

        animatedComposable(NavRoutes.Edit.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
            EditNoteView(id = id) {
                navController.navigateUp()
            }
        }

        animatedComposable(NavRoutes.Search.route) {
            SearchScreen(
                onNoteClicked = { id -> navController.navigate(NavRoutes.Edit.createRoute(id)) },
                onBackNavClicked = { navController.navigateUp() }
            )
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

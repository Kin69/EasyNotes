package com.kin.easynotes.presentation.navigation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.kin.easynotes.presentation.screens.edit.EditNoteView
import com.kin.easynotes.presentation.screens.home.HomeView
import com.kin.easynotes.presentation.screens.search.SearchScreen
import com.kin.easynotes.presentation.screens.settings.SettingsView
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel

@Composable
fun AppNavHost(settingsModel: SettingsViewModel, startDestination : String, navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = startDestination) {
        animatedComposable(route = NavRoutes.Home.route) {
            HomeView(
                onSettingsClicked = { navController.navigate(NavRoutes.Settings.route) },
                onSearchClicked = { navController.navigate(NavRoutes.Search.route) },
                onNoteClicked = { id -> navController.navigate(NavRoutes.Edit.route + "/$id") }
            )
        }

        animatedComposable(route = NavRoutes.Edit.route + "/{id}", arguments = NavRoutes.Edit.navArguments) { entry ->
            val id = entry.arguments?.getInt("id") ?: 0
            EditNoteView(id = id) {
                navController.navigateUp()
            }
        }

        slideInComposable(route = NavRoutes.Settings.route) {
            SettingsView(
                onBackNavClicked = { navController.navigate(NavRoutes.Home.route) },
                settingsModel = settingsModel
            )
        }

        animatedComposable(route = NavRoutes.Search.route) {
            SearchScreen(
                onNoteClicked = { id -> navController.navigate(NavRoutes.Edit.route + "/$id") },
                onBackNavClicked = { navController.navigateUp() }
            )
        }
    }
}


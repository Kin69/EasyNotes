package com.kin.easynotes.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.kin.easynotes.presentation.screens.settings.MainSettings
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel
import com.kin.easynotes.presentation.screens.settings.settings.AboutScreen
import com.kin.easynotes.presentation.screens.settings.settings.CloudScreen
import com.kin.easynotes.presentation.screens.settings.settings.ColorStylesScreen
import com.kin.easynotes.presentation.screens.settings.settings.LanguageScreen
import com.kin.easynotes.presentation.screens.settings.settings.MarkdownScreen
import com.kin.easynotes.presentation.screens.settings.settings.PrivacyScreen
import com.kin.easynotes.presentation.screens.settings.settings.SupportScreen
import com.kin.easynotes.presentation.screens.settings.settings.ToolsScreen

sealed class NavRoutes(val route: String) {
    data object Home : NavRoutes("home")
    data object Edit : NavRoutes("edit/{id}/{encrypted}") {
        fun createRoute(id: Int, encrypted : Boolean) = "edit/$id/$encrypted"
    }
    data object Terms : NavRoutes("terms")
    data object Settings : NavRoutes("settings")
    data object ColorStyles : NavRoutes("settings/color_styles")
    data object Language : NavRoutes("settings/language")
    data object Cloud : NavRoutes("settings/cloud")
    data object Privacy : NavRoutes("settings/privacy")
    data object Markdown : NavRoutes("settings/markdown")
    data object Tools : NavRoutes("settings/tools")
    data object History : NavRoutes("settings/history")
    data object Widgets : NavRoutes("settings/widgets")
    data object About : NavRoutes("settings/about")
    data object Support : NavRoutes("settings/support")
}

val settingScreens = mapOf<String, @Composable (settingsViewModel: SettingsViewModel, navController : NavController) -> Unit>(
    NavRoutes.Settings.route to { settings, navController -> MainSettings(settings, navController) },
    NavRoutes.ColorStyles.route to { settings, navController -> ColorStylesScreen(navController,settings) },
    NavRoutes.Language.route to { settings, navController -> LanguageScreen(navController,settings) },
    NavRoutes.Cloud.route to { settings, navController -> CloudScreen(navController,settings) },
    NavRoutes.Privacy.route to { settings, navController -> PrivacyScreen(navController, settings) },
    NavRoutes.Markdown.route to { settings, navController ->  MarkdownScreen(navController,settings) },
    NavRoutes.Tools.route to { settings, navController -> ToolsScreen(navController,settings) },
    NavRoutes.About.route to { settings, navController -> AboutScreen(navController,settings) },
    NavRoutes.Support.route to { settings, navController -> SupportScreen(navController,settings) }
)

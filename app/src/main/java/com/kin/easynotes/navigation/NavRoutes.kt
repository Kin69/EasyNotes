package com.kin.easynotes.navigation
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class NavRoutes(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {
    data object Home : NavRoutes("home")
    data object Search : NavRoutes("search")
    data object Settings : NavRoutes("settings")
    data object Edit : NavRoutes(
        route = "edit",
        navArguments = listOf(
            navArgument("id") {
                type = NavType.IntType
                defaultValue = 0
            }
        )
    )
}
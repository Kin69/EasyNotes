package com.kin.easynotes.navigation


import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.animatedComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) = composable(
    route = route,
    arguments = arguments,
    deepLinks = deepLinks,
    enterTransition = {
        fadeIn(animationSpec = tween(300)) +
                scaleIn(
                    initialScale = 0.9f,
                    animationSpec = tween(400)
                )
    },
    exitTransition = {
        fadeOut(animationSpec = tween(300)) +
                scaleOut(
                    targetScale = 0.9f,
                    animationSpec = tween(400)
                )
    },
    popEnterTransition = {
        fadeIn(animationSpec = tween(300)) +
                scaleIn(
                    initialScale = 0.9f,
                    animationSpec = tween(400)
                )
    },
    popExitTransition = {
        fadeOut(animationSpec = tween(300)) +
                scaleOut(
                    targetScale = 0.9f,
                    animationSpec = tween(400)
                )
    },
    content = content
)

fun NavGraphBuilder.slideInComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) = composable(
    route = route,
    arguments = arguments,
    deepLinks = deepLinks,
    enterTransition = {
        slideInHorizontally(
            initialOffsetX = { fullWidth -> fullWidth },
            animationSpec = tween(500)
        )
    },
    exitTransition = {
        slideOutHorizontally(
            targetOffsetX = { fullWidth -> fullWidth },
            animationSpec = tween(500)
        )
    },
    popEnterTransition = {
        slideInHorizontally(
            initialOffsetX = { fullWidth -> fullWidth },
            animationSpec = tween(300)
        )
    },
    popExitTransition = {
        slideOutHorizontally(
            targetOffsetX = { fullWidth -> fullWidth },
            animationSpec = tween(300)
        )
    },
    content = content
)

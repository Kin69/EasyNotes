package com.kin.easynotes.presentation.components

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

private const val DEFAULT_FADE_DURATION = 300
private const val DEFAULT_SCALE_DURATION = 400
private const val DEFAULT_SLIDE_DURATION = 400
private const val DEFAULT_INITIAL_SCALE = 0.9f

fun getNoteEnterAnimation(): EnterTransition {
    return fadeIn(animationSpec = tween(DEFAULT_FADE_DURATION)) + scaleIn(
        initialScale = 0.9f,
        animationSpec = tween(DEFAULT_SCALE_DURATION)
    )
}

fun getNoteExitAnimation(slideDirection: Int): ExitTransition {
    return slideOutHorizontally(
        targetOffsetX = { slideDirection * it },
        animationSpec = tween(durationMillis = DEFAULT_SLIDE_DURATION)
    ) + fadeOut(animationSpec = tween(durationMillis = DEFAULT_FADE_DURATION))
}

fun defaultScreenEnterAnimation(): EnterTransition {
    return fadeIn(animationSpec = tween(DEFAULT_FADE_DURATION)) +
            scaleIn(
                initialScale = DEFAULT_INITIAL_SCALE,
                animationSpec = tween(DEFAULT_SCALE_DURATION)
            )
}

fun defaultScreenExitAnimation(): ExitTransition {
    return fadeOut(animationSpec = tween(DEFAULT_FADE_DURATION)) +
            scaleOut(
                targetScale = DEFAULT_INITIAL_SCALE,
                animationSpec = tween(DEFAULT_SCALE_DURATION)
            )
}

fun slideScreenEnterAnimation(): EnterTransition {
    return slideInHorizontally(
        initialOffsetX = { fullWidth -> fullWidth },
        animationSpec = tween(DEFAULT_SLIDE_DURATION)
    )
}

fun slideScreenExitAnimation(): ExitTransition {
    return slideOutHorizontally(
        targetOffsetX = { fullWidth -> fullWidth },
        animationSpec = tween(DEFAULT_SLIDE_DURATION)
    )
}

package com.kin.easynotes.presentation.theme

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel

private fun setStatusBarAppearance(activity: Activity, isLightMode: Boolean) {
    WindowCompat.getInsetsController(activity.window, activity.window.decorView).apply {
        isAppearanceLightStatusBars = isLightMode
    }
}

private fun adjustColor(color: Color, multiplier: Float): Color {
    val newRed = (color.red * multiplier).coerceIn(0f, 1f)
    val newGreen = (color.green * multiplier).coerceIn(0f, 1f)
    val newBlue = (color.blue * multiplier).coerceIn(0f, 1f)
    return Color(newRed, newGreen, newBlue, color.alpha)
}

private fun getColorScheme(context: Context, isDarkTheme: Boolean, isDynamicTheme: Boolean, isAmoledTheme: Boolean): ColorScheme {
    val colorScheme = if (isDynamicTheme && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val dynamicScheme = if (isDarkTheme || isAmoledTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        val onPrimaryColor = if (isDarkTheme || isAmoledTheme) adjustColor(color = dynamicScheme.background, multiplier = 1.2f) else dynamicScheme.surfaceDim
        val onSecondaryColor = if (isDarkTheme || isAmoledTheme) adjustColor(color = dynamicScheme.background, multiplier = 2f) else adjustColor(color = dynamicScheme.surfaceDim, multiplier = 2f)
        dynamicScheme.copy(onPrimary = onPrimaryColor, onSecondary = onSecondaryColor)
    } else if (isDarkTheme || isAmoledTheme) darkScheme else lightScheme

    return if (isAmoledTheme) colorScheme.copy(background = Color.Black, surface = Color.Black) else colorScheme
}

@Composable
fun LeafNotesTheme(
    settingsModel: SettingsViewModel,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val activity = LocalView.current.context as Activity
    setStatusBarAppearance(activity, !(settingsModel.darkTheme || settingsModel.amoledTheme))

    MaterialTheme(
        colorScheme = getColorScheme(context, settingsModel.darkTheme, settingsModel.dynamicTheme, settingsModel.amoledTheme),
        typography = Typography(),
        content = content
    )
}
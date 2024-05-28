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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.core.view.WindowCompat
import com.kin.easynotes.R
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel
private fun getColorScheme(context: Context, isDarkTheme: Boolean, isDynamicTheme: Boolean, isAmoledTheme: Boolean): ColorScheme {
    val colorScheme = if (isDynamicTheme && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (isDarkTheme || isAmoledTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else if (isDarkTheme || isAmoledTheme) darkScheme else lightScheme

    return if (isAmoledTheme) colorScheme.copy(surfaceContainerLow = Color.Black, surface = Color.Black) else colorScheme
}

@Composable
fun LeafNotesTheme(
    settingsModel: SettingsViewModel,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val activity = LocalView.current.context as Activity
    WindowCompat.getInsetsController(activity.window, activity.window.decorView).apply {
        isAppearanceLightStatusBars = !(settingsModel.darkTheme || settingsModel.amoledTheme)
    }

    MaterialTheme(
        colorScheme = getColorScheme(context, settingsModel.darkTheme, settingsModel.dynamicTheme, settingsModel.amoledTheme),
        typography = Typography(),
        content = content
    )
}

val GlobalFont = FontFamily(Font(R.font.roboto))
val CodeFont = FontFamily(Font(R.font.monospace))
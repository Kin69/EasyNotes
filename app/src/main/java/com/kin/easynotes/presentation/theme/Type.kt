package com.kin.easynotes.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel

/**
 * Get a Typography object with font sizes scaled based on user settings
 */
@Composable
fun getTypography(settingsViewModel: SettingsViewModel): Typography {
    val baseFontSize = settingsViewModel.settings.value.fontSize
    val fontSizeFactor = baseFontSize / 16f
    
    fun scaledSize(size: Int): Int = (size * fontSizeFactor).toInt()
    
    return Typography(
        displayLarge = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = FontUtils.getFontSize(settingsViewModel, scaledSize(57)),
        ),
        displayMedium = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = FontUtils.getFontSize(settingsViewModel, scaledSize(45)),
        ),
        displaySmall = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = FontUtils.getFontSize(settingsViewModel, scaledSize(36)),
        ),
        headlineLarge = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = FontUtils.getFontSize(settingsViewModel, scaledSize(32)),
        ),
        headlineMedium = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = FontUtils.getFontSize(settingsViewModel, scaledSize(28)),
        ),
        headlineSmall = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = FontUtils.getFontSize(settingsViewModel, scaledSize(24)),
        ),
        titleLarge = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = FontUtils.getFontSize(settingsViewModel, scaledSize(22)),
        ),
        titleMedium = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = FontUtils.getFontSize(settingsViewModel, scaledSize(16)),
        ),
        titleSmall = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = FontUtils.getFontSize(settingsViewModel, scaledSize(14)),
        ),
        bodyLarge = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = FontUtils.getFontSize(settingsViewModel, scaledSize(16)),
        ),
        bodyMedium = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = FontUtils.getFontSize(settingsViewModel, scaledSize(14)),
        ),
        bodySmall = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = FontUtils.getFontSize(settingsViewModel, scaledSize(12)),
        ),
        labelLarge = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = FontUtils.getFontSize(settingsViewModel, scaledSize(14)),
        ),
        labelMedium = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = FontUtils.getFontSize(settingsViewModel, scaledSize(12)),
        ),
        labelSmall = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = FontUtils.getFontSize(settingsViewModel, scaledSize(11)),
        ),
    )
}

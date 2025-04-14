package com.kin.easynotes.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel

/**
 * Utility functions for font size management throughout the app
 */
object FontUtils {
    /**
     * Get the appropriate font size based on the base size and settings
     * @param settingsViewModel The settings view model
     * @param baseSize The base size to scale from (default is 16sp)
     * @param scaleFactor Optional scale factor to apply (e.g., 0.875 for smaller text)
     * @return The calculated text unit
     */
    @Composable
    fun getFontSize(
        settingsViewModel: SettingsViewModel,
        baseSize: Int = 16,
        scaleFactor: Float = 1f
    ): TextUnit {
        val settingsFontSize = settingsViewModel.settings.value.fontSize
        val sizeDifference = settingsFontSize - baseSize
        
        return ((baseSize + sizeDifference) * scaleFactor).sp
    }
    
    /**
     * Get title font size
     */
    @Composable
    fun getTitleFontSize(settingsViewModel: SettingsViewModel): TextUnit {
        return getFontSize(settingsViewModel, baseSize = 18, scaleFactor = 1.1f)
    }

    
    /**
     * Get body font size
     */
    @Composable
    fun getBodyFontSize(settingsViewModel: SettingsViewModel): TextUnit {
        return getFontSize(settingsViewModel)
    }
}

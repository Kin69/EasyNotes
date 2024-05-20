package com.kin.easynotes.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kin.easynotes.domain.usecase.Preferences
import com.kin.easynotes.navigation.AppNavHost
import com.kin.easynotes.navigation.NavRoutes
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel
import com.kin.easynotes.presentation.theme.LeafNotesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            val settingsViewModel: SettingsViewModel = viewModel()

            settingsViewModel.apply {
                if (Preferences.instance.getBoolean("AUTOMATIC_THEME", true)) {
                    dynamicTheme = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                    darkTheme = isSystemInDarkTheme()
                }
            }

            LeafNotesTheme(settingsViewModel) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceContainerLow
                ) {
                    AppNavHost(settingsViewModel, NavRoutes.Home.route)
                }
            }
        }
    }
}

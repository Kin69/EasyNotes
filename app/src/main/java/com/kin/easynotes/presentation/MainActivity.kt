package com.kin.easynotes.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kin.easynotes.domain.model.Settings
import com.kin.easynotes.presentation.navigation.AppNavHost
import com.kin.easynotes.presentation.navigation.NavRoutes
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
                if (settings.value.automaticTheme) {
                    settingsViewModel.update(settingsViewModel.settings.value.copy(dynamicTheme = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S))
                    settingsViewModel.update(settingsViewModel.settings.value.copy(darkTheme = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S))
                }
            }

            LeafNotesTheme(settingsViewModel) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                ) {
                    AppNavHost(settingsViewModel, NavRoutes.Home.route)
                }
            }
        }
    }
}

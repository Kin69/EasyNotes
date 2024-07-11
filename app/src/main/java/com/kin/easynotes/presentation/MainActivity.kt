package com.kin.easynotes.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.kin.easynotes.presentation.components.registerGalleryObserver
import com.kin.easynotes.presentation.navigation.AppNavHost
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel
import com.kin.easynotes.presentation.theme.LeafNotesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel<SettingsViewModel>()
            val noteId = intent?.getIntExtra("noteId", -1) ?: -1

            registerGalleryObserver(this)

            LeafNotesTheme(settingsViewModel) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                ) {
                    AppNavHost(settingsViewModel, noteId = noteId)
                }
            }
        }
    }
}

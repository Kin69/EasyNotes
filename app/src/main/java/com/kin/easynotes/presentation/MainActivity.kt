package com.kin.easynotes.presentation

import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.rememberNavController
import com.kin.easynotes.presentation.navigation.AppNavHost
import com.kin.easynotes.presentation.navigation.NavRoutes
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel
import com.kin.easynotes.presentation.theme.LeafNotesTheme
import dagger.hilt.android.AndroidEntryPoint

fun NavOptionsBuilder.popUpToTop(navController: NavController) {
    popUpTo(navController.currentBackStackEntry?.destination?.route ?: return) {
        inclusive =  true
    }
}

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavHostController
    private var settingsViewModel: SettingsViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()

        setContent {
            settingsViewModel = hiltViewModel<SettingsViewModel>()
            val noteId = intent?.getIntExtra("noteId", -1) ?: -1

            if (settingsViewModel!!.settings.value.gallerySync) {
                contentResolver.registerContentObserver(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    true,
                    settingsViewModel!!.galleryObserver
                )
            }

            LeafNotesTheme(settingsViewModel!!) {

                Surface(
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                ) {
                    navController = rememberNavController()
                    AppNavHost(settingsViewModel!!, navController, noteId, settingsViewModel!!.defaultRoute!!)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        settingsViewModel?.let {
            it.loadDefaultRoute()
            if (it.defaultRoute != NavRoutes.Home.route) {
                if (it.settings.value.passcode != null || it.settings.value.fingerprint || it.settings.value.pattern != null) {
                    if (it.settings.value.lockImmediately) {
                        navController.navigate(it.defaultRoute!!) { popUpToTop(navController) }
                    }
                }
            }
        }
    }
}

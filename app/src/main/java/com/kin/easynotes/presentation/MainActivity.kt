package com.kin.easynotes.presentation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kin.easynotes.data.repository.SettingsRepositoryImpl
import com.kin.easynotes.presentation.components.GalleryObserver
import com.kin.easynotes.presentation.navigation.AppNavHost
import com.kin.easynotes.presentation.navigation.checkLock
import com.kin.easynotes.presentation.navigation.getDefaultRoute
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel
import com.kin.easynotes.presentation.theme.LeafNotesTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavHostController
    private var settingsViewModel: SettingsViewModel? = null
    private var defaultRoute: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        setupSplashScreen(splashScreen)
        enableEdgeToEdge()

        setContent {
            settingsViewModel = hiltViewModel<SettingsViewModel>()
            val mainViewModel = hiltViewModel<MainViewModel>()
            val noteId = intent?.getIntExtra("noteId", -1) ?: -1

            if (settingsViewModel!!.settings.value.gallerySync) {
                contentResolver.registerContentObserver(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    true,
                    settingsViewModel!!.galleryObserver
                )
            }

            LeafNotesTheme(settingsViewModel!!) {
                defaultRoute = mainViewModel.defaultRoute.collectAsState().value

                LaunchedEffect(settingsViewModel!!.settings.value.termsOfService) {
                    mainViewModel.determineDefaultRoute(settingsViewModel!!, noteId)
                }

                Surface(
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                ) {
                    navController = rememberNavController()
                    if (defaultRoute != null) {
                        AppNavHost(settingsViewModel!!, navController, noteId, defaultRoute!!)
                    }
                }
            }
        }
    }

    private fun setupSplashScreen(splashScreen: SplashScreen) {
        splashScreen.setKeepOnScreenCondition { defaultRoute == null }
    }

    override fun onResume() {
        super.onResume()
        settingsViewModel?.let {
            if (it.settings.value.lockImmediately) {
                checkLock(it, navController)
            }
        }
    }
}

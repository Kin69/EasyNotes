package com.kin.easynotes.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kin.easynotes.presentation.navigation.NavRoutes
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _defaultRoute = MutableStateFlow<String?>(null)
    val defaultRoute: StateFlow<String?> = _defaultRoute

    fun determineDefaultRoute(settingsModel: SettingsViewModel, noteId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val route = when {
                settingsModel.settings.value.passcode != null -> NavRoutes.LockScreen.route
                !settingsModel.settings.value.termsOfService -> NavRoutes.Terms.route
                noteId == -1 -> NavRoutes.Home.route
                else -> NavRoutes.Edit.createRoute(noteId, false)
            }
            _defaultRoute.value = route
        }
    }
}
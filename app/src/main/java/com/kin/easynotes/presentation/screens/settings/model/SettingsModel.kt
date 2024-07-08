package com.kin.easynotes.presentation.screens.settings.model

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kin.easynotes.BuildConfig
import com.kin.easynotes.data.repository.BackupRepository
import com.kin.easynotes.domain.model.Settings
import com.kin.easynotes.domain.usecase.SettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val backup: BackupRepository,
    val settingsUseCase: SettingsUseCase
) : ViewModel() {
    val databaseUpdate = mutableStateOf(false)

    private val _settings = mutableStateOf(Settings())
    var settings: State<Settings> = _settings

    init {
        viewModelScope.launch {
            loadSettings()
        }
    }

    private suspend fun loadSettings() {
        val loadedSettings = runBlocking(Dispatchers.IO) {
            settingsUseCase.loadSettingsFromRepository()
        }
        _settings.value = loadedSettings
    }

    fun update(newSettings: Settings) {
        _settings.value = newSettings.copy()
        viewModelScope.launch {
            settingsUseCase.saveSettingsToRepository(newSettings)
        }
    }

    fun onExport(uri: Uri) {
        viewModelScope.launch {
            backup.export(uri)
            databaseUpdate.value = true
        }
    }

    fun onImport(uri: Uri) {
        viewModelScope.launch {
            backup.import(uri)
            databaseUpdate.value = true
        }
    }

    val version: String = BuildConfig.VERSION_NAME
    val build: String = BuildConfig.BUILD_TYPE
}

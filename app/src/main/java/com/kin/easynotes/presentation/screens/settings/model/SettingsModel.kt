package com.kin.easynotes.presentation.screens.settings.model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalUriHandler
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kin.easynotes.BuildConfig
import com.kin.easynotes.Notes
import com.kin.easynotes.domain.model.Settings
import com.kin.easynotes.domain.repository.NoteRepository
import com.kin.easynotes.domain.usecase.NoteUseCase
import com.kin.easynotes.domain.usecase.SettingsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SettingsViewModel : ViewModel() {
    private val noteRepository: NoteRepository = Notes.dataModule.noteRepository
    private val settingsRepository = Notes.dataModule.settingsRepository

    private val settingsUseCase = SettingsUseCase(settingsRepository)
    val noteUseCase = NoteUseCase(noteRepository, viewModelScope)

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

    val version: String = BuildConfig.VERSION_NAME
}

package com.kin.easynotes.presentation.screens.settings.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kin.easynotes.BuildConfig
import com.kin.easynotes.Notes
import com.kin.easynotes.domain.usecase.NoteUseCase
import com.kin.easynotes.data.settings.Preferences

class SettingsViewModel : ViewModel() {
    private val noteRepository = Notes.dataModule.noteRepository
    val noteUseCase = NoteUseCase(noteRepository, viewModelScope)

    val preferences: Preferences = Notes.dataModule.preferences

    var version = BuildConfig.VERSION_NAME
    var dynamicTheme: Boolean by mutableStateOf(preferences.dynamicTheme)
    var darkTheme: Boolean by mutableStateOf(preferences.darkTheme)
    var amoledTheme: Boolean by mutableStateOf(preferences.amoledTheme)

    var sortProperty: String by mutableStateOf(preferences.sortProperty)
    var sortOrder: String by mutableStateOf(preferences.sortOrder)

    fun updateSetting(variable: String, value: Boolean): Boolean {
        preferences.edit { putBoolean(variable, value.not()) }
        preferences.edit { putBoolean("automatic_theme", false) }
        return value.not()
    }

    fun updateSortSettings(property: String, order: String) {
        sortProperty = property
        sortOrder = order
        preferences.sortProperty = property
        preferences.sortOrder = order
    }
}

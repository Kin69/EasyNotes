package com.kin.easynotes.presentation.screens.settings.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kin.easynotes.BuildConfig
import com.kin.easynotes.Notes
import com.kin.easynotes.di.DataModule
import com.kin.easynotes.domain.usecase.NoteUseCase

class SettingsViewModel() : ViewModel() {
    private val noteRepository = Notes.dataModule.noteRepository
    val noteUseCase = NoteUseCase(noteRepository, viewModelScope)

    val preferences = Notes.dataModule.preferences

    var version = BuildConfig.VERSION_NAME
    var dynamicTheme : Boolean by mutableStateOf(preferences.dynamicTheme)
    var darkTheme : Boolean by mutableStateOf(preferences.darkTheme)
    var amoledTheme  : Boolean by mutableStateOf(preferences.amoledTheme)

    fun updateSetting(variable: String, value: Boolean) : Boolean {
        preferences.edit { putBoolean(variable, value.not()) }
        preferences.edit { putBoolean("automatic_theme", false) }
        return value.not()
    }
}
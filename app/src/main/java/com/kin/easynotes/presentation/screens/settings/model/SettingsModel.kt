package com.kin.easynotes.presentation.screens.settings.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kin.easynotes.BuildConfig
import com.kin.easynotes.domain.usecase.NoteViewModel
import com.kin.easynotes.domain.usecase.Preferences

class SettingsViewModel : NoteViewModel() {
    var version = BuildConfig.VERSION_NAME
    var dynamicTheme : Boolean by mutableStateOf(getValue("DYNAMIC_THEME"))
    var darkTheme : Boolean by mutableStateOf(getValue("DARK_THEME"))
    var amoledTheme  : Boolean by mutableStateOf(getValue("AMOLED_THEME"))

    fun getValue(variable: String) : Boolean {
        return Preferences.instance.getBoolean(variable, false)
    }
    fun updateSetting(variable: String, value: Boolean) : Boolean {
        Preferences.edit { putBoolean(variable, value.not()) }
        Preferences.edit { putBoolean("AUTOMATIC_THEME", false) }
        return value.not()
    }
}
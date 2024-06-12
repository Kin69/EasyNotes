package com.kin.easynotes.domain.usecase

import com.kin.easynotes.domain.model.Settings
import com.kin.easynotes.domain.repository.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SettingsUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend fun loadSettingsFromRepository(): Settings {
        val settingsClass = Settings::class.java
        val settings = settingsClass.newInstance() as Settings
        settingsClass.declaredFields.forEach { field ->
            field.isAccessible = true
            val settingName = field.name
            val settingValue = when (field.type) {
                Boolean::class.java -> settingsRepository.getBoolean(settingName) ?: false
                String::class.java -> settingsRepository.getString(settingName) ?: ""
                Int::class.java -> settingsRepository.getInt(settingName) ?: 0
                else -> throw IllegalArgumentException("Unsupported setting type: ${field.type}")
            }
            field.set(settings, settingValue)
        }

        return settings
    }

   suspend fun saveSettingsToRepository(settings: Settings) {
        val settingsClass = Settings::class.java

        settingsClass.declaredFields.forEach { field ->
            field.isAccessible = true
            val settingName = field.name
            val settingValue = field.get(settings)
            when (settingValue) {
                is Boolean -> settingsRepository.putBoolean(settingName, settingValue)
                is String -> settingsRepository.putString(settingName, settingValue)
                is Int -> settingsRepository.putInt(settingName, settingValue)
                else -> throw IllegalArgumentException("Unsupported setting type: ${settingValue?.javaClass}")
            }
        }
    }
}

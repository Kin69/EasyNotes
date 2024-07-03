package com.kin.easynotes.domain.usecase

import com.kin.easynotes.data.repository.SettingsRepositoryImpl
import com.kin.easynotes.domain.model.Settings
import com.kin.easynotes.domain.repository.SettingsRepository
import javax.inject.Inject

class SettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepositoryImpl
) {
    suspend fun loadSettingsFromRepository(): Settings {
        val settingsClass = Settings::class.java
        val defaultSettings = Settings()
        val settings = settingsClass.getDeclaredConstructor().newInstance()

        settingsClass.declaredFields.forEach { field ->
            field.isAccessible = true
            val settingName = field.name
            val defaultValue = field.get(defaultSettings)
            println(settingName)
            val settingValue = when (field.type) {
                Boolean::class.java -> settingsRepository.getBoolean(settingName) ?: defaultValue
                String::class.java -> settingsRepository.getString(settingName) ?: defaultValue
                Int::class.java -> settingsRepository.getInt(settingName) ?: defaultValue
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

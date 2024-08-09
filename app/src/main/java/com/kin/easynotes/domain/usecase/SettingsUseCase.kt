package com.kin.easynotes.domain.usecase

import com.kin.easynotes.core.constant.ConnectionConst
import com.kin.easynotes.data.repository.SettingsRepositoryImpl
import com.kin.easynotes.domain.model.Settings
import javax.inject.Inject

class SettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepositoryImpl,
    ) {

    suspend fun loadSettingsFromRepository(): Settings {
        return Settings().apply {
            Settings::class.java.declaredFields.forEach { field ->
                field.isAccessible = true
                val settingName = field.name
                val defaultValue = field.get(this)
                val settingValue = getSettingValue(field.type, settingName, defaultValue)
                field.set(this, settingValue)
            }
        }
    }

    private suspend fun getSettingValue(fieldType: Class<*>, settingName: String, defaultValue: Any?): Any? {
        return try {
            when (fieldType) {
                Boolean::class.java -> settingsRepository.getBoolean(settingName) ?: defaultValue
                String::class.java -> settingsRepository.getString(settingName) ?: defaultValue
                Int::class.java -> settingsRepository.getInt(settingName) ?: defaultValue
                else -> throw IllegalArgumentException("Unsupported setting type: $fieldType")
            }
        } catch (e: ClassCastException) {
            handleCorruptedPreference(settingName, e)
            defaultValue
        }
    }

    private fun handleCorruptedPreference(settingName: String, e: ClassCastException) {
        println("Corrupted preference. Contact support: ${ConnectionConst.SUPPORT_MAIL}")
        println("Invalid Key: $settingName")
        println(e.stackTraceToString())
    }

    suspend fun saveSettingsToRepository(settings: Settings) {
        Settings::class.java.declaredFields.forEach { field ->
            field.isAccessible = true
            val settingName = field.name
            val settingValue = field.get(settings)
            saveSettingValue(settingName, settingValue)
        }
    }

    private suspend fun saveSettingValue(settingName: String, settingValue: Any?) {
        when (settingValue) {
            is Boolean -> settingsRepository.putBoolean(settingName, settingValue)
            is String -> settingsRepository.putString(settingName, settingValue)
            is Int -> settingsRepository.putInt(settingName, settingValue)
            else -> throw IllegalArgumentException("Unsupported setting type: ${settingValue?.javaClass}")
        }
    }
}

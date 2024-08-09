package com.kin.easynotes.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kin.easynotes.domain.repository.SettingsRepository
import com.kin.easynotes.widget.NotesWidgetReceiver
import kotlinx.coroutines.flow.first


private const val PREFERENCES_NAME = "settings"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = PREFERENCES_NAME,
    produceMigrations = { context -> listOf(SharedPreferencesMigration(context, "notes")) }
)

class SettingsRepositoryImpl (private val context: Context) : SettingsRepository {
    override suspend fun putString(key: String, value: String) {
        val preferencesKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[preferencesKey] = value
        }
    }

    override suspend fun putInt(key: String, value: Int) {
        val preferencesKey = intPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[preferencesKey] = value
        }
    }

    override suspend fun getString(key: String): String? {
        val preferencesKey = stringPreferencesKey(key)
        val preferences = context.dataStore.data.first()
        return preferences[preferencesKey]
    }

    override suspend fun getInt(key: String): Int? {
        val preferencesKey = intPreferencesKey(key)
        val preferences = context.dataStore.data.first()
        return preferences[preferencesKey]
    }

    override suspend fun putBoolean(key: String, value: Boolean) {
        val preferencesKey = booleanPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[preferencesKey] = value
        }
    }

    override suspend fun getBoolean(key: String): Boolean? {
        val preferencesKey = booleanPreferencesKey(key)
        val preferences = context.dataStore.data.first()
        return preferences[preferencesKey]
    }

    override suspend fun getEveryNotesWidget(): List<Pair<Int, Int>> {
        val preferences = context.dataStore.data.first()
        val widgetPairs = mutableListOf<Pair<Int, Int>>()

        preferences.asMap().forEach { entry ->
            val key = entry.key.name

            if (entry.key.name.startsWith(NotesWidgetReceiver.WIDGET_PREFERENCE)) {
                val widgetId = key.substringAfter(NotesWidgetReceiver.WIDGET_PREFERENCE).toIntOrNull()
                if (widgetId != null) {
                    val value = entry.value as? Int ?: 0
                    widgetPairs.add(widgetId to value)
                }
            }
        }
        return widgetPairs
    }
}
package com.kin.easynotes.widget

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.kin.easynotes.data.repository.SettingsRepositoryImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class NotesWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = NotesWidget()

    companion object {
        const val WIDGET_PREFERENCE = "widgetNoteId_"
    }
}


class NotesDataStore(private val context: Context): DataStore<List<Int>> {
    override val data: Flow<List<Int>>
        get() {
            val settingsRepository = SettingsRepositoryImpl(context)
            return flow { emit(listOf(settingsRepository.getInt(NotesWidgetReceiver.WIDGET_PREFERENCE) ?: 0))}
        }

    override suspend fun updateData(transform: suspend (t: List<Int>) -> List<Int>): List<Int> {
        throw NotImplementedError("Not implemented")
    }
}
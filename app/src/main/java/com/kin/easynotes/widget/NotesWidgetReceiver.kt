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
        const val WIDGET_PREFERENCE = "widgetNote_"
    }
}


class NotesDataStore(private val context: Context): DataStore<List<Pair<Int, Int>>> {
    override val data: Flow<List<Pair<Int, Int>>>
        get() {
            val settingsRepository = SettingsRepositoryImpl(context)
            return flow { emit(settingsRepository.getEveryNotesWidget()) }
        }

    override suspend fun updateData(transform: suspend (t: List<Pair<Int, Int>>) -> List<Pair<Int, Int>>): List<Pair<Int, Int>> {
        throw NotImplementedError("Not implemented")
    }
}
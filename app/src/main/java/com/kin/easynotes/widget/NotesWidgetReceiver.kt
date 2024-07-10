package com.kin.easynotes.widget

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch



class NotesWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = NotesWidget()

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        observeData(context, 0)
    }

    companion object {
        private val coroutineScope = MainScope()

        fun observeData(context: Context, noteId: Int = 0) {
            coroutineScope.launch {
                val glanceId = GlanceAppWidgetManager(context).getGlanceIds(NotesWidget::class.java).firstOrNull()
                if (glanceId != null) {
                    updateAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId) { prefs ->
                        prefs.toMutablePreferences().apply {
                            this[intPreferencesKey("widgetNoteId")] = noteId
                        }
                    }
                    NotesWidget().update(context, glanceId)
                }
            }
        }
    }
}
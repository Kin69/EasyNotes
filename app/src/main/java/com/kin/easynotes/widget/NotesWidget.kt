package com.kin.easynotes.widget

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.provideContent
import androidx.glance.currentState
import androidx.glance.state.GlanceStateDefinition
import com.kin.easynotes.domain.usecase.NoteUseCase
import com.kin.easynotes.widget.ui.SelectedNote
import com.kin.easynotes.widget.ui.ZeroState
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.io.File

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WidgetModelRepositoryEntryPoint {
    fun noteUseCase() : NoteUseCase
}

class NotesWidget : GlanceAppWidget() {
    override val stateDefinition: GlanceStateDefinition<List<Pair<Int, Int>>>
        get() = object: GlanceStateDefinition<List<Pair<Int, Int>>> {
            override suspend fun getDataStore(
                context: Context,
                fileKey: String
            ): DataStore<List<Pair<Int, Int>>> {
                return NotesDataStore(context)
            }
            override fun getLocation(context: Context, fileKey: String): File {
                throw NotImplementedError("Not implemented")
            }
        }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val noteUseCase = getNoteUseCase(context)
        val widgetId = GlanceAppWidgetManager(context).getAppWidgetId(id)
        noteUseCase.observe()

        provideContent {
            GlanceTheme {
                val noteId = currentState<List<Pair<Int, Int>>>().filter { it.first == widgetId }.first().second
                val selectedNote = noteUseCase.notes.filter { it.id == noteId }
                when {
                    selectedNote.isEmpty() -> ZeroState(widgetId = widgetId)
                    else -> SelectedNote(selectedNote.first())
                }
            }
        }
    }
}


fun getNoteUseCase(applicationContext: Context): NoteUseCase {
    var widgetModelRepositoryEntrypoint: WidgetModelRepositoryEntryPoint = EntryPoints.get(
        applicationContext,
        WidgetModelRepositoryEntryPoint::class.java,
    )
    return widgetModelRepositoryEntrypoint.noteUseCase()
}
package com.kin.easynotes.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.kin.easynotes.domain.model.Note
import com.kin.easynotes.domain.usecase.NoteUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@EntryPoint
@InstallIn(SingletonComponent::class)
interface WidgetModelRepositoryEntryoint {
    fun noteUseCase() : NoteUseCase
}

class NotesWidget : GlanceAppWidget() {
    override val stateDefinition = PreferencesGlanceStateDefinition
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val noteUseCase = getNoteUseCase(context)

        provideContent {
            GlanceTheme {
                noteUseCase.observe()
                val prefs = currentState<Preferences>()
                val noteId = prefs[intPreferencesKey("widgetNoteId")]
                val selectedNote = noteUseCase.notes.filter { it.id == noteId }
                when {
                    selectedNote.isEmpty() -> Box {}
                    else -> SelectedNote(selectedNote.first())
                }
            }
        }
    }
}

fun getNoteUseCase(applicationContext: Context): NoteUseCase {
    var widgetModelRepositoryEntrypoint: WidgetModelRepositoryEntryoint = EntryPoints.get(
        applicationContext,
        WidgetModelRepositoryEntryoint::class.java,
    )
    return widgetModelRepositoryEntrypoint.noteUseCase()
}

@Composable
fun SelectedNote(note: Note) {
    Column(
        modifier = GlanceModifier
            .background(GlanceTheme.colors.background)
            .fillMaxSize()
    ) {
        Text(
            text = note.name,
            style = TextStyle(color = GlanceTheme.colors.primary)
        )
        Text(
            text = note.description,
            style = TextStyle(color = GlanceTheme.colors.primary)
        )
    }
}
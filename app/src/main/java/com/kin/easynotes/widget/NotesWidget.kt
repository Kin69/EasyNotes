package com.kin.easynotes.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.kin.easynotes.domain.model.Note
import com.kin.easynotes.domain.usecase.NoteUseCase
import com.kin.easynotes.presentation.MainActivity
import com.kin.easynotes.presentation.navigation.NavRoutes
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.io.File

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WidgetModelRepositoryEntryoint {
    fun noteUseCase() : NoteUseCase
}

fun getNoteUseCase(applicationContext: Context): NoteUseCase {
    var widgetModelRepositoryEntrypoint: WidgetModelRepositoryEntryoint = EntryPoints.get(
        applicationContext,
        WidgetModelRepositoryEntryoint::class.java,
    )
    return widgetModelRepositoryEntrypoint.noteUseCase()
}

class NotesWidget : GlanceAppWidget() {
    override val stateDefinition: GlanceStateDefinition<List<Int>>
        get() = object: GlanceStateDefinition<List<Int>> {
            override suspend fun getDataStore(
                context: Context,
                fileKey: String
            ): DataStore<List<Int>> {
                return NotesDataStore(context)
            }
            override fun getLocation(context: Context, fileKey: String): File {
                throw NotImplementedError("Not implemented")
            }
        }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val noteUseCase = getNoteUseCase(context)

        provideContent {
            GlanceTheme {
                noteUseCase.observe()
                val noteId = currentState<List<Int>>().first()
                val selectedNote = noteUseCase.notes.filter { it.id == noteId }
                when {
                    selectedNote.isEmpty() -> Box { Text(text = "Loading")}
                    else -> SelectedNote(selectedNote.first())
                }
            }
        }
    }
}


private val editScreen = ActionParameters.Key<String>(
    NavRoutes.Edit.route
)


@Composable
fun SelectedNote(note: Note) {
    val context = LocalContext.current

    Column(
        modifier = GlanceModifier
            .background(GlanceTheme.colors.background)
            .padding(6.dp)
            .fillMaxSize()
            .clickable {
                val intent = Intent(context, MainActivity::class.java).apply {
                    putExtra("noteId", note.id)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                context.startActivity(intent)
            }
    ) {
        Text(
            text = note.name,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                color = GlanceTheme.colors.primary,
                fontSize = 24.sp
            )
        )
        Text(
            text = note.description,
            style = TextStyle(color = GlanceTheme.colors.primary)
        )
    }
}

package com.kin.easynotes.widget.ui

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.action.clickable
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.kin.easynotes.domain.model.Note
import com.kin.easynotes.presentation.MainActivity


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

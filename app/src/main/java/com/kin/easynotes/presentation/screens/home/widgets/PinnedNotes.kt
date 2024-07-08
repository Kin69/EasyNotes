package com.kin.easynotes.presentation.screens.home.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kin.easynotes.R
import com.kin.easynotes.domain.model.Note
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel

@Composable
fun PinnedNotes(
    settingsViewModel: SettingsViewModel,
    containerColor: Color,
    onNoteClicked: (Int) -> Unit,
    shape : RoundedCornerShape,
    notes: List<Note>,
    onNoteUpdate: (Note) -> Unit,
    selectedNotes: MutableList<Note>,
    viewMode: Boolean,
    isDeleteClicked: Boolean,
    animationFinished: (Int) -> Unit
) {
    Column {
        Text(
            modifier = Modifier.padding(horizontal = 12.dp),
            text = stringResource(id = R.string.pinned).uppercase(),
            style = TextStyle(fontSize = 11.sp, color = MaterialTheme.colorScheme.secondary)
        )
        NotesGrid(
            settingsViewModel = settingsViewModel,
            containerColor = containerColor,
            onNoteClicked = onNoteClicked,
            notes = notes,
            shape = shape,
            onNoteUpdate = onNoteUpdate,
            selectedNotes = selectedNotes,
            viewMode = viewMode,
            isDeleteClicked = isDeleteClicked,
            animationFinished = animationFinished
        )
    }
}
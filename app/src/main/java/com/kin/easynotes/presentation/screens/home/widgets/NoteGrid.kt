package com.kin.easynotes.presentation.screens.home.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kin.easynotes.domain.model.Note
import com.kin.easynotes.presentation.components.getNoteEnterAnimation
import com.kin.easynotes.presentation.components.getNoteExitAnimation

@Composable
fun NotesGrid(
    onNoteClicked: (Int) -> Unit,
    shape : RoundedCornerShape,
    notes: List<Note>,
    onNoteUpdate: (Note) -> Unit,
    selectedNotes: MutableList<Int>,
    viewMode: Boolean,
    isDeleteClicked: Boolean,
    isSelectAvailable: Boolean,
    animationFinished: (Int) -> Unit
) {
    LazyVerticalStaggeredGrid(
        columns = when(viewMode) {
            true -> StaggeredGridCells.Fixed(2)
            false -> StaggeredGridCells.Fixed(1)
        },
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        content = {
            items(notes) { note ->
                val isAnimationVisible = rememberTransitionState()
                AnimatedVisibility(
                    visibleState = isAnimationVisible,
                    enter = getNoteEnterAnimation(),
                    exit = getNoteExitAnimation(calculateSlideDirection(notes, note))
                ) {
                    NoteCard(
                        note = note,
                        containerColor = getContainerColor(selectedNotes, note),
                        shape = shape,
                        borderColor = getBorderColor(selectedNotes, note),
                        onShortClick = { handleShortClick(selectedNotes, note, onNoteClicked) },
                        onNoteUpdate = onNoteUpdate,
                        onLongClick = { if (isSelectAvailable) handleLongClick(selectedNotes, note) }
                    )
                    if (isDeleteClicked && selectedNotes.contains(note.id)) {
                        isAnimationVisible.targetState = false
                    }
                }
                handleDeleteAnimation(selectedNotes, note, isAnimationVisible, animationFinished)
            }
        },
        modifier = Modifier.padding(12.dp)
    )
}

private fun calculateSlideDirection(notes: List<Note>, note: Note): Int {
    return if (notes.indexOf(note) % 2 == 0) -1 else 1
}

@Composable
private fun rememberTransitionState(): MutableTransitionState<Boolean> {
    return remember { MutableTransitionState(false).apply { targetState = true } }
}

@Composable
private fun getContainerColor(selectedNotes: MutableList<Int>, note: Note): Color {
    return if (selectedNotes.contains(note.id)) MaterialTheme.colorScheme.surfaceContainerHighest
    else MaterialTheme.colorScheme.surfaceContainerHigh
}

@Composable
private fun getBorderColor(selectedNotes: MutableList<Int>, note: Note): Color {
    return if (selectedNotes.contains(note.id)) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.surfaceContainerHigh
}

private fun handleShortClick(
    selectedNotes: MutableList<Int>,
    note: Note,
    onNoteClicked: (Int) -> Unit
) {
    if (selectedNotes.isNotEmpty()) {
        if (selectedNotes.contains(note.id)) {
            selectedNotes.remove(note.id)
        } else {
            selectedNotes.add(note.id)
        }
    } else {
        onNoteClicked(note.id)
    }
}

private fun handleLongClick(selectedNotes: MutableList<Int>, note: Note) {
    if (!selectedNotes.contains(note.id)) {
        selectedNotes.add(note.id)
    }
}

private fun handleDeleteAnimation(
    selectedNotes: MutableList<Int>,
    note: Note,
    isAnimationVisible: MutableTransitionState<Boolean>,
    animationFinished: (Int) -> Unit
) {
    if (!isAnimationVisible.targetState && !isAnimationVisible.currentState && selectedNotes.contains(note.id)) {
        selectedNotes.remove(note.id)
        isAnimationVisible.targetState = true
        animationFinished(note.id)
    }
}

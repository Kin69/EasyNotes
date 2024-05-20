package com.kin.easynotes.presentation.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kin.easynotes.domain.model.Note
import com.kin.easynotes.navigation.NavRoutes
import com.kin.easynotes.presentation.components.AppBarView
import com.kin.easynotes.presentation.screens.home.viewmodel.HomeViewModel
import com.kin.easynotes.presentation.screens.home.widgets.EmptyNoteList

@Composable
fun HomeView(navController: NavController) {
    val viewModel: HomeViewModel = viewModel()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        topBar = {
            AppBarView(
                titleText = "Notes",
                onDeleteClicked = if (viewModel.editMode) { { viewModel.deleteSelectedNotes(viewModel) } } else null,
                onSettingsClicked = { navController.navigate(NavRoutes.Settings.route) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(NavRoutes.Edit.route + "/0")
            }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = "Add",
                        modifier = Modifier.padding(end = 9.dp)
                    )
                    Text(text = "Add Note")
                }
            }
        }
    ) {
        Box(modifier = Modifier.padding(top = it.calculateTopPadding())) {
            NoteList(navController = navController, viewModel)
        }
    }
}

@Composable
private fun NoteList(navController: NavController, viewModel: HomeViewModel) {
    val notes = viewModel.getAllNotes.collectAsState(initial = listOf())

    when {
        notes.value.isEmpty() -> EmptyNoteList()
        else -> NotesGrid(navController = navController, viewModel = viewModel, notes = notes.value)
    }
}
@Composable
private fun NotesGrid(navController: NavController, viewModel: HomeViewModel, notes: List<Note>) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(12.dp)
    ) {
        items(notes) { note ->
            NoteCard(
                note = note,
                onItemClick = { selectedNote ->
                    if (viewModel.editMode) {
                        viewModel.toggleNoteSelection(selectedNote)
                        if (viewModel.selectedNotes.isEmpty()) viewModel.updateEditMode(false)
                    } else navController.navigate(NavRoutes.Edit.route + "/${note.id}")
                },
                onItemLongClick = { selectedNote ->
                    viewModel.toggleNoteSelection(selectedNote)
                    when {
                        viewModel.selectedNotes.isEmpty() -> viewModel.updateEditMode(false)
                        else ->  viewModel.updateEditMode(true)
                    }
                },
                isSelected = viewModel.selectedNotes.contains(note)
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun NoteCard(
    note: Note,
    onItemClick: (Note) -> Unit,
    onItemLongClick: (Note) -> Unit,
    isSelected: Boolean
) {
    Box(
        modifier = Modifier
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(9.dp))
            .combinedClickable(
                onClick = { onItemClick(note) },
                onLongClick = { onItemLongClick(note) }
            )
            .background(
                if (isSelected) MaterialTheme.colorScheme.surfaceContainerHighest else MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(9.dp)
            )
    ) {
        Column(
            modifier = Modifier.padding(6.dp)
        ) {
            Text(
                text = note.name,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 6.dp),
                maxLines = 3
            )
            Text(
                text = note.description,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
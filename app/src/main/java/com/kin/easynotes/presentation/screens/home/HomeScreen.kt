package com.kin.easynotes.presentation.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kin.easynotes.domain.model.Note
import com.kin.easynotes.domain.usecase.NoteViewModel
import com.kin.easynotes.navigation.NavRoutes
import com.kin.easynotes.presentation.components.AppBarView
import com.kin.easynotes.presentation.screens.home.viewmodel.HomeViewModel

@Composable
fun HomeView(navController: NavController) {
    val homeViewModel: HomeViewModel = viewModel()
    val noteViewModel: NoteViewModel = viewModel()

    Scaffold(
        topBar = {
            AppBarView(
                titleText = "Notes",
                onDeleteEnabled = homeViewModel.editMode,
                onDeleteClicked = { homeViewModel.deleteSelectedNotes(noteViewModel) },
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
                        modifier = Modifier.padding(end = 6.dp)
                    )
                    Text(text = "Add Note")
                }
            }
        }
    ) {
        NoteList(navController = navController, homeViewModel = homeViewModel, noteViewModel = noteViewModel, padding = it.calculateTopPadding())
    }
}

@Composable
private fun NoteList(navController: NavController, homeViewModel: HomeViewModel, noteViewModel: NoteViewModel, padding: Dp) {
    val notes = noteViewModel.getAllNotes.collectAsState(initial = listOf())
    if (notes.value.isEmpty()) {
        EmptyNoteList()
    } else {
        NotesGrid(navController = navController, homeViewModel = homeViewModel, notes = notes.value, padding = padding)
    }
}

@Composable
private fun EmptyNoteList() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No created notes.",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun NotesGrid(
    navController: NavController,
    homeViewModel: HomeViewModel,
    notes: List<Note>, padding: Dp) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .padding(start = 12.dp, end = 12.dp, top = padding)
    ) {
        items(notes) { note ->
            NoteCard(
                color = MaterialTheme.colorScheme.onPrimary,
                note = note,
                onItemClick = { selectedNote ->
                    if (homeViewModel.editMode) {
                        homeViewModel.toggleNoteSelection(selectedNote)
                        if (homeViewModel.selectedNotes.isEmpty()) {
                            homeViewModel.updateEditMode(false)
                        }
                    } else {
                        navController.navigate(NavRoutes.Edit.route + "/${note.id}")
                    }
                },
                onItemLongClick = { selectedNote ->
                    homeViewModel.toggleNoteSelection(selectedNote)
                    if (homeViewModel.selectedNotes.isEmpty()) {
                        homeViewModel.updateEditMode(false)
                    } else {
                        homeViewModel.updateEditMode(true)
                    }
                },
                isSelected = homeViewModel.selectedNotes.contains(note)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun NoteCard(
    color: Color,
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
            ).fillMaxWidth()
            .background(
                if (isSelected) MaterialTheme.colorScheme.onSecondary else color,
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
                maxLines = 5
            )
            Text(
                text = note.description,
                modifier = Modifier.padding(start = 3.dp),
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
package com.kin.easynotes.presentation.screens.home

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kin.easynotes.domain.model.Note
import com.kin.easynotes.navigation.NavRoutes
import com.kin.easynotes.presentation.components.AppBarView
import com.kin.easynotes.presentation.components.NotesButton
import com.kin.easynotes.presentation.components.NotesScaffold
import com.kin.easynotes.presentation.screens.home.viewmodel.HomeViewModel
import com.kin.easynotes.presentation.screens.home.widgets.EmptyNoteList
import java.util.Locale

@Composable
fun HomeView(navController: NavController) {
    val viewModel: HomeViewModel = viewModel()
    NotesScaffold(
        topBar = {
            AppBarView(
                titleText = "Notes",
                onDeleteClicked = if (viewModel.isSelectingMode.value) { { viewModel.toggleIsDeleteMode(true) } } else null,
                onSettingsClicked = { navController.navigate(NavRoutes.Settings.route) }
            )
        },
        floatingActionButton = {
            NotesButton(
                icon = Icons.Rounded.Add,
                text = "New Note",
                onClick = { navController.navigate(NavRoutes.Edit.route + "/0") }
            )
        }
    ) {
        NoteList(navController = navController, viewModel)
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
@SuppressLint("UnrememberedMutableState")
@Composable
private fun NotesGrid(navController: NavController, viewModel: HomeViewModel, notes: List<Note>) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(12.dp)
    ) {
        items(notes) { note ->
            val slideDirection = if (notes.indexOf(note) % 2 == 0) -1 else 1
            val animVisibleState = remember {  MutableTransitionState(false).apply {  targetState = true  }  }
            AnimatedVisibility(
                visibleState = animVisibleState,
                enter =  fadeIn(animationSpec = tween(200)) +
                        scaleIn(
                            initialScale = 0.9f,
                            animationSpec = tween(200)
                        ),
                exit = slideOutHorizontally(
                    targetOffsetX = { slideDirection * it },
                    animationSpec = tween(durationMillis = 300)
                ) + fadeOut(animationSpec = tween(durationMillis = 300))
            )  {
                NoteCard(
                    note = note,
                    containerColor = when {
                        viewModel.selectedNotes.contains(note.id) -> MaterialTheme.colorScheme.surfaceContainerHighest
                        else ->  MaterialTheme.colorScheme.surfaceContainerHigh
                    },
                    onShortClick = {
                        when {
                            viewModel.isSelectingMode.value -> viewModel.toggleNoteSelection(note.id)
                            else -> navController.navigate(NavRoutes.Edit.route + "/${note.id}")
                        }
                    },
                    onLongClick = {
                        viewModel.toggleIsSelectingMode(true)
                        viewModel.toggleNoteSelection(note.id)
                    }
                )
                if (viewModel.isDeleteMode.value && viewModel.selectedNotes.contains(note.id)) {
                    animVisibleState.targetState = false
                }
            }
            if (!animVisibleState.targetState && !animVisibleState.currentState && viewModel.selectedNotes.contains(note.id)) {
                viewModel.toggleNoteSelection(note.id)
                animVisibleState.targetState = true
                viewModel.deleteNoteById(note.id)
            }
        }
    }
}



@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun NoteCard(note: Note, containerColor : Color, onShortClick : () -> Unit, onLongClick : () -> Unit) {
    Box(
        modifier = Modifier
            .padding(bottom = 9.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(containerColor)
            .combinedClickable(
                onClick = { onShortClick() },
                onLongClick = { onLongClick() }
            )
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = note.name.replaceFirstChar { it.titlecase(Locale.ROOT)},
                fontWeight = FontWeight.Bold,
                maxLines = 3
            )
            Text(
                text = note.description.replaceFirstChar { it.titlecase(Locale.ROOT)},
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

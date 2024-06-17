package com.kin.easynotes.presentation.screens.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kin.easynotes.R
import com.kin.easynotes.presentation.components.*
import com.kin.easynotes.presentation.screens.home.viewmodel.HomeViewModel
import com.kin.easynotes.presentation.screens.home.widgets.NoteFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    viewModel: HomeViewModel = viewModel(),
    onSettingsClicked: () -> Unit,
    onNoteClicked: (Int) -> Unit,
    onSearchClicked: () -> Unit
) {
    NotesScaffold(
        topBar = {
            when (viewModel.selectedNotes.isNotEmpty()) {
                true -> TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    title = {
                        TitleText(
                            titleText = viewModel.selectedNotes.size.toString()
                        )
                    },
                    navigationIcon = {
                        CloseButton { viewModel.selectedNotes.clear() }
                    },
                    actions = {
                        DeleteButton { viewModel.toggleIsDeleteMode(true) }
                    }
                )
                false -> TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    ),
                    title = {
                        TitleText(
                            titleText = stringResource(id = R.string.home_screen)
                        )
                    },
                    actions = {
                        SearchButton { onSearchClicked() }
                        SettingsButton { onSettingsClicked() }
                    },
                )
            }
        },
        floatingActionButton = {
            NotesButton(
                text = stringResource(R.string.new_note),
                onClick = { onNoteClicked(0) }
            )
        },
        content = {
            val allNotes = viewModel.noteUseCase.getAllNotes.collectAsState(initial = listOf()).value
            NoteFilter(
                onNoteClicked = onNoteClicked,
                notes = allNotes,
                selectedNotes = viewModel.selectedNotes,
                isDeleteMode = viewModel.isDeleteMode.value,
                onNoteUpdate = { note -> viewModel.noteUseCase.addNote(note) },
                isSelectAvailable = true,
                onDeleteNote = {
                    viewModel.toggleIsDeleteMode(false)
                    viewModel.noteUseCase.deleteNoteById(it)
                },
            )
        }
    )
}

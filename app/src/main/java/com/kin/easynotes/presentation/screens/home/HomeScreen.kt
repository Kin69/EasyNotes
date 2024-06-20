package com.kin.easynotes.presentation.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.SelectAll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kin.easynotes.R
import com.kin.easynotes.presentation.components.CloseButton
import com.kin.easynotes.presentation.components.MoreButton
import com.kin.easynotes.presentation.components.NotesButton
import com.kin.easynotes.presentation.components.NotesScaffold
import com.kin.easynotes.presentation.components.SearchButton
import com.kin.easynotes.presentation.components.SettingsButton
import com.kin.easynotes.presentation.components.TitleText
import com.kin.easynotes.presentation.screens.home.viewmodel.HomeViewModel
import com.kin.easynotes.presentation.screens.home.widgets.NoteFilter
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    viewModel: HomeViewModel = viewModel(),
    settingsModel: SettingsViewModel,
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
                        val allNotes = viewModel.noteUseCase.getAllNotes.collectAsState(initial = listOf()).value

                        Box {
                            MoreButton {
                                viewModel.toggleSelectMenu(true)
                            }

                            DropdownMenu(
                                expanded = viewModel.isSelectMenuOpened.value,
                                onDismissRequest = { viewModel.toggleSelectMenu(false) }
                            ) {
                                if (viewModel.selectedNotes.size != allNotes.size) {
                                    DropdownMenuItem(
                                        leadingIcon = { Icon(Icons.Rounded.SelectAll, contentDescription = "Select all")},
                                        text = { Text(stringResource(id = R.string.select_all)) },
                                        onClick = {
                                            allNotes.forEach {
                                                if (!viewModel.selectedNotes.contains(it.id)) {
                                                    viewModel.selectedNotes.add(it.id)
                                                }
                                            }
                                        }
                                    )
                                }

                                DropdownMenuItem(
                                    leadingIcon = { Icon(Icons.Rounded.Delete, contentDescription = "Delete")},
                                    text = { Text(stringResource(id = R.string.delete)) },
                                    onClick = {
                                        viewModel.toggleSelectMenu(false)
                                        viewModel.toggleIsDeleteMode(true)
                                    }
                                )
                            }
                        }
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
                        if (viewModel.noteUseCase.getAllNotes.collectAsState(initial = listOf()).value.isNotEmpty()) {
                            SearchButton { onSearchClicked() }
                        }
                        SettingsButton {
                            onSettingsClicked()
                        }
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
                shape = RoundedCornerShape(settingsModel.settings.value.cornerRadius.dp),
                onNoteClicked = onNoteClicked,
                notes = allNotes,
                selectedNotes = viewModel.selectedNotes,
                viewMode = settingsModel.settings.value.viewMode,
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

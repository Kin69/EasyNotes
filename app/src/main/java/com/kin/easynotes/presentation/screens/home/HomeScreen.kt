package com.kin.easynotes.presentation.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kin.easynotes.R
import com.kin.easynotes.domain.model.Note
import com.kin.easynotes.presentation.components.CloseButton
import com.kin.easynotes.presentation.components.DeleteButton
import com.kin.easynotes.presentation.components.NotesButton
import com.kin.easynotes.presentation.components.NotesScaffold
import com.kin.easynotes.presentation.components.PinButton
import com.kin.easynotes.presentation.components.SelectAllButton
import com.kin.easynotes.presentation.components.SettingsButton
import com.kin.easynotes.presentation.components.TitleText
import com.kin.easynotes.presentation.components.VaultButton
import com.kin.easynotes.presentation.components.VoiceNotesButton
import com.kin.easynotes.presentation.components.defaultScreenEnterAnimation
import com.kin.easynotes.presentation.components.defaultScreenExitAnimation
import com.kin.easynotes.presentation.screens.home.viewmodel.HomeViewModel
import com.kin.easynotes.presentation.screens.home.widgets.NoteFilter
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel
import com.kin.easynotes.presentation.screens.settings.settings.PasswordPrompt
import com.kin.easynotes.presentation.screens.settings.settings.shapeManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun HomeView (
    viewModel: HomeViewModel = hiltViewModel(),
    settingsModel: SettingsViewModel,
    onSettingsClicked: () -> Unit,
    onNoteClicked: (Int, Boolean) -> Unit,
    onVoiceNotesClicked: () -> Unit
) {
    val context = LocalContext.current
    if (viewModel.isPasswordPromptVisible.value) {
        PasswordPrompt(
            context = context,
            text = stringResource(id = R.string.password_continue),
            settingsViewModel = settingsModel,
            onExit = { password ->
                if (password != null) {
                    if (password.text.isNotBlank()) {
                        viewModel.encryptionHelper.setPassword(password.text)
                        viewModel.noteUseCase.observe()
                    }
                }
                viewModel.toggleIsPasswordPromptVisible(false)
            }
        )
    }

    if (settingsModel.databaseUpdate.value) viewModel.noteUseCase.observe()
    val containerColor = getContainerColor(settingsModel)
    NotesScaffold(
        floatingActionButton = { NewNoteButton { onNoteClicked(it, viewModel.isVaultMode.value) } },
        topBar = {
            AnimatedVisibility(
                visible = viewModel.selectedNotes.isNotEmpty(),
                enter = defaultScreenEnterAnimation(),
                exit = defaultScreenExitAnimation()
            ) {
                SelectedNotesTopAppBar(
                    selectedNotes = viewModel.selectedNotes,
                    allNotes = viewModel.getAllNotes(),
                    settingsModel = settingsModel,
                    onPinClick = { viewModel.pinOrUnpinNotes() },
                    onDeleteClick = { viewModel.toggleIsDeleteMode(true) },
                    onSelectAllClick = { selectAllNotes(viewModel, viewModel.getAllNotes()) },
                    onCloseClick = { viewModel.selectedNotes.clear() }
                )
            }
            AnimatedVisibility(
                viewModel.selectedNotes.isEmpty(),
                enter = defaultScreenEnterAnimation(),
                exit = defaultScreenExitAnimation()
            ) {
                NotesSearchBar(
                    settingsModel = settingsModel,
                    query = viewModel.searchQuery.value,
                    onQueryChange = { viewModel.changeSearchQuery(it) },
                    onSettingsClick = onSettingsClicked,
                    onClearClick = { viewModel.changeSearchQuery("") },
                    viewModel = viewModel,
                    onVaultClicked = {
                        if (!viewModel.isVaultMode.value) {
                            viewModel.toggleIsPasswordPromptVisible(true)
                        } else {
                            viewModel.toggleIsVaultMode(false)
                            viewModel.encryptionHelper.removePassword()
                        }
                    },
                    onVoiceNotesClicked = onVoiceNotesClicked
                )
            }
        },
        content = {
            NoteFilter(
                settingsViewModel = settingsModel,
                containerColor = containerColor,
                shape = shapeManager(
                    radius = settingsModel.settings.value.cornerRadius / 2,
                    isBoth = true
                ),
                onNoteClicked = { onNoteClicked(it, viewModel.isVaultMode.value)  },
                notes = viewModel.getAllNotes().sortedWith(sorter(settingsModel.settings.value.sortDescending)),
                selectedNotes = viewModel.selectedNotes,
                viewMode = settingsModel.settings.value.viewMode,
                searchText = viewModel.searchQuery.value.ifBlank { null },
                isDeleteMode = viewModel.isDeleteMode.value,
                onNoteUpdate = { note -> CoroutineScope(Dispatchers.IO).launch {viewModel.noteUseCase.addNote(note) } },
                onDeleteNote = {
                    viewModel.toggleIsDeleteMode(false)
                    viewModel.noteUseCase.deleteNoteById(it)
                },
            )
        }
    )
}

@Composable
fun getContainerColor(settingsModel: SettingsViewModel): Color {
    return if (settingsModel.settings.value.extremeAmoledMode) Color.Black else MaterialTheme.colorScheme.surfaceContainerHigh
}

@Composable
private fun NewNoteButton(onNoteClicked: (Int) -> Unit) {
    NotesButton(text = stringResource(R.string.new_note)) {
        onNoteClicked(0)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectedNotesTopAppBar(
    selectedNotes: List<Note>,
    allNotes: List<Note>,
    settingsModel: SettingsViewModel,
    onPinClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onSelectAllClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    var deletelaert by remember {
        mutableStateOf(false)
    }
    AnimatedVisibility(visible = deletelaert) {
        AlertDialog(onDismissRequest = { deletelaert = false }, title = {
            Text(
                text = stringResource(id = R.string.alert_text)
            )
        }, confirmButton = {
            TextButton(onClick = { deletelaert=false
                onDeleteClick()
            }) {
                Text(text = stringResource(id = R.string.yes), color = MaterialTheme.colorScheme.error )
            }
        },
            dismissButton = {
                TextButton(onClick = { deletelaert = false }) {
                    Text(text =stringResource(id = R.string.cancel))
                }
            })

    }
    TopAppBar(
        modifier = Modifier.padding(bottom = 36.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = if (!settingsModel.settings.value.extremeAmoledMode)
                MaterialTheme.colorScheme.surfaceContainerLow
            else Color.Black
        ),
        title = { TitleText(titleText = selectedNotes.size.toString()) },
        navigationIcon = { CloseButton(onCloseClicked = onCloseClick) },
        actions = {
            Row {
                PinButton(isPinned = selectedNotes.all { it.pinned }, onClick = onPinClick)
                DeleteButton(onClick =  {deletelaert = true})
                SelectAllButton(
                    enabled = selectedNotes.size != allNotes.size,
                    onClick = onSelectAllClick
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotesSearchBar(
    settingsModel: SettingsViewModel,
    viewModel: HomeViewModel,
    query: String,
    onQueryChange: (String) -> Unit,
    onSettingsClick: () -> Unit,
    onVaultClicked: () -> Unit,
    onClearClick: () -> Unit,
    onVoiceNotesClicked: () -> Unit
) {
    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = if (settingsModel.settings.value.makeSearchBarLonger) 16.dp else 36.dp, vertical =  18.dp),
        query = query,
        placeholder = { Text(stringResource(R.string.search)) },
        leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = "Search") },
        trailingIcon = {
            Row {
                if (query.isNotBlank()) {
                    CloseButton(contentDescription = "Clear", onCloseClicked = onClearClick)
                }
                VoiceNotesButton(onClick = onVoiceNotesClicked)
                if (settingsModel.settings.value.vaultSettingEnabled) {
                    VaultButton(viewModel.isVaultMode.value) { onVaultClicked() }
                }
                SettingsButton(onSettingsClicked = onSettingsClick)
            }
        },
        onQueryChange = onQueryChange,
        onSearch = onQueryChange,
        onActiveChange = {},
        active = false,
    ) {}
}

private fun selectAllNotes(viewModel: HomeViewModel, allNotes: List<Note>) {
    allNotes.forEach {
        if (!viewModel.selectedNotes.contains(it)) {
            viewModel.selectedNotes.add(it)
        }
    }
}

fun sorter(descending: Boolean): Comparator<Note> {
    return if (descending) {
        compareByDescending { it.createdAt }
    } else {
        compareBy { it.createdAt }
    }
}

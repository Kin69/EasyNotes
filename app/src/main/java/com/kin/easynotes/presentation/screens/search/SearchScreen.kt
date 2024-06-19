package com.kin.easynotes.presentation.screens.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kin.easynotes.R
import com.kin.easynotes.presentation.components.NavigationIcon
import com.kin.easynotes.presentation.components.NotesScaffold
import com.kin.easynotes.presentation.components.TitleText
import com.kin.easynotes.presentation.screens.edit.components.CustomTextField
import com.kin.easynotes.presentation.screens.home.widgets.NoteFilter
import com.kin.easynotes.presentation.screens.search.viewmodel.SearchViewModel
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onNoteClicked: (Int) -> Unit,
    onBackNavClicked: () -> Unit,
    viewModel: SearchViewModel = viewModel() ,
    settingsModel: SettingsViewModel
) {
    NotesScaffold(
        topBar = {
            TopAppBar(
                navigationIcon = { NavigationIcon { onBackNavClicked() } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
                title = { TitleText(titleText = stringResource(R.string.screen_search)) },
            )
        },
        content = {
            val focusRequester = remember { FocusRequester() }
            Column {
                CustomTextField(
                    value = viewModel.value.value,
                    onValueChange = {viewModel.updateValue(it)},
                    placeholder = stringResource(R.string.search) + "...",
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .padding(12.dp, 0.dp, 12.dp, 8.dp)
                )
                LaunchedEffect(true) {
                    focusRequester.requestFocus()
                }
                val allNotes = viewModel.noteUseCase.getAllNotes.collectAsState(initial = listOf()).value
                NoteFilter(
                    onNoteClicked = onNoteClicked,
                    notes = allNotes,
                    selectedNotes = viewModel.selectedNotes,
                    viewMode = settingsModel.settings.value.viewMode,
                    isDeleteMode = viewModel.isDeleteMode.value,
                    isSelectAvailable = false,
                    onNoteUpdate = { note -> viewModel.noteUseCase.addNote(note) },
                    onDeleteNote = {
                        viewModel.toggleIsDeleteMode(false)
                        viewModel.noteUseCase.deleteNoteById(it)
                    },
                    searchText = viewModel.value.value.text
                )
            }
        }
    )
}
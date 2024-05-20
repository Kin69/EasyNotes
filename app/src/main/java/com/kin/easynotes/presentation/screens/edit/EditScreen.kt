package com.kin.easynotes.presentation.screens.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kin.easynotes.domain.model.Note
import com.kin.easynotes.presentation.components.AppBarView
import com.kin.easynotes.presentation.screens.edit.model.EditViewModel


@Composable
fun EditNoteView(
    navController: NavController,
    id: Int
) {
    val viewModel : EditViewModel = viewModel()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun onStop() {
                if (!viewModel.noteDeleteState) {
                    saveNote(viewModel, id)
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    if (id != 0 && !viewModel.noteDeleteState) {
        val note = viewModel.getNoteById(id)
            .collectAsState(initial = Note(id = 0, name = "", description = ""))
        viewModel.updateNoteNameState(note.value.name)
        viewModel.updateNoteNameDescription(note.value.description)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        topBar = {
            AppBarView(
                titleText = if (id != 0) "Edit"  else "Create Note",
                onBackNavClicked = { navController.navigateUp() },
                onDeleteEnabled = true,
                onDeleteClicked = {
                    viewModel.updateNoteDeleteState(true)
                    navController.navigateUp()
                    if (id != 0) {
                        viewModel.deleteNoteById(id)
                    }
                },
                onSaveEnabled = id == 0,
                onSaveClicked = {
                    navController.navigateUp()
                }
            )
        },
        content = {
            EditNoteContent(viewModel = viewModel, padding = it.calculateTopPadding())
        }
    )
}

@Composable
private fun EditNoteContent(
    viewModel: EditViewModel,
    padding: Dp,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = padding)
            .padding(horizontal = 16.dp)
    ) {
        CustomTextField(
            value = viewModel.noteNameState,
            onValueChange = {
                viewModel.updateNoteNameState(it)
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = "Name",
            RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        )
        CustomTextField(
            value = viewModel.noteDescriptionState,
            onValueChange = {
                viewModel.updateNoteNameDescription(it)
            },
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .padding(top = 1.dp, bottom = 16.dp),
            placeholder = "Description",
            RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
        )
    }
}
fun saveNote(
    viewModel: EditViewModel,
    id: Int
) {
    if (id != 0) {
        if (viewModel.noteNameState.isNotEmpty() && viewModel.noteDescriptionState.isNotEmpty()) {
            viewModel.updateNote(
                Note(
                    id = id,
                    name = viewModel.noteNameState,
                    description = viewModel.noteDescriptionState
                )
            )
        }
    } else {
        if (viewModel.noteNameState.isNotEmpty() && viewModel.noteDescriptionState.isNotEmpty()) {
            viewModel.addNote(
                Note(
                    name = viewModel.noteNameState,
                    description = viewModel.noteDescriptionState
                )
            )
        }
    }
}

@Composable
private fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String,
    shape: RoundedCornerShape
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        shape = shape,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.surfaceContainerLow,
            focusedIndicatorColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
        placeholder = {
            Text(placeholder)
        }
    )
}
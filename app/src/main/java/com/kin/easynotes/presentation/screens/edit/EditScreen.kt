package com.kin.easynotes.presentation.screens.edit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kin.easynotes.domain.model.Note
import com.kin.easynotes.presentation.components.AppBarView
import com.kin.easynotes.presentation.screens.edit.model.EditViewModel


@Composable
fun EditNoteView(navController: NavController, id: Int) {
    val viewModel : EditViewModel = viewModel()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP && !viewModel.noteDeleteState) {
                saveNote(viewModel, id)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    if (id != 0 && !viewModel.noteDeleteState) {
        val note = viewModel.getNoteById(id).collectAsState(initial = Note(id = 0, name = "", description = ""))
        viewModel.updateNoteNameState(note.value.name)
        viewModel.updateNoteNameDescription(note.value.description)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        topBar = {
            AppBarView(
                titleText = if (id != 0) "Edit"  else "Create Note",
                onBackNavClicked = { navController.navigateUp() },
                onDeleteClicked = {
                    viewModel.updateNoteDeleteState(true)
                    navController.navigateUp()
                    if (id != 0) viewModel.deleteNoteById(id)
                },
                onSaveClicked = if (id == 0) { { navController.navigateUp() } } else null
            )
        })
    {
        Box(modifier = Modifier.padding(top = it.calculateTopPadding())) {
            EditNoteContent(viewModel = viewModel)
        }
    }
}

@Composable
private fun EditNoteContent(viewModel: EditViewModel) {
    Column(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .imePadding()
    ) {
        CustomTextField(
            value = viewModel.noteNameState,
            onValueChange = { viewModel.updateNoteNameState(it) },
            placeholder = "Name",
            RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        )
        CustomTextField(
            value = viewModel.noteDescriptionState,
            onValueChange = { viewModel.updateNoteNameDescription(it) },
            placeholder = "Description",
            RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
            modifier = Modifier
                .fillMaxHeight()
        )
    }
}
fun saveNote(
    viewModel: EditViewModel,
    id: Int
) {
    if (viewModel.noteNameState.isNotEmpty() || viewModel.noteDescriptionState.isNotEmpty()) {
        when (id) {
            0 -> viewModel.addNote(Note(name = viewModel.noteNameState, description = viewModel.noteDescriptionState))
            else -> viewModel.updateNote(Note(id = id, name = viewModel.noteNameState, description = viewModel.noteDescriptionState))
        }
    }
}

@Composable
private fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    shape: RoundedCornerShape,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
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
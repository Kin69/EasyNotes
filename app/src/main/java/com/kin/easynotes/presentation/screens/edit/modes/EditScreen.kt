package com.kin.easynotes.presentation.screens.edit.modes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FormatBold
import androidx.compose.material.icons.rounded.FormatItalic
import androidx.compose.material.icons.rounded.FormatListNumbered
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.kin.easynotes.domain.model.Note
import com.kin.easynotes.presentation.screens.edit.components.CustomTextField
import com.kin.easynotes.presentation.screens.edit.components.TextFormattingToolbar
import com.kin.easynotes.presentation.screens.edit.model.EditViewModel

@Composable
fun EditScreen(viewModel: EditViewModel, id : Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp,16.dp, 16.dp, 0.dp)
            .imePadding()
    ) {
        CustomTextField(
            value = viewModel.noteNameState.value,
            onValueChange = { viewModel.updateNoteNameState(it) },
            placeholder = "Name",
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        )
        CustomTextField(
            value = viewModel.noteDescriptionState.value,
            onValueChange = { viewModel.updateNoteNameDescription(it) },
            placeholder = "Description",
            shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
            modifier = Modifier
                .fillMaxHeight(0.92f)
                .padding(bottom = 8.dp, top = 2.dp)
        )
        TextFormattingToolbar(viewModel)
    }
}

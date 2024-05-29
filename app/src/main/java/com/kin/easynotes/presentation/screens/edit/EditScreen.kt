package com.kin.easynotes.presentation.screens.edit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.RemoveRedEye
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kin.easynotes.domain.model.Note
import com.kin.easynotes.presentation.components.NavigationIcon
import com.kin.easynotes.presentation.components.NotesScaffold
import com.kin.easynotes.presentation.components.SaveButton
import com.kin.easynotes.presentation.components.TitleText
import com.kin.easynotes.presentation.screens.edit.components.CustomTextField
import com.kin.easynotes.presentation.screens.edit.components.MarkdownText
import com.kin.easynotes.presentation.screens.edit.components.TextFormattingToolbar
import com.kin.easynotes.presentation.screens.edit.model.EditViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun EditNoteView(
    id : Int,
    viewModel : EditViewModel = viewModel(),
    onClickBack : () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val emptyNote = Note( 0, "", "")
    val pagerState = rememberPagerState(initialPage = if (id == 0) 0 else 1, pageCount = { 2 })
    val note = (viewModel.getNoteById(id).collectAsState(emptyNote).value ?: emptyNote).let {
        viewModel.updateNoteNameState(TextFieldValue(it.name))
        viewModel.updateNoteDescriptionState(TextFieldValue(it.description))
    }



    NotesScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
                title = { if (pagerState.currentPage == 0) TitleText(titleText = "Edit") else TitleText(titleText = "Preview") },
                navigationIcon = { NavigationIcon { onClickBack() } },
                actions = {
                    if (pagerState.currentPage == 0) {
                        SaveButton { onClickBack() }
                    }
                    if (pagerState.currentPage == 1) {
                        IconButton(onClick = {
                            viewModel.noteInfoState.value = true
                        }) {
                            Icon(Icons.Rounded.MoreVert, contentDescription = "Info")
                        }
                    }
                }
            )
        },
        content = {
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
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> {
                        EditScreen(viewModel = viewModel)
                    }
                    1 -> {
                        PreviewScreen(viewModel = viewModel) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(0)
                            }
                        }
                        keyboardController?.hide()
                    }
                }
            }
        }
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomModal(viewModel: EditViewModel) {
    ModalBottomSheet(onDismissRequest = {
        viewModel.noteInfoState.value = false
    }) {
        Column {
            Row(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Words",
                    modifier = Modifier.weight(1f)
                )
                Text(text = viewModel.noteDescriptionState.value.text.split("\\s+".toRegex()).size.toString())
            }
            Row(
                modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 32.dp)
            ) {
                Text(
                    text = "Characters",
                    modifier = Modifier.weight(1f)
                )
                Text(viewModel.noteDescriptionState.value.text.length.toString())

            }
        }
    }
}

fun saveNote(viewModel: EditViewModel, id: Int) {
    if (viewModel.noteNameState.value.text.isNotEmpty() || viewModel.noteDescriptionState.value.text.isNotEmpty()) {
        when {
            viewModel.noteNameState.value.text.isEmpty() ->  viewModel.updateNoteNameState(TextFieldValue("Undefined"))
            viewModel.noteDescriptionState.value.text.isEmpty() ->  viewModel.updateNoteDescriptionState(TextFieldValue("Undefined"))
        }
        when (id) {
            0 -> viewModel.addNote(Note(name = viewModel.noteNameState.value.text ,description = viewModel.noteDescriptionState.value.text))
            else -> viewModel.updateNote(Note(id = id, name = viewModel.noteNameState.value.text, description = viewModel.noteDescriptionState.value.text))
        }
    }
}

@Composable
fun EditScreen(viewModel: EditViewModel) {
    val focusRequester = remember { FocusRequester() }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(16.dp, 0.dp, 16.dp, 0.dp)
            .imePadding()
    ) {
        CustomTextField(
            value = viewModel.noteNameState.value,
            onValueChange = { viewModel.updateNoteNameState(it) },
            placeholder = "Name",
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            modifier = Modifier.focusRequester(focusRequester)
        )
        CustomTextField(
            value = viewModel.noteDescriptionState.value,
            onValueChange = { viewModel.updateNoteDescriptionState(it) },
            placeholder = "Description",
            shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
            modifier = Modifier
                .fillMaxHeight(if (WindowInsets.ime.getBottom(LocalDensity.current) > 0) 0.90f else 1f)
                .padding(
                    bottom = if (WindowInsets.ime.getBottom(LocalDensity.current) > 0) 0.dp else 16.dp,
                    top = 2.dp
                )
        )
        if (WindowInsets.ime.getBottom(LocalDensity.current) > 0) TextFormattingToolbar(viewModel)
    }
}

@Composable
fun PreviewScreen(viewModel: EditViewModel, onClick: () -> Unit) {
    if (viewModel.noteInfoState.value) BottomModal(viewModel)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(16.dp, 0.dp, 16.dp, 0.dp)
            .imePadding()
    ) {
        MarkdownText(
            markdown = viewModel.noteNameState.value.text,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
                .padding(14.dp)
                .clickable { onClick() },
            onContentChange = { viewModel.updateNoteNameState(TextFieldValue(text = it)) }
        )
        Spacer(modifier = Modifier.height(3.dp))
        MarkdownText(
            markdown = viewModel.noteDescriptionState.value.text,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
                .padding(16.dp)
                .fillMaxHeight(0.98f)
                .clickable { onClick() },
            onContentChange = { viewModel.updateNoteDescriptionState(TextFieldValue(text = it)) }
        )
    }
}


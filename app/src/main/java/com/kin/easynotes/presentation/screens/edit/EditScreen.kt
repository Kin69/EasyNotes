package com.kin.easynotes.presentation.screens.edit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.RemoveRedEye
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kin.easynotes.domain.model.Note
import com.kin.easynotes.presentation.components.NavigationIcon
import com.kin.easynotes.presentation.components.NotesScaffold
import com.kin.easynotes.presentation.components.SaveButton
import com.kin.easynotes.presentation.screens.edit.components.CustomIconButton
import com.kin.easynotes.presentation.screens.edit.components.CustomTextField
import com.kin.easynotes.presentation.screens.edit.components.MarkdownView
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
    val emptyNote : Note = Note( 0, "", "")
    val pagerState = rememberPagerState(initialPage = if (id == 0) 0 else 1, pageCount = { 2 })
    val note = (viewModel.getNoteById(id).collectAsState(emptyNote).value ?: emptyNote).let {
        viewModel.updateNoteNameState(it.name)
        viewModel.updateNoteNameDescription(it.description)
    }


    NotesScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
                title = { ModeButton(viewModel = viewModel, pagerState) },
                navigationIcon = { NavigationIcon { onClickBack() } },
                actions = {
                    if (pagerState.currentPage == 0) {
                        SaveButton { onClickBack() }
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
                        EditScreen(viewModel = viewModel, id = id)
                    }
                    1 -> {
                        PreviewScreen(viewModel = viewModel, id = id) {
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
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ModeButton(viewModel: EditViewModel, pagerState: PagerState) {
    val coroutineScope = rememberCoroutineScope()

    Row {
        CustomIconButton(
            onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(0)
                }
            },
            icon = Icons.Rounded.Edit,
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 32.dp, bottomStart = 32.dp))
                .background(
                    when (pagerState.currentPage) {
                        0 -> MaterialTheme.colorScheme.surfaceContainerHighest
                        else -> MaterialTheme.colorScheme.surfaceContainerHigh
                    }
                )
        )
        CustomIconButton(
            onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(1)
                }
            },
            icon = Icons.Rounded.RemoveRedEye,
            modifier = Modifier
                .clip(RoundedCornerShape(bottomEnd = 32.dp, topEnd = 32.dp))
                .background(
                    when (pagerState.currentPage) {
                        1 -> MaterialTheme.colorScheme.surfaceContainerHighest
                        else -> MaterialTheme.colorScheme.surfaceContainerHigh
                    }
                )
        )
    }
}

fun saveNote(viewModel: EditViewModel, id: Int) {
    if (viewModel.noteNameState.value.isNotEmpty() || viewModel.noteDescriptionState.value.isNotEmpty()) {
        when {
            viewModel.noteNameState.value.isEmpty() ->  viewModel.updateNoteNameState("Undefined")
            viewModel.noteDescriptionState.value.isEmpty() ->  viewModel.updateNoteNameDescription("Undefined")
        }
        when (id) {
            0 -> viewModel.addNote(Note(name = viewModel.noteNameState.value ,description = viewModel.noteDescriptionState.value))
            else -> viewModel.updateNote(Note(id = id, name = viewModel.noteNameState.value, description = viewModel.noteDescriptionState.value))
        }
    }
}

@Composable
fun EditScreen(viewModel: EditViewModel, id : Int) {
    val focusRequester = remember { FocusRequester() }
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
            modifier = Modifier.focusRequester(focusRequester)
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

@Composable
fun PreviewScreen(viewModel: EditViewModel, id: Int, onClick: () -> Unit) {
    val text = """
        # Markdown Rendering with Compose
        
        - Create rich text content programmatically
        - Support headings, lists, links, and code snippets
        
        Here's a example code snippet:
        
        ```
        fun main() {
            println("Hello, Compose!")
        }

        ```
        
        [Learn more about Compose](https://developer.android.com/jetpack/compose)
        """.trimIndent()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, 16.dp, 16.dp, 0.dp)
            .imePadding()
    ) {
        MarkdownView(
            markdown = viewModel.noteNameState.value.trimIndent(),
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
                .clickable { onClick() }
        )
        Spacer(modifier = Modifier.height(3.dp))
        MarkdownView(
            markdown = viewModel.noteDescriptionState.value.trimIndent(),
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
                .fillMaxHeight(0.98f)
                .clickable { onClick() }
        )
    }
}


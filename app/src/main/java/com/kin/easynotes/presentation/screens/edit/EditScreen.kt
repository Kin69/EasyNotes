package com.kin.easynotes.presentation.screens.edit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kin.easynotes.domain.model.Note
import com.kin.easynotes.presentation.components.MoreButton
import com.kin.easynotes.presentation.components.NavigationIcon
import com.kin.easynotes.presentation.components.NotesScaffold
import com.kin.easynotes.presentation.components.SaveButton
import com.kin.easynotes.presentation.components.TitleText
import com.kin.easynotes.presentation.screens.edit.components.CustomTextField
import com.kin.easynotes.presentation.screens.edit.components.MarkdownText
import com.kin.easynotes.presentation.screens.edit.components.TextFormattingToolbar
import com.kin.easynotes.presentation.screens.edit.model.EditViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun EditNoteView(
    id: Int,
    viewModel: EditViewModel = viewModel(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    pagerState: PagerState = rememberPagerState(initialPage = if (id == 0) 0 else 1, pageCount = { 2 }),
    onClickBack: () -> Unit
) {
    viewModel.getNoteById(id).collectAsState(Note(0,"","")).value.let {
        viewModel.syncNote(it)
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                saveNote(viewModel, id)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    NotesScaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
                title = {
                    when (pagerState.currentPage) {
                        0 -> TitleText(titleText = "Edit")
                        1 -> TitleText(titleText = "Preview")
                    }
                },
                navigationIcon = { NavigationIcon { onClickBack() } },
                actions = {
                    when (pagerState.currentPage) {
                        0 -> SaveButton { onClickBack() }
                        1 -> MoreButton { viewModel.updateInfo(true) }
                    }
                }
            )
        },
        content = {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> EditScreen(viewModel = viewModel)
                    1 -> {
                        PreviewScreen(viewModel = viewModel) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(0)
                            }
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomModal(viewModel: EditViewModel) {
    ModalBottomSheet(onDismissRequest = { viewModel.updateInfo(false) }) {
        Column {
            Row(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Words",
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = viewModel.noteDescriptionState.value.text.split("\\s+".toRegex()).size.toString())
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
            .focusRequester(focusRequester)
    ) {

        CustomTextField(
            value = viewModel.noteNameState.value,
            onValueChange = { viewModel.updateNoteNameState(it) },
            placeholder = "Name",
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
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
         TextFormattingToolbar(viewModel)
    }
}

@Composable
fun PreviewScreen(viewModel: EditViewModel, onClick: () -> Unit) {
    val focusManager = LocalFocusManager.current
    focusManager.clearFocus()
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


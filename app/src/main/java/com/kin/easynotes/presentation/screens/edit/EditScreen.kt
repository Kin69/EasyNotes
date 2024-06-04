package com.kin.easynotes.presentation.screens.edit

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Numbers
import androidx.compose.material.icons.rounded.RemoveRedEye
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.unit.dp
import androidx.lifecycle.*
import com.kin.easynotes.domain.model.Note
import com.kin.easynotes.presentation.components.*
import com.kin.easynotes.presentation.components.Makrdown.MarkdownText
import com.kin.easynotes.presentation.screens.edit.components.*
import com.kin.easynotes.presentation.screens.edit.model.EditViewModel
import com.kin.easynotes.presentation.screens.settings.widgets.SettingsBox
import kotlinx.coroutines.*
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EditNoteView(
    id: Int,
    viewModel: EditViewModel = viewModel(),
    onClickBack: () -> Unit
) {
    SetupNoteData(id = id, viewModel = viewModel)
    ObserveLifecycleEvents(viewModel, id)

    val pagerState = rememberPagerState(initialPage = if (id == 0) 0 else 1, pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()

    NotesScaffold(
        topBar = { TopBar(pagerState, coroutineScope,onClickBack, viewModel) },
        content = { PagerContent(pagerState, viewModel, coroutineScope) }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopBarActions(pagerState: PagerState, onClickBack: () -> Unit, viewModel: EditViewModel) {
    when (pagerState.currentPage) {
        0 -> {
            SaveButton { onClickBack() }
        }
        1 -> {
            MoreButton { viewModel.toggleNoteInfoVisibility(true) }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerContent(pagerState: PagerState, viewModel: EditViewModel, coroutineScope: CoroutineScope) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .imePadding()
    ) { page ->
        when (page) {
            0 -> EditScreen(viewModel)
            1 -> PreviewScreen(viewModel) {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(0)
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TopBar(pagerState: PagerState,coroutineScope: CoroutineScope, onClickBack: () -> Unit, viewModel: EditViewModel) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        title = { ModeButton(pagerState, coroutineScope) },
        navigationIcon = { NavigationIcon(onClickBack) },
        actions = { TopBarActions(pagerState,  onClickBack, viewModel) }
    )
}

@Composable
fun ObserveLifecycleEvents(viewModel: EditViewModel, id: Int) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                coroutineScope.launch {
                    viewModel.saveNote(viewModel.noteId.value)
                    if (id == 0) fetchLastNoteAndUpdate(viewModel, coroutineScope)
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
}

private suspend fun fetchLastNoteAndUpdate(viewModel: EditViewModel, coroutineScope: CoroutineScope) {
    viewModel.getLastNoteId { lastId ->
        coroutineScope.launch {
            viewModel.getNoteById(lastId?.toInt() ?: 0).collect { note ->
                viewModel.syncNote(note)
            }
        }
    }
}

@Composable
fun SetupNoteData(id: Int, viewModel: EditViewModel) {
    val note = viewModel.getNoteById(id).collectAsState(Note(
        id = 0,
        name = "",
        description = "",
    )).value
    viewModel.syncNote(note)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomModal(viewModel: EditViewModel) {
    ModalBottomSheet(
        onDismissRequest = { viewModel.toggleNoteInfoVisibility(false) }
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)) {
            SettingsBox(
                title = "Created Time",
                icon = Icons.Rounded.Numbers,
                radius = arrayOf(16.dp, 16.dp, 0.dp, 0.dp),
                customAction = {
                    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    Text(sdf.format(viewModel.noteCreatedTime.value).toString())
                }
            )
            SettingsBox(
                title = "Words",
                icon = Icons.Rounded.Numbers,
                radius = arrayOf(0.dp, 0.dp, 0.dp, 0.dp),
                customAction = {
                    Text(viewModel.noteDescription.value.text.split("\\s+".toRegex()).size.toString())
                }
            )
            SettingsBox(
                title = "Characters",
                icon = Icons.Rounded.Numbers,
                radius = arrayOf(0.dp, 0.dp, 16.dp, 16.dp),
                customAction = {
                    Text(viewModel.noteDescription.value.text.length.toString())
                }
            )
        }
    }
}

@Composable
fun EditScreen(viewModel: EditViewModel) {
    var isInFocus by remember{ mutableStateOf(false)}
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(top = 16.dp, start = 16.dp,end = 16.dp)
            .imePadding()
    ) {
        CustomTextField(
            value = viewModel.noteName.value,
            onValueChange = { viewModel.updateNoteName(it) },
            placeholder = "Name",
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        )
        Spacer(modifier = Modifier.height(2.dp))
        CustomTextField(
            value = viewModel.noteDescription.value,
            onValueChange = { viewModel.updateNoteDescription(it) },
            placeholder = "Description",
            shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
            modifier = Modifier
                .onFocusChanged { isInFocus = it.isFocused }
                .fillMaxHeight(if (isInFocus) 0.90f else 1f)
                .padding(bottom = if (isInFocus) 0.dp else 16.dp)
        )
        TextFormattingToolbar(viewModel)
    }
}

@Composable
fun PreviewScreen(viewModel: EditViewModel, onClick: () -> Unit) {
    val focusManager = LocalFocusManager.current
    focusManager.clearFocus()
    if (viewModel.isNoteInfoVisible.value) BottomModal(viewModel)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        MarkdownText(
            markdown = viewModel.noteName.value.text,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
                .padding(14.dp)
                .clickable { onClick() },
            onContentChange = { viewModel.updateNoteName(TextFieldValue(text = it)) }
        )
        Spacer(modifier = Modifier.height(3.dp))
        MarkdownText(
            markdown = viewModel.noteDescription.value.text,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
                .padding(16.dp)
                .fillMaxHeight()
                .clickable { onClick() },
            onContentChange = { viewModel.updateNoteDescription(TextFieldValue(text = it)) }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ModeButton(pagerState: PagerState,coroutineScope: CoroutineScope) {
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
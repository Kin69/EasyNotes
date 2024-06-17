package com.kin.easynotes.presentation.screens.edit

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Numbers
import androidx.compose.material.icons.rounded.RemoveRedEye
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.unit.dp
import androidx.lifecycle.*
import com.kin.easynotes.R
import com.kin.easynotes.presentation.components.*
import com.kin.easynotes.presentation.components.markdown.MarkdownText
import com.kin.easynotes.presentation.screens.edit.components.*
import com.kin.easynotes.presentation.screens.edit.model.EditViewModel
import com.kin.easynotes.presentation.screens.settings.widgets.ActionType
import com.kin.easynotes.presentation.screens.settings.widgets.SettingsBox
import kotlinx.coroutines.*
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EditNoteView(
    id: Int,
    onClickBack: () -> Unit
) {
    val viewModel: EditViewModel = viewModel()
    viewModel.setupNoteData(id)
    ObserveLifecycleEvents(viewModel)

    val pagerState = rememberPagerState(initialPage = if (id == 0) 0 else 1, pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()

    NotesScaffold(
        topBar = { TopBar(pagerState, coroutineScope, onClickBack, viewModel) },
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
            Box {
                MoreButton {
                    viewModel.toggleEditMenuVisibility(true)
                }
                DropdownMenu(
                    expanded = viewModel.isEditMenuVisible.value,
                    onDismissRequest = { viewModel.toggleEditMenuVisibility(false) }
                ) {
                    if (viewModel.noteId.value != 0) {
                        DropdownMenuItem(
                            text = { Text("Delete") },
                            leadingIcon = { Icon(Icons.Rounded.Delete, contentDescription = "Delete")},
                            onClick = {
                                viewModel.toggleEditMenuVisibility(false)
                                viewModel.deleteNote(viewModel.noteId.value)
                                onClickBack()
                            }
                        )
                    }

                    DropdownMenuItem(
                        text = { Text("Information") },
                        leadingIcon = { Icon(Icons.Rounded.Info, contentDescription = "Information")},
                        onClick = {
                            viewModel.toggleEditMenuVisibility(false)
                            viewModel.toggleNoteInfoVisibility(true)
                        }
                    )
                }
            }
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
fun ObserveLifecycleEvents(viewModel: EditViewModel) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                viewModel.saveNote(viewModel.noteId.value)
                viewModel.fetchLastNoteAndUpdate()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomModal(viewModel: EditViewModel) {
    ModalBottomSheet(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        onDismissRequest = { viewModel.toggleNoteInfoVisibility(false) }
    ) {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        Column(modifier = Modifier.padding(16.dp,0.dp,16.dp,16.dp)) {
            SettingsBox(
                title = stringResource(R.string.created_time),
                icon = Icons.Rounded.Numbers,
                shape = RoundedCornerShape(16.dp,16.dp,0.dp,0.dp),
                actionType = ActionType.TEXT,
                customText = sdf.format(viewModel.noteCreatedTime.value).toString()
            )
            SettingsBox(
                title = stringResource(R.string.words),
                icon = Icons.Rounded.Numbers,
                actionType = ActionType.TEXT,
                customText = if (viewModel.noteDescription.value.text != "") viewModel.noteDescription.value.text.split("\\s+".toRegex()).size.toString() else "0"
            )
            SettingsBox(
                title = stringResource(R.string.characters),
                icon = Icons.Rounded.Numbers,
                shape = RoundedCornerShape(0.dp,0.dp,16.dp,16.dp),
                actionType = ActionType.TEXT,
                customText = viewModel.noteDescription.value.text.length.toString()
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
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .imePadding()
    ) {
        CustomTextField(
            value = viewModel.noteName.value,
            onValueChange = { viewModel.updateNoteName(it) },
            placeholder = stringResource(R.string.name),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            modifier = Modifier.heightIn(max = 128.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        CustomTextField(
            value = viewModel.noteDescription.value,
            onValueChange = { viewModel.updateNoteDescription(it) },
            placeholder = stringResource(R.string.description),
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
            .heightIn(max = 128.dp)
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
                .heightIn(max = 128.dp)
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
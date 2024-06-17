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
import androidx.compose.ui.focus.FocusManager
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
    val focusManager = LocalFocusManager.current

    NotesScaffold(
        topBar = { TopBar(pagerState, coroutineScope,onClickBack, viewModel) },
        content = { PagerContent(pagerState, viewModel, coroutineScope, focusManager) }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopBarActions(pagerState: PagerState, onClickBack: () -> Unit, viewModel: EditViewModel) {
    when (pagerState.currentPage) {
        0 -> { SaveButton { onClickBack() } }
        1 -> { MoreButton { viewModel.toggleNoteInfoVisibility(true) } }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerContent(pagerState: PagerState, viewModel: EditViewModel, coroutineScope: CoroutineScope,focusManager : FocusManager) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .navigationBarsPadding()
            .imePadding()
    ) { page ->
        when (page) {
            0 -> EditScreen(viewModel)
            1 -> PreviewScreen(viewModel) {
                focusManager.clearFocus()
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
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        MarkdownBox(
            shape = RoundedCornerShape(32.dp,32.dp,6.dp,6.dp),
            content = {
                CustomTextField(
                    value = viewModel.noteName.value,
                    modifier = Modifier.padding(2.dp,2.dp,2.dp,0.dp),
                    onValueChange = { viewModel.updateNoteName(it) },
                    placeholder = stringResource(R.string.name),
                )
            }
        )
        MarkdownBox(
            shape = RoundedCornerShape(6.dp,6.dp,32.dp,32.dp),
            modifier = Modifier
                .onFocusChanged { isInFocus = it.isFocused }
                .fillMaxHeight(if (isInFocus) 0.92f else 1f)
                .padding(bottom = if (isInFocus) 0.dp else 16.dp),
            content = {
                CustomTextField(
                    value = viewModel.noteDescription.value,
                    onValueChange = { viewModel.updateNoteDescription(it) },
                    modifier = Modifier.fillMaxHeight(),
                    placeholder = stringResource(R.string.description),
                )
            }
        )
        TextFormattingToolbar(viewModel)
    }
}

@Composable
fun PreviewScreen(viewModel: EditViewModel, onClick: () -> Unit) {
    if (viewModel.isNoteInfoVisible.value) BottomModal(viewModel)
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
        MarkdownBox(
            modifier = Modifier.clickable { onClick() },
            shape = RoundedCornerShape(32.dp,32.dp,6.dp,6.dp),
            content = {
                MarkdownText(
                    markdown = viewModel.noteName.value.text,
                    modifier = Modifier.padding(16.dp),
                    onContentChange = { viewModel.updateNoteName(TextFieldValue(text = it)) }
                )
            }
        )
        MarkdownBox(
            shape = RoundedCornerShape(6.dp,6.dp,32.dp,32.dp),
            modifier = Modifier
                .clickable { onClick() }
                .fillMaxHeight(),
            content = {
                MarkdownText(
                    markdown = viewModel.noteDescription.value.text,
                    modifier = Modifier.padding(16.dp),
                    onContentChange = { viewModel.updateNoteDescription(TextFieldValue(text = it)) }
                )
            }
        )
    }
}

@Composable
fun MarkdownBox(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(0.dp),
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(color = MaterialTheme.colorScheme.surfaceContainerHigh,)
            .heightIn(max = 128.dp, min = 42.dp),
    ) {
        content()
    }
    Spacer(modifier = Modifier.height(3.dp))
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
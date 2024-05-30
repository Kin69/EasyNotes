package com.kin.easynotes.presentation.screens.edit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Numbers
import androidx.compose.material.icons.rounded.RemoveRedEye
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
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
import com.kin.easynotes.presentation.screens.settings.widgets.SettingsBox
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
                viewModel.saveNote(id)
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
                        0 -> {
                            EditButton {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(1)
                                }
                        }
                            SaveButton { onClickBack() } }
                        1 -> { MoreButton { viewModel.toggleNoteInfoVisibility(true) } }
                    }
                }
            )
        },
        content = {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .imePadding()
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

@Composable
fun EditButton(onClick: () -> Unit) {
    IconButton(onClick = { onClick() }) {
        Icon(imageVector = Icons.Rounded.RemoveRedEye, contentDescription = "")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomModal(
    viewModel: EditViewModel
) {
    ModalBottomSheet(
        onDismissRequest = { viewModel.toggleNoteInfoVisibility(false) }
    ) {
        Column(modifier = Modifier.padding(16.dp,0.dp,16.dp,24.dp)) {
            SettingsBox(
                title = "Words",
                icon = Icons.Rounded.Numbers,
                radius = arrayOf(16.dp,16.dp,0.dp,0.dp),
                customAction = {
                    Text(viewModel.noteDescription.value.text.split("\\s+".toRegex()).size.toString())
                }
            )
            SettingsBox(
                title = "Characters",
                icon = Icons.Rounded.Numbers,
                radius = arrayOf(0.dp,0.dp,16.dp,16.dp),
                customAction = {
                    Text(viewModel.noteDescription.value.text.length.toString())
                }
            )
        }
    }
}

@Composable
fun EditScreen(viewModel: EditViewModel) {
    val isKeyboardVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(16.dp, 0.dp, 16.dp, 0.dp)
            .imePadding()
    ) {
        CustomTextField(
            value = viewModel.noteName.value,
            onValueChange = { viewModel.updateNoteName(it) },
            placeholder = "Name",
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        )
        Spacer(modifier = Modifier.height(3.dp))
        CustomTextField(
            value = viewModel.noteDescription.value,
            onValueChange = { viewModel.updateNoteDescription(it) },
            placeholder = "Description",
            shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
            modifier = Modifier
                .fillMaxHeight(if (isKeyboardVisible) 0.90f else 1f)
                .padding(bottom = if (isKeyboardVisible) 0.dp else 16.dp)
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
            .padding(16.dp, 0.dp, 16.dp, 16.dp)
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
        Spacer(modifier = Modifier.height(4.dp))
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

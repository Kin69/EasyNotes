package com.kin.easynotes.presentation.screens.edit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.kin.easynotes.presentation.screens.edit.model.EditViewModel
import com.kin.easynotes.presentation.screens.edit.modes.EditScreen
import com.kin.easynotes.presentation.screens.edit.modes.PreviewScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun EditNoteView(
    id : Int,
    viewModel : EditViewModel = viewModel(),
    onClickBack : () -> Unit
) {
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
                        SaveButton {
                            saveNote(viewModel,id)
                            onClickBack()
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
                        EditScreen(viewModel = viewModel, id = id)
                    }
                    1 -> {
                        PreviewScreen(viewModel = viewModel, id = id)
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
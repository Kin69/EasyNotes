package com.kin.easynotes.presentation.screens.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kin.easynotes.Notes
import com.kin.easynotes.domain.usecase.viewModelFactory
import com.kin.easynotes.presentation.components.NavigationIcon
import com.kin.easynotes.presentation.components.NotesScaffold
import com.kin.easynotes.presentation.components.TitleText
import com.kin.easynotes.presentation.screens.edit.components.CustomTextField
import com.kin.easynotes.presentation.screens.home.NoteList
import com.kin.easynotes.presentation.screens.home.viewmodel.HomeViewModel
import com.kin.easynotes.presentation.screens.search.viewmodel.SearchViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = viewModel()
) {
    NotesScaffold(
        topBar = {
            TopAppBar(
                navigationIcon = { NavigationIcon { navController.navigateUp() } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
                title = { TitleText(titleText = "Search") },
            )
        },
        content = {
            val focusRequester = remember { FocusRequester() }
            Column {
                CustomTextField(
                    value = viewModel.value.value,
                    onValueChange = {viewModel.updateValue(it)},
                    placeholder = "Search...",
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .padding(12.dp, 0.dp, 12.dp, 8.dp)
                )
                LaunchedEffect(true) {
                    focusRequester.requestFocus()
                }
                NoteList(navController = navController, viewModel = viewModel, viewModel.value.value.text)
            }
        }
    )
}
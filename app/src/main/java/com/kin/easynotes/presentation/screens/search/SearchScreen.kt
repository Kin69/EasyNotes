package com.kin.easynotes.presentation.screens.search

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kin.easynotes.presentation.components.NavigationIcon
import com.kin.easynotes.presentation.components.NotesScaffold
import com.kin.easynotes.presentation.components.TitleText
import com.kin.easynotes.presentation.screens.edit.components.CustomTextField
import com.kin.easynotes.presentation.screens.home.NoteList
import com.kin.easynotes.presentation.screens.home.viewmodel.HomeViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
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
            var value by remember { mutableStateOf(TextFieldValue("")) }
            Column {
                CustomTextField(
                    value = value,
                    onValueChange = {value = it},
                    placeholder = "Search...",
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.padding(12.dp,0.dp,12.dp,8.dp)
                )
                NoteList(navController = navController, viewModel = viewModel, value.text)
            }
        }
    )
}
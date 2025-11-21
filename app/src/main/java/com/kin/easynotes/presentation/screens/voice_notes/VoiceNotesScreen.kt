package com.kin.easynotes.presentation.screens.voice_notes

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun VoiceNotesScreen(viewModel: VoiceNotesViewModel = hiltViewModel()) {
    val voiceNotes by viewModel.voiceNotes.collectAsState()
    var isRecording by remember { mutableStateOf(false) }
    val permissionState = rememberPermissionState(permission = Manifest.permission.RECORD_AUDIO)

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Voice Notes") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (permissionState.status.isGranted) {
                    isRecording = if (!isRecording) {
                        viewModel.startRecording()
                        true
                    } else {
                        viewModel.stopRecording()
                        false
                    }
                } else {
                    permissionState.launchPermissionRequest()
                }
            }) {
                Icon(imageVector = Icons.Default.Mic, contentDescription = "Record")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn {
                items(voiceNotes) { voiceNote ->
                    VoiceNoteItem(voiceNote = voiceNote, onDelete = {
                        viewModel.deleteVoiceNote(it)
                    })
                }
            }
        }
    }
}

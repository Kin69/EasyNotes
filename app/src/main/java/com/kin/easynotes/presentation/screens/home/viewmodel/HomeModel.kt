package com.kin.easynotes.presentation.screens.home.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.kin.easynotes.domain.model.Note
import com.kin.easynotes.domain.usecase.NoteUseCase
import com.kin.easynotes.presentation.components.DecryptionResult
import com.kin.easynotes.presentation.components.EncryptionHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val encryptionHelper: EncryptionHelper,
    val noteUseCase: NoteUseCase,
) : ViewModel() {
    var selectedNotes = mutableStateListOf<Note>()

    private var _isDeleteMode = mutableStateOf(false)
    val isDeleteMode: State<Boolean> = _isDeleteMode

    private var _isPasswordPromptVisible = mutableStateOf(false)
    val isPasswordPromptVisible: State<Boolean> = _isPasswordPromptVisible

    private var _isVaultMode = mutableStateOf(false)
    val isVaultMode: State<Boolean> = _isVaultMode


    private var _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    init {
        noteUseCase.observe()
    }

    fun toggleIsDeleteMode(enabled: Boolean) {
        _isDeleteMode.value = enabled
    }

    fun toggleIsVaultMode(enabled: Boolean) {
        _isVaultMode.value = enabled
        noteUseCase.observe()
    }


    fun toggleIsPasswordPromptVisible(enabled: Boolean) {
        _isPasswordPromptVisible.value = enabled
    }

    fun changeSearchQuery(newValue: String) {
        _searchQuery.value = newValue
    }

    fun pinOrUnpinNotes() {
        if (selectedNotes.all { it.pinned }) {
            selectedNotes.forEach { note ->
                val updatedNote = note.copy(pinned = false)
                noteUseCase.pinNote(updatedNote)
            }
        } else {
            selectedNotes.forEach { note ->
                val updatedNote = note.copy(pinned = true)
                noteUseCase.pinNote(updatedNote)
            }
        }

        selectedNotes.clear()
    }

    fun getAllNotes(): List<Note> {
        val allNotes = noteUseCase.notes
        val filteredNotes = allNotes.filter { it.encrypted == isVaultMode.value }
        when (noteUseCase.decryptionResult) {
            DecryptionResult.BAD_PASSWORD -> { encryptionHelper.removePassword() }
            DecryptionResult.SUCCESS -> { toggleIsVaultMode(true) }
            DecryptionResult.EMPTY -> {/* Let vault be enabled */}
            else -> { toggleIsVaultMode(false) }
        }
        return filteredNotes
    }
}

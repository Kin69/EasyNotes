package com.kin.easynotes.presentation.screens.home.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.kin.easynotes.R
import com.kin.easynotes.domain.model.Note
import com.kin.easynotes.domain.usecase.NoteUseCase
import com.kin.easynotes.presentation.components.DecryptionResult
import com.kin.easynotes.presentation.components.EncryptionHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val encryptionHelper: EncryptionHelper,
    val noteUseCase: NoteUseCase,
    @ApplicationContext private val context: Context,
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
        if (!enabled) {
            noteUseCase.decryptionResult = DecryptionResult.LOADING
        }
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
        val filteredNotes = allNotes.filter { it.encrypted == isVaultMode.value}
        when (noteUseCase.decryptionResult) {
            DecryptionResult.LOADING -> { /* Don't do anything */ }
            DecryptionResult.EMPTY -> {
                if (!encryptionHelper.isPasswordEmpty()) {
                    toggleIsVaultMode(true)
                }
            }
            DecryptionResult.BAD_PASSWORD, DecryptionResult.BLANK_DATA, DecryptionResult.INVALID_DATA -> {
                toggleIsVaultMode(false)
                Toast.makeText(context, context.getString(R.string.invalid_password), Toast.LENGTH_SHORT).show()
                encryptionHelper.removePassword()
            }
            else -> { toggleIsVaultMode(true) }
        }
        return filteredNotes
    }
}

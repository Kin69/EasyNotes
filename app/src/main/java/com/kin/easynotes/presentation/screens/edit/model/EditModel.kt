package com.kin.easynotes.presentation.screens.edit.model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kin.easynotes.domain.model.Note
import com.kin.easynotes.domain.usecase.NoteUseCase
import com.kin.easynotes.presentation.components.DecryptionResult
import com.kin.easynotes.presentation.components.EncryptionHelper
import com.kin.easynotes.presentation.screens.edit.components.UndoRedoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    private val noteUseCase: NoteUseCase,
    private val encryption: EncryptionHelper
) : ViewModel() {
    private val _noteName = mutableStateOf(TextFieldValue())
    val noteName: State<TextFieldValue> get() = _noteName

    private val _isDescriptionInFocus = mutableStateOf(false)
    val isDescriptionInFocus: State<Boolean> get() = _isDescriptionInFocus

    private val _isEncrypted = mutableStateOf(false)
    val isEncrypted: State<Boolean> get() = _isEncrypted

    private val _noteDescription = mutableStateOf(TextFieldValue())
    val noteDescription: State<TextFieldValue> get() = _noteDescription

    private val _noteId = mutableIntStateOf(0)
    val noteId: State<Int> get() = _noteId

    private val _noteCreatedTime = mutableLongStateOf(System.currentTimeMillis())
    val noteCreatedTime: State<Long> get() = _noteCreatedTime

    private val _isNoteInfoVisible = mutableStateOf(false)
    val isNoteInfoVisible: State<Boolean> get() = _isNoteInfoVisible

    private val _isEditMenuVisible = mutableStateOf(false)
    val isEditMenuVisible: State<Boolean> get() = _isEditMenuVisible

    private val _isPinned = mutableStateOf(false)
    val isPinned: State<Boolean> get() = _isPinned

    private val undoRedoState = UndoRedoState()

    fun saveNote(id: Int) {
        if (noteName.value.text.isNotEmpty() || noteDescription.value.text.isNotBlank()) {
            viewModelScope.launch {
                noteUseCase.addNote(
                    Note(
                        id = id,
                        name = noteName.value.text,
                        description = noteDescription.value.text,
                        pinned = isPinned.value,
                        encrypted = isEncrypted.value,
                        createdAt = if (noteCreatedTime.value != 0L) noteCreatedTime.value else System.currentTimeMillis(),
                    )
                )

                fetchLastNoteAndUpdate()
            }
        }
    }

    fun deleteNote(id: Int) {
        noteUseCase.deleteNoteById(id = id)
    }

    private fun syncNote(note: Note) {
        if (note.encrypted) {
            val (name, nameStatus) = encryption.decrypt(note.name)
            val (description, descriptionStatus) = encryption.decrypt(note.description)
            if (nameStatus == DecryptionResult.SUCCESS && descriptionStatus == DecryptionResult.SUCCESS) {
                updateNoteName(TextFieldValue(name!!, selection = TextRange(note.name.length)))
                updateNoteDescription(TextFieldValue(description!!, selection = TextRange(note.description.length)))
            }
        } else {
            updateNoteName(TextFieldValue(note.name, selection = TextRange(note.name.length)))
            updateNoteDescription(TextFieldValue(note.description, selection = TextRange(note.description.length)))
        }
        updateNoteCreatedTime(note.createdAt)
        updateNoteId(note.id)
        updateNotePin(note.pinned)
        updateIsEncrypted(note.encrypted)
    }

    fun setupNoteData(id : Int = noteId.value) {
        if (id != 0) {
            viewModelScope.launch {
                noteUseCase.getNoteById(id).collectLatest { note ->
                    if (note != null) {
                        syncNote(note)
                    }
                }
            }
        }
    }

    private fun fetchLastNoteAndUpdate() {
        if (noteName.value.text.isNotEmpty() || noteDescription.value.text.isNotBlank()) {
            if (noteId.value == 0) {
                viewModelScope.launch {
                    noteUseCase.getLastNoteId { lastId ->
                        viewModelScope.launch {
                            setupNoteData(lastId?.toInt() ?: 1)
                        }
                    }
                }
            }
        }
    }


    fun toggleEditMenuVisibility(value: Boolean) {
        _isEditMenuVisible.value = value
    }

    fun toggleNoteInfoVisibility(value: Boolean) {
        _isNoteInfoVisible.value = value
    }

    fun toggleIsDescriptionInFocus(value: Boolean) {
        _isDescriptionInFocus.value = value
    }

    fun toggleNotePin(value: Boolean) {
        _isPinned.value = value
    }

    fun updateNoteName(newName: TextFieldValue) {
        _noteName.value = newName
        undoRedoState.onInput(newName)
    }

    fun updateIsEncrypted(value: Boolean) {
        _isEncrypted.value = value
    }

    fun updateNoteDescription(newDescription: TextFieldValue) {
        _noteDescription.value = newDescription
        undoRedoState.onInput(newDescription)
    }

    private fun updateNoteCreatedTime(newTime: Long) {
        _noteCreatedTime.longValue = newTime
    }

    private fun updateNotePin(pinned: Boolean) {
        _isPinned.value = pinned
    }

    fun updateNoteId(newId: Int) {
        _noteId.intValue = newId
    }

    fun undo() {
        undoRedoState.undo()
        _noteDescription.value = undoRedoState.input
    }

    fun redo() {
        undoRedoState.redo()
        _noteDescription.value = undoRedoState.input
    }

    private fun isSelectorAtStartOfNonEmptyLine(): Boolean {
        val text = _noteDescription.value.text
        val selectionStart = _noteDescription.value.selection.start

        if (selectionStart == 0) {
            return true
        }
        return text[selectionStart - 1] == '\n'
    }


    private fun getIntRangeForCurrentLine(): IntRange {
        val text = _noteDescription.value.text
        val selectionStart = _noteDescription.value.selection.start
        val selectionEnd = _noteDescription.value.selection.end
        var lineStart = selectionStart
        var lineEnd = selectionEnd

        while (lineStart > 0 && text[lineStart - 1] != '\n') {
            lineStart--
        }

        while (lineEnd < text.length && text[lineEnd] != '\n') {
            lineEnd++
        }
        return IntRange(lineStart, lineEnd - 1);
    }

    fun insertText(insertText: String, offset: Int = 1, newLine: Boolean = true) {
        val currentText = _noteDescription.value.text
        val resultSelectionIndex: Int
        val rangeOfCurrentLine = getIntRangeForCurrentLine()
        val updatedText = if (!rangeOfCurrentLine.isEmpty()) {
            val currentLineContents = currentText.substring(rangeOfCurrentLine)
            val newLine = if (isSelectorAtStartOfNonEmptyLine()) {
                insertText + currentLineContents
            } else {
                if (newLine) {
                    currentLineContents + "\n" + insertText
                } else {
                    currentLineContents + insertText
                }
            }
            resultSelectionIndex = rangeOfCurrentLine.first + newLine.length - 1
            currentText.replaceRange(rangeOfCurrentLine, newLine)
        } else {
            resultSelectionIndex = (currentText + insertText).length
            currentText + insertText
        }

        _noteDescription.value = TextFieldValue(
            text = updatedText,
            selection = TextRange(resultSelectionIndex + offset)
        )
    }
}
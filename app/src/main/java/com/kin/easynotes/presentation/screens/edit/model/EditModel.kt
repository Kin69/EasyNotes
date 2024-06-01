package com.kin.easynotes.presentation.screens.edit.model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.kin.easynotes.domain.model.Note
import com.kin.easynotes.domain.usecase.NoteViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class EditViewModel : NoteViewModel() {
    private val _noteName = mutableStateOf(TextFieldValue())
    val noteName: State<TextFieldValue> get() = _noteName

    private val _noteDescription = mutableStateOf(TextFieldValue())
    val noteDescription: State<TextFieldValue> get() = _noteDescription

    private val _noteId = mutableIntStateOf(0)
    val noteId: State<Int> get() = _noteId

    private val _noteCreatedTime = mutableLongStateOf(0L)
    val noteCreatedTime: State<Long> get() = _noteCreatedTime

    private val _isNoteInfoVisible = mutableStateOf(false)
    val isNoteInfoVisible: State<Boolean> get() = _isNoteInfoVisible

    override fun getNoteById(id: Int): Flow<Note> {
        return if (id == 0) {
            flowOf(Note(0, "", "", 0L))
        } else {
            super.getNoteById(id)
        }
    }

    fun saveNote(id: Int) {
        if (noteName.value.text.isNotEmpty() || noteDescription.value.text.isNotEmpty()) {
            val note = Note(
                id = id,
                name = noteName.value.text,
                description = noteDescription.value.text,
                createdAt = noteCreatedTime.value
            )

            when (note.id) {
                0 -> addNote(note)
                else -> updateNote(note)
            }
        }
    }

    fun syncNote(note: Note) {
        updateNoteName(TextFieldValue(note.name, selection = TextRange(note.name.length)))
        updateNoteDescription(TextFieldValue(note.description, selection = TextRange(note.name.length)))
        updateNoteCreatedTime(note.createdAt)
        updateNoteId(note.id)
    }

    fun toggleNoteInfoVisibility(value: Boolean) {
        _isNoteInfoVisible.value = value
    }

    fun updateNoteName(newName: TextFieldValue) {
        _noteName.value = newName
    }

    private fun updateNoteCreatedTime(newTime: Long) {
        _noteCreatedTime.longValue = newTime
    }

    private fun updateNoteId(newId: Int) {
        _noteId.intValue = newId
    }

    fun updateNoteDescription(newDescription: TextFieldValue) {
        _noteDescription.value = newDescription
    }

    private fun isSelectorAtStartOfNonEmptyLine(): Boolean {
        val text = _noteDescription.value.text
        val selectionStart = _noteDescription.value.selection.start

        if (selectionStart == 0) {
            return true
        }
        return text[selectionStart - 1] == '\n'
    }

    private fun getCurrentLine(): String {
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
        return text.substring(lineStart, lineEnd)
    }

    fun insertText(insertText: String, offset: Int = 1) {
        val currentText = _noteDescription.value.text
        val updatedText = if (getCurrentLine().isNotEmpty()) {
            if (isSelectorAtStartOfNonEmptyLine()) {
                val currentLine = getCurrentLine()
                val newLine = insertText + currentLine
                currentText.replace(currentLine, newLine)
            } else {
                "$currentText\n$insertText"
            }
        } else currentText + insertText
        println(getCurrentLine())
        _noteDescription.value = TextFieldValue(
            text = updatedText,
            selection = TextRange(updatedText.length + offset)
        )
    }
}
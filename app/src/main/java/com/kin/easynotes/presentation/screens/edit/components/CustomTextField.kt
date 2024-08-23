package com.kin.easynotes.presentation.screens.edit.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    placeholder: String,
    shape: RoundedCornerShape = RoundedCornerShape(0.dp),
    interactionSource: MutableInteractionSource = MutableInteractionSource(),
    singleLine: Boolean = false,
    modifier: Modifier = Modifier,
    hideContent: Boolean = false,
    useMonoSpaceFont: Boolean = false
) {

    val visualTransformation = if (hideContent) {
        PasswordVisualTransformation()
    } else {
        VisualTransformation.None
    }

    TextField(
        value = value,
        textStyle =  if (useMonoSpaceFont) LocalTextStyle.current.copy(fontFamily = FontFamily.Monospace) else LocalTextStyle.current,
        visualTransformation = visualTransformation,
        onValueChange = onValueChange,
        interactionSource = interactionSource,
        modifier = modifier
            .fillMaxWidth()
            .clip(shape),
        singleLine = singleLine,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
        ),
        placeholder = {
            Text(placeholder)
        }
    )
}

class UndoRedoState {
    var input by mutableStateOf(TextFieldValue(""))
    private val undoHistory = ArrayDeque<TextFieldValue>()
    private val redoHistory = ArrayDeque<TextFieldValue>()

    init {
        undoHistory.add(input)
    }

    fun onInput(value: TextFieldValue) {
        // always set the cursor at the end (selection = text length)
        val updatedValue = value.copy(value.text, selection = TextRange(value.text.length))
        undoHistory.add(updatedValue)
        redoHistory.clear()  // Clear redo history on new input
        input = updatedValue
    }

    fun undo() {
        if (undoHistory.size > 1) {
            // Pop the last
            val lastState = undoHistory.removeLastOrNull()
            lastState?.let {
                redoHistory.add(it)
            }

            // Peek the last
            val previousState = undoHistory.lastOrNull()
            previousState?.let {
                input = it
            }
        }
    }

    fun redo() {
        val redoState = redoHistory.removeLastOrNull()
        redoState?.let {
            undoHistory.add(it)
            input = it
        }
    }
}

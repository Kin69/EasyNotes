package com.kin.easynotes.presentation.components.markdown

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration

interface TextStyleSegment {
    val delimiter: String
    fun getSpanStyle(): SpanStyle
}

data class BoldSegment(override val delimiter: String = "**") : TextStyleSegment {
    override fun getSpanStyle() = SpanStyle(fontWeight = FontWeight.Bold)
}

data class ItalicSegment(override val delimiter: String = "*") : TextStyleSegment {
    override fun getSpanStyle() = SpanStyle(fontStyle = FontStyle.Italic)
}

data class HighlightSegment(override val delimiter: String = "==") : TextStyleSegment {
    override fun getSpanStyle() = SpanStyle(background = Color.Yellow.copy(alpha = 0.2f))
}

data class Strikethrough(override val delimiter: String = "~~") : TextStyleSegment {
    override fun getSpanStyle() = SpanStyle(textDecoration = TextDecoration.LineThrough)
}

data class Underline(override val delimiter: String = "_") : TextStyleSegment {
    override fun getSpanStyle() = SpanStyle(textDecoration = TextDecoration.Underline)
}
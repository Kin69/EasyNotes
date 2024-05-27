package com.kin.easynotes.presentation.screens.edit.components

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest

sealed interface MarkdownElement {
    fun render(builder: StringBuilder)
}

data class Heading(val level: Int, val text: String) : MarkdownElement {
    override fun render(builder: StringBuilder) {
        builder.append("#".repeat(level)).append(" $text\n\n")
    }
}

data class ImageInsertion(val photoUri: String) : MarkdownElement {
    override fun render(builder: StringBuilder) {
        // Markdown syntax for image insertion: !($photoUri)
        builder.append("!($photoUri)\n\n")
    }
}


data class ListItem(val text: String) : MarkdownElement {
    override fun render(builder: StringBuilder) {
        builder.append("- ${text}\n")
    }
}

data class CodeBlock(val code: String) : MarkdownElement {
    override fun render(builder: StringBuilder) {
        builder.append("```\n$code\n```\n")
    }
}

data class NormalText(val text: String) : MarkdownElement {
    override fun render(builder: StringBuilder) {
        builder.append("$text\n\n")
    }
}
class CodeBlockProcessor : MarkdownLineProcessor {
    override fun canProcessLine(line: String): Boolean = line == "```"

    override fun processLine(line: String, builder: MarkdownBuilder) {
        val codeBlock = StringBuilder()
        var index = builder.lineIndex + 1 // Start from the next line

        while (index < builder.lines.size) {
            val nextLine = builder.lines[index]
            if (nextLine == "```") {
                builder.lineIndex = index // Update the line index in the builder
                break
            }
            codeBlock.appendLine(nextLine)
            index++
        }
        builder.add(CodeBlock(codeBlock.toString()))
    }
}

class MarkdownBuilder(internal val lines: List<String>, private var lineProcessors: List<MarkdownLineProcessor>) {
    var lineIndex = -1

    internal val content = mutableListOf<MarkdownElement>()

    init {
        lineProcessors += ImageInsertionProcessor()
    }
    fun add(element: MarkdownElement) {
        content.add(element)
    }

    fun parse() {
        while (hasNextLine()) {
            val line = nextLine()
            val processor = lineProcessors.find { it.canProcessLine(line) }
            if (processor != null) {
                processor.processLine(line, this)
            } else {
                add(NormalText(line))
            }
        }
    }

    private fun hasNextLine(): Boolean = lineIndex + 1 < lines.size

    private fun nextLine(): String {
        lineIndex++
        return lines[lineIndex]
    }
}

@Composable
fun MarkdownText(
    text: String,
    modifier: Modifier = Modifier,
    weight: FontWeight = FontWeight.Normal,
    style: FontStyle = FontStyle.Normal,
    fontSize: TextUnit = 16.sp,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE
) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = fontSize,
        fontWeight = weight,
        fontStyle = style,
        overflow = overflow,
        maxLines = maxLines
    )
}


class ImageInsertionProcessor : MarkdownLineProcessor {
    override fun canProcessLine(line: String): Boolean {
        return line.trim().startsWith("!(") && line.trim().endsWith(")")
    }

    override fun processLine(line: String, builder: MarkdownBuilder) {
        val photoUri = line.substringAfter("!(", "").substringBefore(")")
        builder.add(ImageInsertion(photoUri))
    }
}

@Composable
fun MarkdownCodeBlock(code: String, modifier: Modifier, weight : FontWeight = FontWeight.Normal,color : Color = MaterialTheme.colorScheme.surfaceContainerLow,fontSize: TextUnit) {
    Box(
        modifier = Modifier,
        content = {
            Surface(
                color = color,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .fillMaxWidth(),
                content = {
                    MarkdownText(
                        text = code,
                        fontSize = fontSize,
                        modifier = modifier.padding(8.dp),
                        weight = weight,
                    )
                }
            )
        }
    )
}

class HeadingProcessor : MarkdownLineProcessor {
    override fun canProcessLine(line: String): Boolean = line.startsWith("#")

    override fun processLine(line: String, builder: MarkdownBuilder) {
        val level = line.takeWhile { it == '#' }.length
        val text = line.drop(level).trim()
        builder.add(Heading(level, text))
    }
}

class ListItemProcessor : MarkdownLineProcessor {
    override fun canProcessLine(line: String): Boolean = line.startsWith("- ")

    override fun processLine(line: String, builder: MarkdownBuilder) {
        val text = line.removePrefix("- ").trim()
        builder.add(ListItem(text))
    }
}



interface MarkdownLineProcessor {
    fun canProcessLine(line: String): Boolean
    fun processLine(line: String, builder: MarkdownBuilder)
}

@Composable
fun MarkdownView(
    markdown: String,
    modifier: Modifier = Modifier
) {
    val lines = markdown.lines()
    val lineProcessors = listOf(HeadingProcessor(), ListItemProcessor(), CodeBlockProcessor(), ImageInsertionProcessor())
    val markdownBuilder = MarkdownBuilder(lines, lineProcessors)
    markdownBuilder.parse()
    LazyColumn(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        items(markdownBuilder.content.size) { index ->
            when (val element = markdownBuilder.content[index]) {
                is Heading -> {
                    val fontSize = when (element.level) {
                        1 -> 24.sp
                        2 -> 20.sp
                        else -> 16.sp
                    }
                    MarkdownText(text = element.text, fontSize = fontSize, modifier = Modifier)
                }
                is ListItem -> {
                    MarkdownText(text = "• ${element.text}", modifier = Modifier)
                }
                is CodeBlock -> {
                    MarkdownCodeBlock(code = element.code.dropLast(1), modifier = Modifier, fontSize = 16.sp)
                }
                is ImageInsertion -> {
                    AsyncImage(
                        model = element.photoUri,
                        contentDescription = "ahoj",
                        modifier = Modifier.clip(RoundedCornerShape(9.dp))
                        )
                }
                is NormalText -> {
                    MarkdownText(text = element.text, modifier = Modifier)
                }
            }
        }
    }
}

@Composable
fun MarkdownPreview(
    markdown: String,
    maxHeight: Dp,
    modifier: Modifier = Modifier,
    weight: FontWeight = FontWeight.Normal,
    fontSize: TextUnit = 16.sp
) {
    val lines = markdown.lines()
    val lineProcessors = listOf(HeadingProcessor(), ListItemProcessor(), CodeBlockProcessor())
    val markdownBuilder = MarkdownBuilder(lines, lineProcessors)
    markdownBuilder.parse()

    Box(modifier = modifier.padding(3.dp)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = maxHeight)
        ) {
            items(markdownBuilder.content.size) { index ->
                val element = markdownBuilder.content[index]
                when (element) {
                    is Heading -> {
                        val fontSize = when (element.level) {
                            1 -> 24.sp
                            2 -> 20.sp
                            else -> 16.sp
                        }
                        MarkdownText(text = element.text, fontSize = fontSize, modifier = Modifier, weight = weight)
                    }
                    is ListItem -> {
                        MarkdownText(
                            text = "• ${element.text}",
                            modifier = Modifier,
                            weight = weight,
                            fontSize = fontSize,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }
                    is CodeBlock -> {
                        MarkdownCodeBlock(
                            code = element.code.dropLast(1),
                            modifier = Modifier,
                            color = MaterialTheme.colorScheme.surfaceContainerLow,
                            fontSize = fontSize
                        )
                    }
                    is ImageInsertion -> {
                        AsyncImage(
                            model = element.photoUri,
                            contentDescription = "ahoj",
                            modifier = Modifier.clip(RoundedCornerShape(9.dp))
                        )
                    }
                    is NormalText -> {
                        MarkdownText(
                            text = element.text,
                            modifier = Modifier,
                            weight = weight,
                            fontSize = fontSize,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

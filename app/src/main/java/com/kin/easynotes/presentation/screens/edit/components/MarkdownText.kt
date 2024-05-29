package com.kin.easynotes.presentation.screens.edit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kin.easynotes.presentation.theme.CodeFont
import com.kin.easynotes.presentation.theme.GlobalFont

sealed interface MarkdownElement {
    fun render(builder: StringBuilder)
}

data class Heading(val level: Int, val text: String) : MarkdownElement {
    override fun render(builder: StringBuilder) {
        builder.append("#".repeat(level)).append(" $text\n\n")
    }
}

data class CheckboxItem(val text: String, var checked: Boolean = false) : MarkdownElement {
    override fun render(builder: StringBuilder) {
        builder.append("[${if (checked) "X" else " "}] $text\n")
    }
}

data class Quote(val level: Int, val text: String) : MarkdownElement {
    override fun render(builder: StringBuilder) {
        builder.append("> ${text}\n")
    }
}

data class ImageInsertion(val photoUri: String) : MarkdownElement {
    override fun render(builder: StringBuilder) {
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
        var index = builder.lineIndex + 1

        while (index < builder.lines.size) {
            val nextLine = builder.lines[index]
            if (nextLine == "```") {
                builder.lineIndex = index
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

class ImageInsertionProcessor : MarkdownLineProcessor {
    override fun canProcessLine(line: String): Boolean {
        return line.trim().startsWith("!(") && line.trim().endsWith(")")
    }

    override fun processLine(line: String, builder: MarkdownBuilder) {
        val photoUri = line.substringAfter("!(", "").substringBefore(")")
        builder.add(ImageInsertion(photoUri))
    }
}

class CheckboxProcessor : MarkdownLineProcessor {
    override fun canProcessLine(line: String): Boolean = line.startsWith("[ ]") || line.startsWith("[X]") || line.startsWith("[]")

    override fun processLine(line: String, builder: MarkdownBuilder) {
        val checked = line.startsWith("[X]")
        val text = line.drop(3).trim()
        builder.add(CheckboxItem(text, checked))
    }
}

@Composable
fun MarkdownCodeBlock(
    color: Color,
    text: @Composable () -> Unit
) {
    Box(
        modifier = Modifier,
        content = {
            Surface(
                color = color,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .fillMaxWidth(),
                content = {
                    text()
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

class QuoteProcessor : MarkdownLineProcessor {
    override fun canProcessLine(line: String): Boolean = line.trim().startsWith(">")

    override fun processLine(line: String, builder: MarkdownBuilder) {
        val level = line.takeWhile { it == '>' }.length
        val text = line.drop(level).trim()
        builder.add(Quote(level, text))
    }
}

class ListItemProcessor : MarkdownLineProcessor {
    override fun canProcessLine(line: String): Boolean = line.startsWith("- ")

    override fun processLine(line: String, builder: MarkdownBuilder) {
        val text = line.removePrefix("- ").trim()
        builder.add(ListItem(text))
    }
}

@Composable
fun MarkdownQuote(content: String, fontSize: TextUnit) {
    Row(horizontalArrangement = Arrangement.Center) {
        Box(
            modifier = Modifier
                .height(22.dp)
                .width(6.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceContainerLow,
                    RoundedCornerShape(16.dp)
                )
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = " $content",fontSize = fontSize)
    }
}




interface MarkdownLineProcessor {
    fun canProcessLine(line: String): Boolean
    fun processLine(line: String, builder: MarkdownBuilder)
}


@Composable
fun MarkdownCheck(content: @Composable () -> Unit, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        content()
    }
}



@Composable
fun MarkdownText(
    markdown: String,
    modifier: Modifier = Modifier,
    weight: FontWeight = FontWeight.Normal,
    fontSize: TextUnit = 16.sp,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    onContentChange: (String) -> Unit = {}
) {
    val lines = markdown.lines()
    val lineProcessors = listOf(HeadingProcessor(), ListItemProcessor(), CodeBlockProcessor(), QuoteProcessor(), CheckboxProcessor())
    val markdownBuilder = MarkdownBuilder(lines, lineProcessors)
    markdownBuilder.parse()

    LazyColumn(modifier = modifier.fillMaxWidth()) {
        items(markdownBuilder.content.size) { index ->
            Spacer(modifier = Modifier.height(4.dp))
            when (val element = markdownBuilder.content[index]) {
                is Heading -> {
                    Text(
                        text = element.text,
                        fontSize = 24.sp,
                        overflow = overflow,
                        fontWeight = weight,
                        maxLines = maxLines,
                        fontFamily = GlobalFont
                    )
                }
                is CheckboxItem -> {
                    MarkdownCheck(
                        content = {
                            Text(
                                text = element.text,
                                fontSize = fontSize,
                                overflow = overflow,
                                fontWeight = weight,
                                maxLines = maxLines,
                                fontFamily = GlobalFont
                            )
                        },
                        checked = element.checked) { newChecked ->
                            val newMarkdown = lines.toMutableList()
                            if (newChecked)  {
                                newMarkdown[index] = "[X] ${element.text}"
                            } else {
                                newMarkdown[index] = "[ ] ${element.text}"
                            }
                            onContentChange(newMarkdown.joinToString("\n"))
                        }
                }
                is ListItem -> {
                    Text(
                        text = "• ${element.text}",
                        fontSize = fontSize,
                        overflow = overflow,
                        fontWeight = weight,
                        maxLines = maxLines,
                        fontFamily = GlobalFont
                    )
                }
                is Quote -> {
                    MarkdownQuote(content = element.text, fontSize = fontSize)
                }
                is CodeBlock -> {
                    MarkdownCodeBlock(color = MaterialTheme.colorScheme.surfaceContainerLow) {
                        Text(
                            text = element.code.dropLast(1),
                            fontSize = fontSize,
                            fontWeight = weight,
                            overflow = overflow,
                            maxLines = maxLines,
                            fontFamily = CodeFont,
                            modifier = Modifier.padding(6.dp)
                        )
                    }
                }
                is ImageInsertion -> {
                    AsyncImage(
                        model = element.photoUri,
                        contentDescription = "Image",
                        modifier = Modifier.clip(RoundedCornerShape(9.dp))
                    )
                }
                is NormalText -> {
                    Text(
                        text = element.text,
                        fontWeight = weight,
                        fontSize = fontSize,
                        overflow = overflow,
                        maxLines = maxLines,
                        fontFamily = GlobalFont
                    )
                }
            }
        }
    }
}
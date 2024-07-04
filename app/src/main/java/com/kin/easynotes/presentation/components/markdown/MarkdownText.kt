package com.kin.easynotes.presentation.components.markdown


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun MarkdownCodeBlock(
    color: Color,
    text: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.padding(top = 6.dp),
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

@Composable
fun MarkdownCheck(content: @Composable () -> Unit, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 6.dp)
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
    isPreview: Boolean = false,
    isEnabled: Boolean,
    modifier: Modifier = Modifier.fillMaxWidth(),
    weight: FontWeight = FontWeight.Normal,
    fontSize: TextUnit = 16.sp,
    spacing: Dp = 2.dp,
    onContentChange: (String) -> Unit = {}
) {
    if (!isEnabled) {
        StaticMarkdownText(
            markdown = markdown,
            modifier = modifier,
            weight = weight,
            fontSize = fontSize
        )
        return
    }

    val lines = markdown.lines()
    val lineProcessors = listOf(
        HeadingProcessor(),
        ListItemProcessor(),
        CodeBlockProcessor(),
        QuoteProcessor(),
        CheckboxProcessor()
    )
    val markdownBuilder = MarkdownBuilder(lines, lineProcessors)
    markdownBuilder.parse()

    MarkdownContent(
        isPreview = isPreview,
        content = markdownBuilder.content,
        modifier = modifier,
        spacing = spacing,
        weight = weight,
        fontSize = fontSize,
        lines = lines,
        onContentChange = onContentChange
    )
}

@Composable
fun StaticMarkdownText(
    markdown: String,
    modifier: Modifier,
    weight: FontWeight,
    fontSize: TextUnit
) {
    Text(
        text = markdown,
        fontSize = fontSize,
        fontWeight = weight,
        modifier = modifier
    )
}

@Composable
fun MarkdownContent(
    isPreview: Boolean,
    content: List<MarkdownElement>,
    modifier: Modifier,
    spacing: Dp,
    weight: FontWeight,
    fontSize: TextUnit,
    lines: List<String>,
    onContentChange: (String) -> Unit
) {
    SelectionContainer {
        if (isPreview) {
            Column(
                modifier = modifier
            ) {

                content.take(4).forEach {
                    RenderMarkdownElement(
                        element = it,
                        weight = weight,
                        fontSize = fontSize,
                        lines = lines,
                        onContentChange = onContentChange
                    )
                }
            }
        }
        else {
            SelectionContainer {

            }
            LazyColumn(modifier = modifier) {
                items(content.size) { index ->
                    Spacer(modifier = Modifier.height(spacing))
                    RenderMarkdownElement(
                        element = content[index],
                        weight = weight,
                        fontSize = fontSize,
                        lines = lines,
                        onContentChange = onContentChange
                    )
                }
            }
        }
    }
}

@Composable
fun RenderMarkdownElement(
    element: MarkdownElement,
    weight: FontWeight,
    fontSize: TextUnit,
    lines: List<String>,
    onContentChange: (String) -> Unit
) {
    when (element) {
        is Heading -> {
            Text(
                maxLines = 1,
                text = element.text + "\n",
                fontSize = when (element.level) {
                    in 1..6 -> (28 - (2 * element.level)).sp
                    else -> fontSize
                },
                fontWeight = weight,
                modifier = Modifier.padding(vertical = 10.dp)
            )
        }
        is CheckboxItem -> {
            MarkdownCheck(
                content = {
                    Text(
                        maxLines = 1,
                        text = element.text + "\n",
                        fontSize = fontSize,
                        fontWeight = weight,
                    )
                },
                checked = element.checked
            ) { newChecked ->
                val newMarkdown = lines.toMutableList().apply {
                    this[element.index] = if (newChecked) {
                        "[X] ${element.text}"
                    } else {
                        "[ ] ${element.text}"
                    }
                }
                onContentChange(newMarkdown.joinToString("\n"))
            }
        }
        is ListItem -> {
            Text(
                maxLines = 1,
                text = "• ${element.text}" + "\n",
                fontSize = fontSize,
                fontWeight = weight,
            )
        }
        is Quote -> {
            MarkdownQuote(content = element.text, fontSize = fontSize)
        }
        is CodeBlock -> {
            if (element.isEnded) {
                MarkdownCodeBlock(color = MaterialTheme.colorScheme.surfaceContainerLow) {
                    Text(
                        maxLines = 1,
                        text = element.code.dropLast(1) + "\n",
                        fontSize = fontSize,
                        fontWeight = weight,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(6.dp),
                    )
                }
            } else {
                Text(
                    maxLines = 1,
                    text = element.firstLine + "\n",
                    fontWeight = weight,
                    fontSize = fontSize,
                )
            }
        }
        is NormalText -> {
            Text(
                maxLines = 1,
                text = element.text.ifBlank {"\u00A0"} + "\n",
                fontWeight = weight,
                fontSize = fontSize,
            )
        }
    }
}

fun replaceNonBreakingSpaces(text: String): String {
    return text.replace("\u00A0", "\n")
}

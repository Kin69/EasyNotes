package com.kin.easynotes.presentation.components.markdown


import android.text.SpannableString
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.BasicText
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
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kin.easynotes.presentation.screens.settings.settings.shapeManager


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
        Text(
            text = buildString(" $content"),
            fontSize = fontSize
        )
    }
}

@Composable
fun MarkdownCheck(content: @Composable () -> Unit, checked: Boolean, onCheckedChange: ((Boolean) -> Unit)?) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier
                .padding(end = 12.dp)
                .size(20.dp)
        )
        content()
    }
}

@Composable
fun MarkdownText(
    radius: Int,
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
        ImageInsertionProcessor(),
        CheckboxProcessor()
    )
    val markdownBuilder = MarkdownBuilder(lines, lineProcessors)
    markdownBuilder.parse()

    MarkdownContent(
        radius = radius,
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
    radius: Int,
    isPreview: Boolean,
    content: List<MarkdownElement>,
    modifier: Modifier,
    spacing: Dp,
    weight: FontWeight,
    fontSize: TextUnit,
    lines: List<String>,
    onContentChange: (String) -> Unit
) {
    if (isPreview) {
        Column(modifier = modifier) {
             content.take(4).forEachIndexed { index, _ ->
                    RenderMarkdownElement(
                        radius = radius,
                        index = index,
                        content = content,
                        weight = weight,
                        fontSize = fontSize,
                        lines = lines,
                        isPreview = true,
                        onContentChange = onContentChange
                    )
                }
            }
    } else {
        SelectionContainer {
            LazyColumn(modifier = modifier) {
                items(content.size) { index ->
                    Spacer(modifier = Modifier.height(spacing))
                    RenderMarkdownElement(
                        radius = radius,
                        content = content,
                        index = index,
                        weight = weight,
                        fontSize = fontSize,
                        lines = lines,
                        isPreview = isPreview,
                        onContentChange = onContentChange
                    )
                }
            }
        }
    }
}

@Composable
fun RenderMarkdownElement(
    radius: Int,
    content: List<MarkdownElement>,
    index: Int,
    weight: FontWeight,
    fontSize: TextUnit,
    lines: List<String>,
    isPreview: Boolean,
    onContentChange: (String) -> Unit
) {
    val element = content[index]
    Row {
        when (element) {
            is Heading -> {
                Text(
                    text = buildString(element.text, weight),
                    fontSize = when (element.level) {
                        in 1..6 -> (28 - (2 * element.level) - fontSize.value/3).sp
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
                            text = buildString(element.text, weight),
                            fontSize = fontSize,
                            fontWeight = weight,
                        )
                    },
                    checked = element.checked,
                    onCheckedChange = if (isPreview) null else { newChecked ->
                        val newMarkdown = lines.toMutableList().apply {
                            this[element.index] = if (newChecked) {
                                "[X] ${element.text}"
                            } else {
                                "[ ] ${element.text}"
                            }
                        }
                        onContentChange(newMarkdown.joinToString("\n"))
                    }
                )
            }

            is ListItem -> {
                Text(
                    text = buildString("• ${element.text}", weight),
                    fontSize = fontSize,
                    fontWeight = weight,
                )
            }

            is Quote -> {
                MarkdownQuote(content = element.text, fontSize = fontSize)
            }

            is ImageInsertion -> {
                val modifier = if (isPreview) {
                    Modifier.clip(shape = shapeManager(radius = radius))
                } else {
                    Modifier
                        .clip(shape = shapeManager(isBoth = true, radius = radius / 2))
                        .clickable { /* Just for animation */ }
                }
                AsyncImage(
                    model = element.photoUri,
                    contentDescription = "Image",
                    modifier = modifier
                )
            }

            is CodeBlock -> {
                if (element.isEnded) {
                    MarkdownCodeBlock(color = MaterialTheme.colorScheme.surfaceContainerLow) {
                        Text(
                            text = element.code.dropLast(1),
                            fontSize = fontSize,
                            fontWeight = weight,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(6.dp),
                        )
                    }
                } else {
                    Text(
                        text = buildString(element.firstLine, weight),
                        fontWeight = weight,
                        fontSize = fontSize,
                    )
                }
            }

            is NormalText -> {
                Text(text = buildString(element.text, weight), fontSize = fontSize)
            }
        }
        // Add new line to selectionContainer but don't render it
        if (content.lastIndex != index) {
            Text(
                text = "\n",
                maxLines = 1
            )
        }
    }
}

package com.kin.easynotes.presentation.components.Makrdown

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.IOException


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
                            )
                        },
                        checked = element.checked) { newChecked ->
                            val newMarkdown = lines.toMutableList()
                            if (newChecked)  {
                                newMarkdown[element.index] = "[X] ${element.text}"
                            } else {
                                newMarkdown[element.index] = "[ ] ${element.text}"
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
                    )
                }
                is Quote -> {
                    MarkdownQuote(content = element.text, fontSize = fontSize)
                }
                is CodeBlock -> {
                    if (element.iSEnded) {
                        MarkdownCodeBlock(color = MaterialTheme.colorScheme.surfaceContainerLow) {
                            Text(
                                text = element.code.dropLast(1),
                                fontSize = fontSize,
                                fontWeight = weight,
                                overflow = overflow,
                                maxLines = maxLines,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.padding(6.dp)
                            )
                        }
                    } else {
                        Text(
                            text = "```",
                            fontWeight = weight,
                            fontSize = fontSize,
                            overflow = overflow,
                            maxLines = maxLines,

                        )
                    }
                }
                is ImageInsertion -> {
                    LoadImageFromUri(context = LocalContext.current, imageUri = element.photoUri)
                }
                is NormalText -> {
                    Text(
                        text = element.text,
                        fontWeight = weight,
                        fontSize = fontSize,
                        overflow = overflow,
                        maxLines = maxLines,

                    )
                }
            }
        }
    }
}

@Composable
fun LoadImageFromUri(context: Context, imageUri: Uri) {
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(imageUri) {
        try {
            val inputStream = context.contentResolver.openInputStream(imageUri)
            bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
        } catch (e: SecurityException) {
            println(e.stackTraceToString())
        }
    }

    bitmap?.let { loadedBitmap ->
        Image(
            bitmap = loadedBitmap.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
        )
    }
}
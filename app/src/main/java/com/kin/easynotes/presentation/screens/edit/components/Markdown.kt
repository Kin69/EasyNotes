package com.kin.easynotes.presentation.screens.edit.components

import android.content.Context
import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewStateWithHTMLData

@Composable
fun MarkdownRichText(
    modifier: Modifier = Modifier,
    htmlBodyText: String,
    color: Color
) {
    val context = LocalContext.current
    val styledHtmlBodyText = """
        <html>
        <head>
      a  <style>
        body {
            color: ${String.format("#%06X", (0xFFFFFF and color.toArgb()))};
        }
        </style>
        </head>
        <article class="markdown-body">
        $htmlBodyText
        </article>
        </html>
    """.trimIndent()

    val webViewState = rememberWebViewStateWithHTMLData(data = styledHtmlBodyText)
    val client = remember {
        LocalContentWebViewClient(
            context = context,
            handleUrl = { false }
        )
    }
    val backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh
    WebView(
        state = webViewState,
        modifier = modifier
            .background(backgroundColor)
            .padding(9.dp),
        onCreated = { webView ->
            val webViewSettings = webView.settings
            webViewSettings.allowFileAccess = false
            webViewSettings.allowContentAccess = false
            webView.setBackgroundColor(backgroundColor.toArgb())
        },
        client = client
    )
}

private class LocalContentWebViewClient(
    context: Context,
    private val handleUrl: (Uri) -> Boolean
) : AccompanistWebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        return request?.url?.let { handleUrl(it) } ?: super.shouldOverrideUrlLoading(view, request)
    }
}


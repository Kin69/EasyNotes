package com.kin.easynotes.widget.ui

import android.appwidget.AppWidgetManager
import androidx.compose.runtime.Composable
import androidx.glance.Button
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import com.kin.easynotes.R
import com.kin.easynotes.widget.NotesWidgetActivity

@Composable
fun ZeroState(widgetId: Int) {
    val widgetIdKey = ActionParameters.Key<Int>(AppWidgetManager.EXTRA_APPWIDGET_ID)
    val context = LocalContext.current

    Box(
        modifier = GlanceModifier
            .background(GlanceTheme.colors.background)
            .fillMaxSize(),
        contentAlignment = Alignment.Center) {
        Button(
            text = context.getString(R.string.select_note),
            onClick = actionStartActivity<NotesWidgetActivity>(
                parameters = actionParametersOf(widgetIdKey to widgetId),
            ),
        )
    }
}
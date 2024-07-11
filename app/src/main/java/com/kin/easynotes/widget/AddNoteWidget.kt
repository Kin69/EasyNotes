package com.kin.easynotes.widget

import android.content.Context
import android.content.Intent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.width
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.kin.easynotes.R
import com.kin.easynotes.presentation.MainActivity

class AddNoteWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                val intent = Intent(context, MainActivity::class.java).apply {
                    putExtra("noteId", 0)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                Row(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .height(50.dp)
                        .width(100.dp)
                        .background(GlanceTheme.colors.primary)
                        .clickable { context.startActivity(intent) }
                        .cornerRadius(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = context.getString(R.string.new_note),
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = GlanceTheme.colors.background
                        ),
                    )
                }
            }
        }
    }
}
package com.kin.easynotes.presentation.screens.edit.modes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.kin.easynotes.presentation.screens.edit.components.MarkdownRichText
import com.kin.easynotes.presentation.screens.edit.model.EditViewModel

@Composable
fun PreviewScreen(viewModel: EditViewModel, id: Int) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {



        MarkdownRichText(
            htmlBodyText = viewModel.noteNameState.value,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(16.dp,16.dp,16.dp,0.dp)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .height(54.dp)
        )
        MarkdownRichText(
            htmlBodyText = viewModel.noteDescriptionState.value,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(16.dp,3.dp,16.dp,16.dp)
                .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),)
                .fillMaxHeight()
        )
    }
}

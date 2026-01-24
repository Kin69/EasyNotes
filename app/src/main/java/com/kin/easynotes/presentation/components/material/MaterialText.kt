/*
 * Copyright (C) 2025-2026 Vexzure
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package com.kin.easynotes.presentation.components.material

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MaterialText(
    title: String,
    description: String? = null,
    modifier: Modifier = Modifier,
    titleSize: TextUnit = 14.sp,
    descriptionSize: TextUnit = 11.sp,
    center: Boolean = false,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    descriptionColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Column(
        modifier = modifier,
        horizontalAlignment = if (center) Alignment.CenterHorizontally else Alignment.Start,) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontSize = titleSize),
            color = titleColor,
            modifier = Modifier.padding(bottom = 3.dp),
            textAlign = if (center) TextAlign.Center else TextAlign.Start
        )
        if (description != null) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = descriptionSize),
                color = descriptionColor,
                modifier = Modifier.padding(bottom = 3.dp),
                maxLines = 6,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

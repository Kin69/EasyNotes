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

package com.kin.easynotes.presentation.theme

import androidx.compose.ui.graphics.Color
import kotlin.math.abs

object ColorUtils {
    fun Color.darken(factor: Float): Color {
        val (hue, saturation, lightness) = rgbToHsl(this.red, this.green, this.blue)
        val newLightness = (lightness - factor * lightness).coerceIn(0f, 1f)
        return hslToColor(hue, saturation, newLightness)
    }

    fun Color.editRGB(red: Float = 0F, green: Float = 0F, blue: Float = 0F): Color {
        try {
            return Color(
                red = this.red + (red/255),
                green = this.green + (green/255),
                blue = this.blue + (blue/255),
                alpha = this.alpha
            )
        } catch (e: IllegalArgumentException) {
            return this
        }
    }

    private fun rgbToHsl(red: Float, green: Float, blue: Float): Triple<Float, Float, Float> {

        val max = maxOf(red, green, blue)
        val min = minOf(red, green, blue)
        val delta = max - min

        val lightness = (max + min) / 2

        val saturation = if (delta == 0f) {
            0f
        } else {
            delta / (1 - abs(2 * lightness - 1))
        }

        val hue = when {
            delta == 0f -> 0f
            max == red -> ((green - blue) / delta + (if (green < blue) 6 else 0)) / 6
            max == green -> ((blue - red) / delta + 2) / 6
            max == blue -> ((red - green) / delta + 4) / 6
            else -> 0f
        }

        return Triple(hue, saturation, lightness)
    }

    private fun hslToColor(hue: Float, saturation: Float, lightness: Float): Color {
        val c = (1 - abs(2 * lightness - 1)) * saturation
        val x = c * (1 - abs((hue * 6) % 2 - 1))
        val m = lightness - c / 2

        val (rPrime, gPrime, bPrime) = when {
            hue < 1 / 6f -> Triple(c, x, 0f)
            hue < 2 / 6f -> Triple(x, c, 0f)
            hue < 3 / 6f -> Triple(0f, c, x)
            hue < 4 / 6f -> Triple(0f, x, c)
            hue < 5 / 6f -> Triple(x, 0f, c)
            else -> Triple(c, 0f, x)
        }

        val r = (rPrime + m).coerceIn(0f, 1f)
        val g = (gPrime + m).coerceIn(0f, 1f)
        val b = (bPrime + m).coerceIn(0f, 1f)

        return Color(red = r, green = g, blue = b)
    }
}
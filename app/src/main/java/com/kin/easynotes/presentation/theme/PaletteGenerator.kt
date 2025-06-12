package com.kin.easynotes.presentation.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.kyant.m3color.hct.Hct
import com.kyant.m3color.scheme.SchemeContent
import com.kyant.m3color.scheme.SchemeExpressive
import com.kyant.m3color.scheme.SchemeFidelity
import com.kyant.m3color.scheme.SchemeFruitSalad
import com.kyant.m3color.scheme.SchemeMonochrome
import com.kyant.m3color.scheme.SchemeNeutral
import com.kyant.m3color.scheme.SchemeRainbow
import com.kyant.m3color.scheme.SchemeTonalSpot
import com.kyant.m3color.scheme.SchemeVibrant

enum class PaletteStyle {
    TonalSpot, Neutral, Vibrant, Expressive, Rainbow, FruitSalad, Monochrome, Fidelity, Content
}

@Stable
fun generatePalette(
    keyColor: Color,
    isDark: Boolean,
    style: PaletteStyle = PaletteStyle.Content,
    contrastLevel: Double = 0.0
): ColorScheme {
    val hct = Hct.fromInt(keyColor.toArgb())
    val scheme = when (style) {
        PaletteStyle.TonalSpot -> SchemeTonalSpot(hct, isDark, contrastLevel)
        PaletteStyle.Neutral -> SchemeNeutral(hct, isDark, contrastLevel)
        PaletteStyle.Vibrant -> SchemeVibrant(hct, isDark, contrastLevel)
        PaletteStyle.Expressive -> SchemeExpressive(hct, isDark, contrastLevel)
        PaletteStyle.Rainbow -> SchemeRainbow(hct, isDark, contrastLevel)
        PaletteStyle.FruitSalad -> SchemeFruitSalad(hct, isDark, contrastLevel)
        PaletteStyle.Monochrome -> SchemeMonochrome(hct, isDark, contrastLevel)
        PaletteStyle.Fidelity -> SchemeFidelity(hct, isDark, contrastLevel)
        PaletteStyle.Content -> SchemeContent(hct, isDark, contrastLevel)
    }

    return ColorScheme(
        background = scheme.background.toColor(),
        error = scheme.error.toColor(),
        errorContainer = scheme.errorContainer.toColor(),
        inverseOnSurface = scheme.inverseOnSurface.toColor(),
        inversePrimary = scheme.inversePrimary.toColor(),
        inverseSurface = scheme.inverseSurface.toColor(),
        onBackground = scheme.onBackground.toColor(),
        onError = scheme.onError.toColor(),
        onErrorContainer = scheme.onErrorContainer.toColor(),
        onPrimary = scheme.onPrimary.toColor(),
        onPrimaryContainer = scheme.onPrimaryContainer.toColor(),
        onSecondary = scheme.onSecondary.toColor(),
        onSecondaryContainer = scheme.onSecondaryContainer.toColor(),
        onSurface = scheme.onSurface.toColor(),
        onSurfaceVariant = scheme.onSurfaceVariant.toColor(),
        onTertiary = scheme.onTertiary.toColor(),
        onTertiaryContainer = scheme.onTertiaryContainer.toColor(),
        outline = scheme.outline.toColor(),
        outlineVariant = scheme.outlineVariant.toColor(),
        primary = scheme.primary.toColor(),
        primaryContainer = scheme.primaryContainer.toColor(),
        scrim = scheme.scrim.toColor(),
        secondary = scheme.secondary.toColor(),
        secondaryContainer = scheme.secondaryContainer.toColor(),
        surface = scheme.surface.toColor(),
        surfaceBright = scheme.surfaceBright.toColor(),
        surfaceContainer = scheme.surfaceContainer.toColor(),
        surfaceContainerLow = scheme.surfaceContainerLow.toColor(),
        surfaceContainerLowest = scheme.surfaceContainerLowest.toColor(),
        surfaceContainerHigh = scheme.surfaceContainerHigh.toColor(),
        surfaceContainerHighest = scheme.surfaceContainerHighest.toColor(),
        surfaceDim = scheme.surfaceDim.toColor(),
        surfaceTint = scheme.surfaceTint.toColor(),
        surfaceVariant = scheme.surfaceVariant.toColor(),
        tertiary = scheme.tertiary.toColor(),
        tertiaryContainer = scheme.tertiaryContainer.toColor(),
    )
}

@Suppress("NOTHING_TO_INLINE")
private inline fun Int.toColor(): Color = Color(this)
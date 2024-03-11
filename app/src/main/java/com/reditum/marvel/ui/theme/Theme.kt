package com.reditum.marvel.ui.theme

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.net.toUri
import androidx.palette.graphics.Palette
import coil.imageLoader
import coil.request.ImageRequest
import com.materialkolor.DynamicMaterialTheme
import com.materialkolor.hct.Hct
import com.materialkolor.palettes.TonalPalette
import com.materialkolor.score.Score

import kotlin.math.max

val DefaultThemeColor = Color.Red

@Composable
fun MarvelTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    themeColor: Color = DefaultThemeColor,
    content: @Composable () -> Unit
) {
    DynamicMaterialTheme(
        seedColor = themeColor,
        useDarkTheme = darkTheme,
        typography = Typography,
        animate = true,
        content = content
    )
}

fun Bitmap.extractThemeColor(): Color {
    val colorsToPopulation = Palette.from(this)
        .maximumColorCount(8)
        .generate()
        .swatches
        .associate { it.rgb to it.population }
    val rankedColors = Score.score(colorsToPopulation)
    return Color(rankedColors.first())
}

fun Configuration.isDark(): Boolean {
    return (uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
}

data class PrimaryColors(
    val color: Color,
    val primary: Color,
    val onPrimary: Color,
)

fun getPrimaryColors(color: Color, darkTheme: Boolean = true): PrimaryColors {
    val hct = Hct.fromInt(color.toArgb())
    val hue = hct.hue
    val chroma = hct.chroma
    val a1 = TonalPalette.fromHueAndChroma(hue, max(48.0, chroma))
    return if (darkTheme) PrimaryColors(
        color,
        Color(a1.tone(30)),
        Color(a1.tone(90))
    ) else PrimaryColors(
        color,
        Color(a1.tone(90)),
        Color(a1.tone(10))
    )
}

suspend fun getNetworkImageColor(context: Context, url: String): Color {
    val res = context.imageLoader.execute(
        ImageRequest.Builder(context)
            .data(url.toUri())
            .allowHardware(false)
            .build()
    )
    return (res.drawable as BitmapDrawable).bitmap.extractThemeColor()
}

val ColorSaver = object : Saver<Color, Int> {
    override fun restore(value: Int): Color = Color(value)
    override fun SaverScope.save(value: Color): Int = value.toArgb()
}
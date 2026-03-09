package com.hapkonic.tailorapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary          = Primary,
    onPrimary        = Color.White,
    primaryContainer = PrimaryLight,
    background       = Background,
    surface          = Surface
)

private val DarkColors = darkColorScheme(
    primary          = PrimaryLight,
    onPrimary        = Color.Black,
    primaryContainer = PrimaryDark,
    background       = Color(0xFF121212),
    surface          = Color(0xFF1E1E1E)
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val darkTheme = isSystemInDarkTheme()
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography  = AppTypography,
        content     = content
    )
}

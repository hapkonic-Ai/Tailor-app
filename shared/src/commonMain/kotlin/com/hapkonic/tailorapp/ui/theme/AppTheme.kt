package com.hapkonic.tailorapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary          = Primary,
    onPrimary        = androidx.compose.ui.graphics.Color.White,
    primaryContainer = PrimaryLight,
    background       = Background,
    surface          = Surface
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography  = AppTypography,
        content     = content
    )
}

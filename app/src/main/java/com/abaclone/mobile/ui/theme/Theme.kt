package com.abaclone.mobile.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AbaColorScheme = lightColorScheme(
    primary = AbaMaroon,
    onPrimary = Color.White,
    primaryContainer = AbaMaroonLight,
    secondary = AbaGold,
    background = AbaBackground,
    surface = AbaSurface,
    onBackground = AbaTextPrimary,
    onSurface = AbaTextPrimary,
    error = AbaRed
)

@Composable
fun AbaCloneTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AbaColorScheme,
        typography = AbaTypography,
        content = content
    )
}

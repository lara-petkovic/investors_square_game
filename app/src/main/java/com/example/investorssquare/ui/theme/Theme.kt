package com.example.investorssquare.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = LightGreen,
    onPrimary = Color.Black, // Text color
    onSecondary = Color.White,
    secondary = LightGreen,
    surface = JailPopupGreenBackgroundLight // Jail popup background
)

private val LightColorScheme = lightColorScheme(
    primary = DarkGreen,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    secondary = DarkGreen,
    surface = JailPopupGreenBackgroundDark
)

@Composable
fun InvestorsSquareTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
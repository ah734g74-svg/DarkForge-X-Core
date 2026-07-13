@file:Suppress("DEPRECATION")

package com.darkforge.x.ui

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush

// DarkForge-X Professional Color Palette
val neonCyan = Color(0xFF00D9FF)
val electricViolet = Color(0xFF9D4EDD)
val obsidianBlack = Color(0xFF0A0E27)
val darkCharcoal = Color(0xFF1A1F3A)
val cyberGray = Color(0xFF2D3561)

val gradientBrush = Brush.horizontalGradient(listOf(neonCyan, electricViolet))

// Animated border gradient colors
val gradientPurple = electricViolet
val gradientViolet = neonCyan
val gradientMagenta = Color(0xFFFF006E)

val DarkColorScheme = darkColorScheme(
    primary = neonCyan,
    onPrimary = obsidianBlack,
    primaryContainer = electricViolet,
    onPrimaryContainer = Color(0xFFFFFFFF),
    secondary = electricViolet,
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = darkCharcoal,
    onSecondaryContainer = neonCyan,
    tertiary = neonCyan,
    onTertiary = obsidianBlack,
    tertiaryContainer = cyberGray,
    onTertiaryContainer = neonCyan,
    surface = darkCharcoal,
    onSurface = Color(0xFFFFFFFF),
    surfaceVariant = cyberGray,
    onSurfaceVariant = neonCyan,
    background = obsidianBlack,
    onBackground = Color(0xFFFFFFFF),
    outline = neonCyan,
    outlineVariant = electricViolet,
    error = Color(0xFFFF006E),
    onError = obsidianBlack,
    errorContainer = Color(0xFF3B0000),
    onErrorContainer = Color(0xFFFFB4AB),
)

val LightColorScheme = lightColorScheme(
    primary = electricViolet,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = neonCyan,
    onPrimaryContainer = obsidianBlack,
    secondary = neonCyan,
    onSecondary = obsidianBlack,
    secondaryContainer = Color(0xFFE0F7FA),
    onSecondaryContainer = darkCharcoal,
    tertiary = electricViolet,
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFF3E5F5),
    onTertiaryContainer = darkCharcoal,
    surface = Color(0xFFFAFAFA),
    onSurface = darkCharcoal,
    surfaceVariant = Color(0xFFE8E8E8),
    onSurfaceVariant = cyberGray,
    background = Color(0xFFFFFFFF),
    onBackground = darkCharcoal,
    outline = electricViolet,
    outlineVariant = neonCyan,
    error = Color(0xFFB3261E),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B),
)

@Composable
fun DarkForgeXTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

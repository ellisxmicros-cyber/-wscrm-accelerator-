package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CyberColorScheme = darkColorScheme(
  primary = NeonCyan,
  onPrimary = Color.Black,
  primaryContainer = Color(0xFF003840),
  onPrimaryContainer = NeonCyan,
  secondary = NeonGreen,
  onSecondary = Color.Black,
  secondaryContainer = Color(0xFF0C3805),
  onSecondaryContainer = NeonGreen,
  tertiary = NeonPurple,
  onTertiary = Color.White,
  tertiaryContainer = Color(0xFF38004D),
  onTertiaryContainer = Color(0xFFE9B3FF),
  background = CyberBlack,
  onBackground = TextWhite,
  surface = CyberCard,
  onSurface = TextWhite,
  surfaceVariant = CyberCardVariant,
  onSurfaceVariant = TextMuted,
  outline = CyberBorder,
  outlineVariant = CyberBorderBright
)

@Composable
fun MyApplicationTheme(
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = CyberColorScheme,
    typography = Typography,
    content = content
  )
}

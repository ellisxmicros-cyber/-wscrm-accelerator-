package com.example.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import java.util.Locale

/**
 * Global reactive language state for TurboVentas WS.
 * Automatically detects if the device is in English (e.g., in the US, UK, Canada, etc.)
 * and also allows manual 1-tap switching between Español (ES) and English (EN).
 */
val LocalIsEnglish = compositionLocalOf {
    Locale.getDefault().language.lowercase().startsWith("en")
}

@Composable
fun tr(es: String, en: String): String {
    return if (LocalIsEnglish.current) en else es
}

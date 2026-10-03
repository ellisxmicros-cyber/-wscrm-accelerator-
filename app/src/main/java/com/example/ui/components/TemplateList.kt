package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.AppDatabase
import com.example.data.Template
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberCard
import com.example.ui.theme.CyberCardVariant
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A reusable Composable that fetches saved sales scripts directly from the
 * Room database and displays them with a quick-copy button.
 *
 * @param modifier Custom modifier for layout sizing and padding.
 * @param categoryFilter Optional category name (e.g. "Precios", "Pagos") to filter scripts.
 * @param searchQuery Optional query text to filter by title, content, or shortcut.
 * @param onTemplateCopied Optional callback fired when a sales script is copied to clipboard.
 * @param onTemplateClick Optional callback fired when tapping the script card.
 */
@Composable
fun TemplateList(
    modifier: Modifier = Modifier,
    categoryFilter: String? = null,
    searchQuery: String = "",
    onTemplateCopied: ((Template) -> Unit)? = null,
    onTemplateClick: ((Template) -> Unit)? = null
) {
    val context = LocalContext.current
    val database = remember(context) { AppDatabase.getInstance(context) }
    val scope = rememberCoroutineScope()

    // Observe saved sales scripts reactively from Room DAO
    val allTemplates by database.templateDao().getAllTemplates().collectAsStateWithLifecycle(initialValue = emptyList())

    // Apply category & query filters
    val filteredTemplates = remember(allTemplates, categoryFilter, searchQuery) {
        allTemplates.filter { template ->
            val matchesCategory = categoryFilter.isNullOrBlank() ||
                    categoryFilter.equals("Todos", ignoreCase = true) ||
                    template.category.equals(categoryFilter, ignoreCase = true)

            val matchesQuery = searchQuery.isBlank() ||
                    template.title.contains(searchQuery, ignoreCase = true) ||
                    template.content.contains(searchQuery, ignoreCase = true) ||
                    template.shortcut.contains(searchQuery, ignoreCase = true)

            matchesCategory && matchesQuery
        }
    }

    TemplateListContent(
        templates = filteredTemplates,
        onCopy = { template ->
            copyScriptToClipboard(context, template)
            scope.launch {
                withContext(Dispatchers.IO) {
                    database.templateDao().incrementUsage(template.id)
                }
            }
            onTemplateCopied?.invoke(template)
        },
        onTemplateClick = onTemplateClick,
        modifier = modifier
    )
}

/**
 * Overload for TemplateList accepting an explicit list of templates for maximum reusability.
 */
@Composable
fun TemplateList(
    templates: List<Template>,
    onCopyClick: (Template) -> Unit,
    modifier: Modifier = Modifier,
    onTemplateClick: ((Template) -> Unit)? = null
) {
    TemplateListContent(
        templates = templates,
        onCopy = onCopyClick,
        onTemplateClick = onTemplateClick,
        modifier = modifier
    )
}

@Composable
private fun TemplateListContent(
    templates: List<Template>,
    onCopy: (Template) -> Unit,
    modifier: Modifier = Modifier,
    onTemplateClick: ((Template) -> Unit)? = null
) {
    if (templates.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    tint = CyberBorder,
                    modifier = Modifier.size(42.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "No hay plantillas guardadas",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.5.sp,
                    color = TextMuted
                )
            }
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .testTag("template_list"),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(templates, key = { it.id }) { template ->
                TemplateScriptCard(
                    template = template,
                    onCopy = { onCopy(template) },
                    onClick = if (onTemplateClick != null) { { onTemplateClick(template) } } else null
                )
            }
        }
    }
}

/**
 * Individual card displaying a saved sales script with category badge and quick-copy button.
 */
@Composable
fun TemplateScriptCard(
    template: Template,
    onCopy: () -> Unit,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    var isJustCopied by remember { mutableStateOf(false) }

    val copyButtonColor by animateColorAsState(
        targetValue = if (isJustCopied) NeonGreen else NeonCyan,
        label = "copyButtonColor"
    )

    LaunchedEffect(isJustCopied) {
        if (isJustCopied) {
            delay(1800)
            isJustCopied = false
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .testTag("template_item_${template.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CyberCard),
        border = BorderStroke(
            1.dp,
            if (template.isPinned) NeonCyan.copy(alpha = 0.5f) else CyberBorder
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: Title and Quick-Copy Action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (template.isPinned) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Fijado",
                            tint = NeonCyan,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text(
                        text = template.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.5.sp,
                        color = TextWhite
                    )
                }

                // Quick-Copy Button
                OutlinedButton(
                    onClick = {
                        isJustCopied = true
                        onCopy()
                    },
                    modifier = Modifier
                        .height(34.dp)
                        .testTag("quick_copy_btn_${template.id}"),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, copyButtonColor),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
                ) {
                    Icon(
                        imageVector = if (isJustCopied) Icons.Default.Check else Icons.Default.ContentCopy,
                        contentDescription = "Copiar",
                        tint = copyButtonColor,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = if (isJustCopied) "¡COPIADO!" else "COPIAR",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = copyButtonColor,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            // Category & Shortcut Chips
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category Chip
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(NeonCyan.copy(alpha = 0.12f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = template.category,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NeonCyan
                    )
                }

                // Shortcut Chip (if configured)
                if (template.shortcut.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(NeonPurple.copy(alpha = 0.12f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = template.shortcut,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = NeonPurple
                        )
                    }
                }

                if (template.usageCount > 0) {
                    Text(
                        text = "• ${template.usageCount} usos",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                }
            }

            // Sales Script Content
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = template.content,
                fontSize = 12.5.sp,
                lineHeight = 17.sp,
                color = Color(0xFFA0A0B8)
            )
        }
    }
}

/**
 * Copies the script to system clipboard and triggers haptic + toast feedback.
 */
private fun copyScriptToClipboard(context: Context, template: Template) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(template.title, template.content)
    clipboard.setPrimaryClip(clip)

    // Haptic feedback
    try {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createOneShot(45, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(45)
        }
    } catch (_: Exception) {}

    Toast.makeText(context, "⚡ ¡Copiado: ${template.title}!", Toast.LENGTH_SHORT).show()
}

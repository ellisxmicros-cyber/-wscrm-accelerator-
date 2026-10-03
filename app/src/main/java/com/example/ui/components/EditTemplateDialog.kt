package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TemplateEntity
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberCard
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EditTemplateDialog(
    initialTemplate: TemplateEntity?,
    onDismiss: () -> Unit,
    onSave: (TemplateEntity) -> Unit
) {
    var title by remember { mutableStateOf(initialTemplate?.title ?: "") }
    var content by remember { mutableStateOf(initialTemplate?.content ?: "") }
    var category by remember { mutableStateOf(initialTemplate?.category ?: "Precios") }
    var shortcut by remember { mutableStateOf(initialTemplate?.shortcut ?: "") }
    var isPinned by remember { mutableStateOf(initialTemplate?.isPinned ?: false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val categories = listOf("Precios", "Pagos", "Info", "Saludos", "Envíos", "Cierre", "Promos")
    val placeholders = listOf("{nombre}", "{producto}", "{precio}", "{descuento}")

    BasicAlertDialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(1.5.dp, NeonCyan, RoundedCornerShape(16.dp))
                .testTag("edit_template_dialog"),
            color = CyberCard
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = if (initialTemplate == null) "⚡ NUEVA PLANTILLA" else "⚡ EDITAR PLANTILLA",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = NeonCyan,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                CyberTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = "Título de la Plantilla",
                    placeholder = "ej. ⚡ Precios de Hoy",
                    testTag = "template_title_input"
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "CATEGORÍA",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = TextMuted
                )
                Spacer(modifier = Modifier.height(6.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.forEach { cat ->
                        CyberFilterChip(
                            text = cat,
                            selected = category == cat,
                            onClick = { category = cat }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                CyberTextField(
                    value = shortcut,
                    onValueChange = { shortcut = it },
                    label = "Atajo Rápido (Opcional)",
                    placeholder = "ej. /precios",
                    testTag = "template_shortcut_input"
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "CONTENIDO DEL MENSAJE",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = TextMuted
                )
                Spacer(modifier = Modifier.height(4.dp))

                // Insert variable chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                    text = "Insertar:",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp,
                    color = TextMuted
                )
                    Spacer(modifier = Modifier.width(6.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        placeholders.forEach { tag ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(NeonGreen.copy(alpha = 0.15f))
                                    .border(1.dp, NeonGreen.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                                    .clickable { content = "$content $tag" }
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = tag,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 10.sp,
                                    color = NeonGreen
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                CyberTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = "Texto de WhatsApp (con formato *negrita*, etc.)",
                    placeholder = "Escribe el texto de respuesta rápida...",
                    singleLine = false,
                    maxLines = 6,
                    testTag = "template_content_input"
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isPinned,
                        onCheckedChange = { isPinned = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = NeonCyan,
                            checkmarkColor = Color(0xFF0A0A0E),
                            uncheckedColor = CyberBorder
                        )
                    )
                    Text(
                        text = "Fijar al inicio de la lista",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        color = TextWhite
                    )
                }

                if (errorMessage != null) {
                    Text(
                        text = errorMessage ?: "",
                        color = Color(0xFFFF3366),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(
                            text = "CANCELAR",
                            fontFamily = FontFamily.Monospace,
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    NeonButton(
                        text = "GUARDAR",
                        color = NeonCyan,
                        onClick = {
                            if (title.isBlank() || content.isBlank()) {
                                errorMessage = "Por favor ingresa título y contenido"
                                return@NeonButton
                            }
                            val updated = initialTemplate?.copy(
                                title = title.trim(),
                                content = content.trim(),
                                category = category,
                                shortcut = shortcut.trim(),
                                isPinned = isPinned
                            ) ?: TemplateEntity(
                                title = title.trim(),
                                content = content.trim(),
                                category = category,
                                shortcut = shortcut.trim(),
                                isPinned = isPinned
                            )
                            onSave(updated)
                        },
                        testTag = "save_template_button"
                    )
                }
            }
        }
    }
}

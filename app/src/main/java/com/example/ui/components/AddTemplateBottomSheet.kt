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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Icon
import com.example.data.Template
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberCardVariant
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

/**
 * Cyberpunk Modal Bottom Sheet for adding or modifying WhatsApp sales templates.
 * When [initialTemplate] is provided, fields are pre-filled with existing content.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddTemplateBottomSheet(
    onDismissRequest: () -> Unit,
    onSaveTemplate: (Template) -> Unit,
    modifier: Modifier = Modifier,
    initialTemplate: Template? = null,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    val isEditing = initialTemplate != null
    var title by remember(initialTemplate) { mutableStateOf(initialTemplate?.title ?: "") }
    var content by remember(initialTemplate) { mutableStateOf(initialTemplate?.content ?: "") }
    var category by remember(initialTemplate) { mutableStateOf(initialTemplate?.category ?: "Ventas") }
    var isPinned by remember(initialTemplate) { mutableStateOf(initialTemplate?.isPinned ?: false) }
    var isAiCopilotOpen by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val categories = listOf("Ventas", "Soporte", "Seguimiento", "Precios", "Pagos", "Info", "Saludos", "Envíos", "Cierre", "Promos")
    val placeholders = listOf("{nombre}", "{producto}", "{precio}", "{descuento}")

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = Color(0xFF12121A), // Fondo Oscuro Cyberpunk
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 4.dp)
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFF2A2A3D))
            )
        },
        modifier = modifier.testTag("add_template_modal_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
                .padding(bottom = 20.dp, top = 8.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            // Header
            Text(
                text = if (isEditing) "// EDITAR PLANTILLA" else "// NUEVA PLANTILLA",
                fontFamily = FontFamily.Monospace,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00E5FF), // Azul Neón
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo 1: Título
            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    if (errorMessage != null) errorMessage = null
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("modal_template_title_input"),
                label = { Text("Título (ej. ⚡ Datos de Pago)", color = Color.Gray, fontSize = 13.sp) },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF161622),
                    unfocusedContainerColor = Color(0xFF161622),
                    focusedBorderColor = Color(0xFF00E5FF),
                    unfocusedBorderColor = Color(0xFF2A2A3D),
                    cursorColor = Color(0xFF00E5FF)
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Category selector
            Text(
                text = "CATEGORÍA (FILTRO RÁPIDO)",
                fontFamily = FontFamily.Monospace,
                fontSize = 10.5.sp,
                color = TextMuted,
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(6.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                categories.forEach { cat ->
                    val isSelected = category.equals(cat, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSelected) NeonCyan else CyberCardVariant)
                            .border(1.dp, if (isSelected) NeonCyan else CyberBorder, RoundedCornerShape(6.dp))
                            .clickable { category = cat }
                            .padding(horizontal = 9.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = cat,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color(0xFF0A0A0E) else TextWhite
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Quick Insert Placeholders
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Insertar variable:",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.5.sp,
                    color = TextMuted
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    placeholders.forEach { tag ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(NeonGreen.copy(alpha = 0.15f))
                                .border(1.dp, NeonGreen.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                                .clickable { content = if (content.isEmpty()) tag else "$content $tag" }
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

            Spacer(modifier = Modifier.height(10.dp))

            // 🤖 AI Persuasion Assistant Tones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "⚡ ASISTENTE IA DE PERSUASIÓN",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonCyan,
                    letterSpacing = 0.8.sp
                )
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF261238))
                        .border(1.dp, NeonPurple, RoundedCornerShape(6.dp))
                        .clickable { isAiCopilotOpen = true }
                        .padding(horizontal = 7.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Copiloto",
                        tint = NeonPurple,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "⚡ COPILOTO GEMINI",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonPurple
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                com.example.util.SalesAiAssistant.PersuasionTone.values().forEach { tone ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF162032))
                            .border(1.dp, NeonCyan.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                            .clickable {
                                content = com.example.util.SalesAiAssistant.optimizeTemplate(
                                    originalText = content,
                                    tone = tone,
                                    clientName = "{nombre}",
                                    productName = "{producto}"
                                )
                                if (title.isBlank()) {
                                    title = "${tone.emoji} ${tone.labelEs}"
                                }
                            }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${tone.emoji} ${tone.labelEs}",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            color = NeonCyan
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Campo 2: Contenido
            OutlinedTextField(
                value = content,
                onValueChange = {
                    content = it
                    if (errorMessage != null) errorMessage = null
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("modal_template_content_input"),
                label = { Text("Mensaje de WhatsApp (*con formato*)", color = Color.Gray, fontSize = 13.sp) },
                maxLines = 5,
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF161622),
                    unfocusedContainerColor = Color(0xFF161622),
                    focusedBorderColor = Color(0xFF00E5FF),
                    unfocusedBorderColor = Color(0xFF2A2A3D),
                    cursorColor = Color(0xFF00E5FF)
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Pin to top option
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isPinned,
                    onCheckedChange = { isPinned = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF00E5FF),
                        checkmarkColor = Color.Black,
                        uncheckedColor = CyberBorder
                    )
                )
                Text(
                    text = "Fijar plantilla al inicio de la lista",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.5.sp,
                    color = TextWhite
                )
            }

            if (errorMessage != null) {
                Text(
                    text = errorMessage ?: "",
                    color = Color(0xFFFF3366),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.5.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Botón de Guardar / Actualizar
            Button(
                onClick = {
                    if (title.isBlank() || content.isBlank()) {
                        errorMessage = "Por favor completa título y mensaje"
                        return@Button
                    }
                    val targetTemplate = initialTemplate?.copy(
                        title = title.trim(),
                        content = content.trim(),
                        category = category,
                        isPinned = isPinned
                    ) ?: Template(
                        title = title.trim(),
                        content = content.trim(),
                        category = category,
                        isPinned = isPinned
                    )
                    onSaveTemplate(targetTemplate)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("save_template_modal_button"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00E5FF), // Azul Neón
                    contentColor = Color.Black
                ),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 14.dp)
            ) {
                Text(
                    text = if (isEditing) "ACTUALIZAR PLANTILLA" else "GUARDAR PLANTILLA",
                    style = androidx.compose.ui.text.TextStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.5.sp,
                        letterSpacing = 1.sp
                    )
                )
            }
        }
    }

    if (isAiCopilotOpen) {
        AiSalesCopilotBottomSheet(
            onDismissRequest = { isAiCopilotOpen = false },
            onSaveAsTemplate = { tTitle, tContent, _ ->
                title = tTitle
                content = tContent
                isAiCopilotOpen = false
            },
            onUseGeneratedText = { generatedText ->
                content = generatedText
                if (title.isBlank()) {
                    title = "⚡ Copy IA Gemini"
                }
                isAiCopilotOpen = false
            },
            initialPrompt = content
        )
    }
}

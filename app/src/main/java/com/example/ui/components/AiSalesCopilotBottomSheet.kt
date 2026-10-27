package com.example.ui.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberCard
import com.example.ui.theme.CyberCardVariant
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.util.LocalIsEnglish
import com.example.util.SalesAiAssistant
import com.example.util.tr
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AiSalesCopilotBottomSheet(
    onDismissRequest: () -> Unit,
    onSaveAsTemplate: (title: String, content: String, category: String) -> Unit,
    onUseGeneratedText: (String) -> Unit = {},
    initialClientName: String = "",
    initialProductName: String = "",
    initialPrompt: String = ""
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val scope = rememberCoroutineScope()
    val isEnglish = LocalIsEnglish.current

    var instruction by remember { mutableStateOf(initialPrompt) }
    var clientName by remember { mutableStateOf(initialClientName) }
    var productName by remember { mutableStateOf(initialProductName) }
    var selectedTone by remember { mutableStateOf(SalesAiAssistant.PersuasionTone.BENEFITS_AIDA) }

    var isGenerating by remember { mutableStateOf(false) }
    var generatedResult by remember { mutableStateOf("") }
    var lastError by remember { mutableStateOf<String?>(null) }

    val quickIdeas = remember(isEnglish) {
        if (isEnglish) {
            listOf(
                "20% discount if closed today",
                "Warm follow-up to quote sent yesterday",
                "Limited stock alert (only 3 spots left)",
                "Explain why our price reflects top quality"
            )
        } else {
            listOf(
                "Descuento 20% si confirma hoy",
                "Seguimiento cálido a cotización de ayer",
                "Últimas 3 unidades en stock con envío gratis",
                "Manejo de objeción: nuestro valor vs competencia"
            )
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = CyberBlack,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(NeonPurple.copy(alpha = 0.2f))
                            .border(1.dp, NeonPurple, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Gemini AI",
                            tint = NeonPurple,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = tr("COPILOTO IA DE VENTAS", "AI SALES COPILOT"),
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = TextWhite,
                            letterSpacing = 1.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(NeonGreen)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "GEMINI 3.5 FLASH // ENGINE ACTIVE",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.5.sp,
                                color = NeonGreen
                            )
                        }
                    }
                }

                IconButton(
                    onClick = onDismissRequest,
                    modifier = Modifier.testTag("ai_copilot_close")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Objective / Description Input
            Text(
                text = tr("1. ¿QUÉ DESEAS COMUNICAR O VENDER?", "1. WHAT DO YOU WANT TO COMMUNICATE?"),
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = NeonCyan,
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = instruction,
                onValueChange = { instruction = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ai_instruction_input"),
                placeholder = {
                    Text(
                        text = tr(
                            "ej. Invitar a comprar el nuevo paquete con 20% de descuento solo por hoy...",
                            "e.g. Invite to buy the new package with 20% discount today only..."
                        ),
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                },
                maxLines = 4,
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = CyberCard,
                    unfocusedContainerColor = CyberCard,
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = CyberBorder
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Quick Idea Chips
            Text(
                text = tr("Ideas rápidas:", "Quick ideas:"),
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp,
                color = TextMuted
            )
            Spacer(modifier = Modifier.height(4.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                quickIdeas.forEach { idea ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(CyberCardVariant)
                            .border(1.dp, CyberBorder, RoundedCornerShape(6.dp))
                            .clickable { instruction = idea }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "💡 $idea",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            color = NeonCyan
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Client and Product Fields
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = tr("PROSPECTO (OPCIONAL)", "PROSPECT (OPTIONAL)"),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = clientName,
                        onValueChange = { clientName = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("ej. María", color = Color.Gray, fontSize = 12.sp) },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = CyberCard,
                            unfocusedContainerColor = CyberCard,
                            focusedBorderColor = NeonPurple,
                            unfocusedBorderColor = CyberBorder
                        )
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = tr("PRODUCTO / OFERTA", "PRODUCT / OFFER"),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = productName,
                        onValueChange = { productName = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("ej. Zapatillas", color = Color.Gray, fontSize = 12.sp) },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = CyberCard,
                            unfocusedContainerColor = CyberCard,
                            focusedBorderColor = NeonPurple,
                            unfocusedBorderColor = CyberBorder
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Tone Selection
            Text(
                text = tr("2. ESTRATEGIA & TONO DE PERSUASIÓN", "2. SALES STRATEGY & TONE"),
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = NeonCyan,
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(6.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                SalesAiAssistant.PersuasionTone.values().forEach { tone ->
                    val isSelected = tone == selectedTone
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) NeonPurple else CyberCardVariant)
                            .border(
                                width = 1.dp,
                                color = if (isSelected) NeonPurple else CyberBorder,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { selectedTone = tone }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${tone.emoji} ${if (isEnglish) tone.labelEn else tone.labelEs}",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.5.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) TextWhite else TextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Generate Button
            Button(
                onClick = {
                    if (isGenerating) return@Button
                    isGenerating = true
                    lastError = null

                    scope.launch {
                        try {
                            val result = SalesAiAssistant.generateSalesPitchWithGemini(
                                instruction = instruction,
                                tone = selectedTone,
                                clientName = clientName,
                                productName = productName,
                                isEnglish = isEnglish
                            )
                            generatedResult = result
                        } catch (e: Exception) {
                            lastError = e.localizedMessage
                        } finally {
                            isGenerating = false
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("ai_generate_button"),
                enabled = !isGenerating,
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonPurple,
                    contentColor = TextWhite
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                if (isGenerating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = TextWhite,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = tr("GENERANDO CON GEMINI 3.5 FLASH...", "GENERATING WITH GEMINI 3.5 FLASH..."),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = tr("⚡ GENERAR COPY CON IA GEMINI", "⚡ GENERATE COPY WITH GEMINI AI"),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }

            // Error display if any
            if (lastError != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "⚠️ $lastError",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = NeonPink
                )
            }

            // Result Section
            AnimatedVisibility(visible = generatedResult.isNotBlank()) {
                Column(modifier = Modifier.padding(top = 18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = tr("✨ COPY PERSUASIVO GENERADO", "✨ GENERATED PERSUASIVE COPY"),
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.5.sp,
                            color = NeonGreen,
                            letterSpacing = 1.sp
                        )
                        IconButton(
                            onClick = {
                                if (isGenerating) return@IconButton
                                isGenerating = true
                                scope.launch {
                                    try {
                                        generatedResult = SalesAiAssistant.generateSalesPitchWithGemini(
                                            instruction = instruction,
                                            tone = selectedTone,
                                            clientName = clientName,
                                            productName = productName,
                                            isEnglish = isEnglish
                                        )
                                    } finally {
                                        isGenerating = false
                                    }
                                }
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Regenerar",
                                tint = NeonCyan,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Text Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF0F1420))
                            .border(1.dp, NeonGreen.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                            .padding(14.dp)
                    ) {
                        Text(
                            text = generatedResult,
                            fontFamily = FontFamily.Default,
                            fontSize = 13.5.sp,
                            lineHeight = 19.sp,
                            color = TextWhite
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Copy Button
                        Button(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(generatedResult))
                                Toast.makeText(
                                    context,
                                    if (isEnglish) "Copied to clipboard!" else "¡Copiado al portapapeles!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CyberCardVariant,
                                contentColor = NeonCyan
                            )
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = tr("COPIAR", "COPY"),
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Save as Template Button
                        Button(
                            onClick = {
                                val titleStr = "${selectedTone.emoji} ${selectedTone.labelEs}"
                                onSaveAsTemplate(titleStr, generatedResult, "VENTAS")
                                Toast.makeText(
                                    context,
                                    if (isEnglish) "Saved as template!" else "¡Guardado como plantilla!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CyberCardVariant,
                                contentColor = NeonGreen
                            )
                        ) {
                            Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = tr("GUARDAR", "SAVE"),
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Use in Chat / Send Button
                        Button(
                            onClick = {
                                onUseGeneratedText(generatedResult)
                                onDismissRequest()
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NeonGreen,
                                contentColor = Color(0xFF0A0A0E)
                            )
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = tr("USAR", "USE"),
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

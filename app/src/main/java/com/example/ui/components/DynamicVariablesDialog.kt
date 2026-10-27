package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
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
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.util.tr

/**
 * Cyberpunk Dynamic Variable Filler Bottom Sheet.
 * Detects tags like {nombre}, {producto}, {precio} in the template
 * and lets the seller quickly fill them before firing to WhatsApp or copying.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DynamicVariablesDialog(
    template: TemplateEntity,
    onDismissRequest: () -> Unit,
    onSendWhatsApp: (personalizedText: String, phone: String?) -> Unit,
    onCopyPersonalized: (personalizedText: String) -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    // Detect variables formatted as {variable_name}
    val variableNames = remember(template.content) {
        Regex("\\{([^}]+)\\}").findAll(template.content)
            .map { it.groupValues[1] }
            .distinct()
            .toList()
    }

    val variableValues = remember {
        mutableStateMapOf<String, String>().apply {
            variableNames.forEach { put(it, "") }
        }
    }

    var targetPhone by remember { mutableStateOf("") }

    // Live preview of text with replaced values
    val personalizedContent by remember {
        derivedStateOf {
            var result = template.content
            variableNames.forEach { name ->
                val replacement = variableValues[name]?.takeIf { it.isNotBlank() } ?: "{$name}"
                result = result.replace("{$name}", replacement)
            }
            result
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = Color(0xFF12121A),
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
        modifier = modifier.testTag("dynamic_variables_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
                .padding(bottom = 20.dp, top = 6.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = null,
                    tint = NeonGreen,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = tr("// PERSONALIZAR ANTES DE ENVIAR", "// CUSTOMIZE BEFORE SENDING"),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonGreen,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = template.title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Dynamic Variable Fields
            Text(
                text = "${tr("CAMPOS DETECTADOS", "DETECTED FIELDS")} (${variableNames.size})",
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                color = TextMuted,
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            variableNames.forEach { varName ->
                val currentValue = variableValues[varName] ?: ""
                val humanLabel = when (varName.lowercase()) {
                    "nombre" -> tr("Nombre del cliente", "Client name")
                    "producto" -> tr("Producto / Servicio", "Product / Service")
                    "precio" -> tr("Precio o Monto", "Price or Amount")
                    "descuento" -> tr("Porcentaje / Cupón de descuento", "Discount / Coupon percentage")
                    "fecha" -> tr("Fecha o Día", "Date or Day")
                    else -> "${tr("Valor para", "Value for")} {$varName}"
                }

                OutlinedTextField(
                    value = currentValue,
                    onValueChange = { variableValues[varName] = it },
                    label = { Text(humanLabel, fontSize = 12.sp, color = Color.Gray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .testTag("variable_input_$varName"),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color(0xFF161622),
                        unfocusedContainerColor = Color(0xFF161622),
                        focusedBorderColor = NeonGreen,
                        unfocusedBorderColor = Color(0xFF2A2A3D),
                        cursorColor = NeonGreen
                    )
                )
            }

            // Optional direct phone
            OutlinedTextField(
                value = targetPhone,
                onValueChange = { targetPhone = it },
                label = { Text(tr("Teléfono de destino (Opcional)", "Destination Phone (Optional)"), fontSize = 12.sp, color = TextMuted) },
                placeholder = { Text(tr("Ej: +58 412 1234567 (o dejar vacío)", "e.g. +1 305 1234567 (or leave blank)"), fontSize = 12.sp, color = Color.DarkGray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .testTag("optional_phone_input"),
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF161622),
                    unfocusedContainerColor = Color(0xFF161622),
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = Color(0xFF2A2A3D)
                )
            )

            // Live Preview Box
            Text(
                text = tr("VISTA PREVIA DEL MENSAJE", "MESSAGE PREVIEW"),
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                color = TextMuted,
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(6.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF0F0F17))
                    .border(1.dp, Color(0xFF2A2A3D), RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = personalizedContent,
                    fontSize = 12.5.sp,
                    color = TextWhite,
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Secondary: Copiar personalizado
                OutlinedButton(
                    onClick = {
                        onCopyPersonalized(personalizedContent)
                        onDismissRequest()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("copy_personalized_button"),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonCyan)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = tr("COPIAR", "COPY"),
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                // Primary: Enviar a WhatsApp
                Button(
                    onClick = {
                        onSendWhatsApp(
                            personalizedContent,
                            targetPhone.takeIf { it.isNotBlank() }
                        )
                        onDismissRequest()
                    },
                    modifier = Modifier
                        .weight(1.4f)
                        .height(48.dp)
                        .testTag("send_whatsapp_personalized_button"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF25D366),
                        contentColor = Color.Black
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = tr("ENVIAR A WHATSAPP", "SEND TO WHATSAPP"),
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

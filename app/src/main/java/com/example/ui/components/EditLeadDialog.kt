package com.example.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import com.example.data.LeadEntity
import com.example.data.LeadStage
import com.example.ui.theme.CyberCard
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.TextMuted

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EditLeadDialog(
    initialLead: LeadEntity?,
    onDismiss: () -> Unit,
    onSave: (LeadEntity) -> Unit
) {
    var name by remember { mutableStateOf(initialLead?.name ?: "") }
    var phone by remember { mutableStateOf(initialLead?.phone ?: "+58") }
    var stage by remember { mutableStateOf(initialLead?.stage ?: LeadStage.NEW) }
    var productInterest by remember { mutableStateOf(initialLead?.productInterest ?: "") }
    var estimatedValueStr by remember { mutableStateOf(if (initialLead != null && initialLead.estimatedValue > 0) initialLead.estimatedValue.toString() else "") }
    var notes by remember { mutableStateOf(initialLead?.notes ?: "") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    BasicAlertDialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(1.5.dp, NeonGreen, RoundedCornerShape(16.dp))
                .testTag("edit_lead_dialog"),
            color = CyberCard
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = if (initialLead == null) "👤 NUEVO CLIENTE / PROSPECTO" else "👤 EDITAR CLIENTE",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = NeonGreen,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                CyberTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Nombre del Cliente",
                    placeholder = "ej. Carlos Mendoza",
                    testTag = "lead_name_input"
                )

                Spacer(modifier = Modifier.height(12.dp))

                CyberTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = "Teléfono de WhatsApp con código país",
                    placeholder = "ej. +584121234567",
                    testTag = "lead_phone_input"
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "ESTADO DEL PIPELINE",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = TextMuted
                )
                Spacer(modifier = Modifier.height(6.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    LeadStage.values().forEach { st ->
                        CyberFilterChip(
                            text = st.label,
                            selected = stage == st,
                            onClick = { stage = st },
                            activeColor = Color(st.colorHex)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                CyberTextField(
                    value = productInterest,
                    onValueChange = { productInterest = it },
                    label = "Producto o Interés",
                    placeholder = "ej. Combo Opción B ($20)",
                    testTag = "lead_product_input"
                )

                Spacer(modifier = Modifier.height(12.dp))

                CyberTextField(
                    value = estimatedValueStr,
                    onValueChange = { estimatedValueStr = it },
                    label = "Valor Estimado (\$ USD)",
                    placeholder = "ej. 20.00",
                    testTag = "lead_value_input"
                )

                Spacer(modifier = Modifier.height(12.dp))

                CyberTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = "Notas de Seguimiento",
                    placeholder = "ej. Preguntó por garantía y delivery",
                    singleLine = false,
                    maxLines = 4,
                    testTag = "lead_notes_input"
                )

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
                        color = NeonGreen,
                        onClick = {
                            if (name.isBlank() || phone.isBlank()) {
                                errorMessage = "Por favor ingresa nombre y teléfono"
                                return@NeonButton
                            }
                            val parsedVal = estimatedValueStr.toDoubleOrNull() ?: 0.0
                            val updated = initialLead?.copy(
                                name = name.trim(),
                                phone = phone.trim(),
                                stage = stage,
                                productInterest = productInterest.trim(),
                                estimatedValue = parsedVal,
                                notes = notes.trim(),
                                lastContactedAt = System.currentTimeMillis()
                            ) ?: LeadEntity(
                                name = name.trim(),
                                phone = phone.trim(),
                                stage = stage,
                                productInterest = productInterest.trim(),
                                estimatedValue = parsedVal,
                                notes = notes.trim(),
                                lastContactedAt = System.currentTimeMillis()
                            )
                            onSave(updated)
                        },
                        testTag = "save_lead_button"
                    )
                }
            }
        }
    }
}

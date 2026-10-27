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
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberCard
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.TextMuted
import com.example.util.tr

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EditLeadDialog(
    initialLead: LeadEntity?,
    onDismiss: () -> Unit,
    onSave: (LeadEntity) -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
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
                .border(1.5.dp, NeonGreen, RoundedCornerShape(16.dp)),
            color = CyberCard
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = if (initialLead == null)
                        tr("👤 NUEVO CLIENTE / PROSPECTO", "👤 NEW CLIENT / LEAD")
                    else
                        tr("👤 EDITAR CLIENTE", "👤 EDIT CLIENT"),
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
                    label = tr("Nombre del Cliente", "Client Name"),
                    placeholder = tr("ej. Carlos Mendoza", "e.g. John Doe"),
                    testTag = "lead_name_input"
                )

                Spacer(modifier = Modifier.height(12.dp))

                CyberTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = tr("Teléfono WhatsApp (con código país)", "WhatsApp Phone (with country code)"),
                    placeholder = tr("ej. +584121234567", "e.g. +13051234567"),
                    testTag = "lead_phone_input"
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = tr("ESTADO DEL PIPELINE", "PIPELINE STAGE"),
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
                            text = st.localizedLabel(),
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
                    label = tr("Producto o Interés", "Product or Interest"),
                    placeholder = tr("ej. Combo Opción B ($20)", "e.g. Pro Plan ($49)"),
                    testTag = "lead_product_input"
                )

                Spacer(modifier = Modifier.height(12.dp))

                CyberTextField(
                    value = estimatedValueStr,
                    onValueChange = { estimatedValueStr = it },
                    label = tr("Valor Estimado (\$ USD)", "Estimated Value (\$ USD)"),
                    placeholder = "ej. 20.00",
                    testTag = "lead_value_input"
                )

                Spacer(modifier = Modifier.height(12.dp))

                CyberTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = tr("Notas de Seguimiento", "Follow-up Notes"),
                    placeholder = tr("ej. Preguntó por garantía y delivery", "e.g. Asked about warranty and shipping"),
                    singleLine = false,
                    maxLines = 4,
                    testTag = "lead_notes_input"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ⏰ Programar Recordatorio de Seguimiento
                Text(
                    text = tr("⏰ RECORDATORIO DE SEGUIMIENTO", "⏰ FOLLOW-UP REMINDER"),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700),
                    letterSpacing = 0.8.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                var reminderMinutes by remember { mutableStateOf<Int?>(null) }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val reminderOptions = listOf(
                        15 to tr("En 15 min", "In 15 min"),
                        60 to tr("En 1 hora", "In 1 hr"),
                        240 to tr("En 4 horas", "In 4 hrs"),
                        1440 to tr("Mañana", "Tomorrow")
                    )
                    reminderOptions.forEach { (mins, label) ->
                        val isSelected = reminderMinutes == mins
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) Color(0xFFFFD700).copy(alpha = 0.25f) else Color(0xFF161622))
                                .border(1.dp, if (isSelected) Color(0xFFFFD700) else CyberBorder, RoundedCornerShape(6.dp))
                                .clickable {
                                    reminderMinutes = if (isSelected) null else mins
                                }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.5.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color(0xFFFFD700) else TextMuted
                            )
                        }
                    }
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

                val validationErrorMsg = tr("Por favor ingresa nombre y teléfono", "Please enter client name and phone")

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(
                            text = tr("CANCELAR", "CANCEL"),
                            fontFamily = FontFamily.Monospace,
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    NeonButton(
                        text = tr("GUARDAR", "SAVE"),
                        color = NeonGreen,
                        onClick = {
                            if (name.isBlank() || phone.isBlank()) {
                                errorMessage = validationErrorMsg
                                return@NeonButton
                            }
                            val parsedVal = estimatedValueStr.toDoubleOrNull() ?: 0.0
                            val finalLead = initialLead?.copy(
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

                            if (reminderMinutes != null) {
                                val triggerMs = System.currentTimeMillis() + (reminderMinutes!! * 60 * 1000L)
                                com.example.util.ReminderBroadcastReceiver.scheduleReminder(
                                    context = context,
                                    leadId = finalLead.id.takeIf { it != 0L } ?: System.currentTimeMillis(),
                                    leadName = finalLead.name,
                                    phone = finalLead.phone,
                                    notes = if (notes.isNotBlank()) notes else "Hacer seguimiento comercial por WhatsApp",
                                    triggerTimeMs = triggerMs
                                )
                            }

                            onSave(finalLead)
                        },
                        testTag = "save_lead_button"
                    )
                }
            }
        }
    }
}

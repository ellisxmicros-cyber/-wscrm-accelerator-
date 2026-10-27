package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CustomerInteractionEntity
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextMuted
import com.example.util.tr
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Bottom sheet displaying customer interaction history stored in Room Database.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerInteractionHistorySheet(
    interactions: List<CustomerInteractionEntity>,
    onDeleteInteraction: (Long) -> Unit,
    onDirectWhatsApp: (phone: String, text: String) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = Color(0xFF12121A),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 4.dp)
                    .width(42.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFF2A2A3D))
            )
        },
        modifier = modifier.testTag("customer_interaction_history_modal")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp, top = 6.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = tr("HISTORIAL DE CLIENTES", "CLIENT INTERACTION HISTORY"),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(NeonCyan.copy(alpha = 0.15f))
                        .border(1.dp, NeonCyan.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${interactions.size} ${tr("REGISTROS", "RECORDS")}",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.5.sp,
                        color = NeonCyan,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = tr(
                    "Registro local persistente de mensajes y cambios de embudo en Room SQLite",
                    "Persistent local log of messages and pipeline stage changes in Room SQLite"
                ),
                fontSize = 11.5.sp,
                color = TextMuted,
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
            )

            if (interactions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tr(
                            "No hay interacciones registradas aún.\nEnvía plantillas o actualiza leads para comenzar.",
                            "No logged interactions yet.\nSend templates or update leads to begin."
                        ),
                        color = TextMuted,
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Monospace,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(420.dp)
                ) {
                    items(interactions, key = { it.id }) { interaction ->
                        InteractionItemRow(
                            interaction = interaction,
                            formattedDate = dateFormat.format(Date(interaction.timestamp)),
                            onDelete = { onDeleteInteraction(interaction.id) },
                            onLaunchWhatsApp = {
                                onDirectWhatsApp(interaction.customerPhone, interaction.content)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InteractionItemRow(
    interaction: CustomerInteractionEntity,
    formattedDate: String,
    onDelete: () -> Unit,
    onLaunchWhatsApp: () -> Unit
) {
    val (typeIcon, typeColor, typeLabel) = when (interaction.interactionType) {
        CustomerInteractionEntity.TYPE_TEMPLATE_SENT -> Triple(
            Icons.Default.Message,
            NeonGreen,
            tr("PLANTILLA", "TEMPLATE")
        )
        CustomerInteractionEntity.TYPE_STAGE_CHANGE -> Triple(
            Icons.Default.SwapHoriz,
            NeonPurple,
            tr("EMBUDO", "PIPELINE")
        )
        CustomerInteractionEntity.TYPE_NOTE -> Triple(
            Icons.Default.Tune,
            NeonCyan,
            tr("NOTA", "NOTE")
        )
        else -> Triple(
            Icons.Default.Phone,
            NeonGreen,
            "WHATSAPP"
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF161622))
            .border(1.dp, Color(0xFF2A2A3D), RoundedCornerShape(12.dp))
            .padding(12.dp)
            .testTag("interaction_item_${interaction.id}")
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(typeColor.copy(alpha = 0.15f))
                            .border(1.dp, typeColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = typeIcon,
                            contentDescription = null,
                            tint = typeColor,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = if (interaction.customerName.isNotBlank()) interaction.customerName else interaction.customerPhone,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.5.sp,
                            color = Color.White
                        )
                        if (interaction.customerName.isNotBlank() && interaction.customerPhone.isNotBlank()) {
                            Text(
                                text = interaction.customerPhone,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.5.sp,
                                color = TextMuted
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = formattedDate,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        color = TextMuted
                    )

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = tr("Eliminar", "Delete"),
                            tint = Color(0xFFFF3366).copy(alpha = 0.6f),
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }

            if (!interaction.templateTitle.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "📄 ${interaction.templateTitle}",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = NeonCyan,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = interaction.content,
                fontSize = 12.sp,
                color = Color(0xFFDCDCE8),
                lineHeight = 16.sp
            )
        }
    }
}

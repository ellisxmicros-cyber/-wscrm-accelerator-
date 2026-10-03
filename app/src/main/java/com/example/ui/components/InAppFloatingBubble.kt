package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.TemplateEntity
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberCard
import com.example.ui.theme.CyberCardVariant
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import kotlin.math.roundToInt

@Composable
fun InAppFloatingBubble(
    templates: List<TemplateEntity>,
    onCopyTemplate: (TemplateEntity) -> Unit,
    onLaunchWhatsApp: () -> Unit,
    onCloseBubble: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val density = LocalDensity.current
        val maxWidthPx = with(density) { (maxWidth - 70.dp).toPx() }
        val maxHeightPx = with(density) { (maxHeight - 160.dp).toPx() }

        var offsetX by remember { mutableFloatStateOf(maxWidthPx - 20f) }
        var offsetY by remember { mutableFloatStateOf(maxHeightPx * 0.4f) }

        // Floating draggable bubble
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .size(62.dp)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offsetX = (offsetX + dragAmount.x).coerceIn(10f, maxWidthPx)
                        offsetY = (offsetY + dragAmount.y).coerceIn(50f, maxHeightPx)
                    }
                }
                .shadow(elevation = 16.dp, shape = CircleShape, spotColor = NeonGreen)
                .clip(CircleShape)
                .background(CyberBlack)
                .border(2.dp, NeonGreen, CircleShape)
                .clickable { isExpanded = true }
                .testTag("floating_overlay_bubble"),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.FlashOn,
                contentDescription = "Burbuja Flotante",
                tint = NeonGreen,
                modifier = Modifier.size(34.dp)
            )
        }

        // Expanded floating HUD modal
        if (isExpanded) {
            Dialog(onDismissRequest = { isExpanded = false }) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(480.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.5.dp, NeonCyan, RoundedCornerShape(16.dp))
                        .testTag("floating_hud_panel"),
                    color = CyberBlack
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp)
                    ) {
                        // Title Bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.FlashOn,
                                    contentDescription = null,
                                    tint = NeonGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "WS COMPANION // HUD",
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = NeonCyan,
                                    fontSize = 14.sp,
                                    letterSpacing = 1.sp
                                )
                            }
                            IconButton(onClick = { isExpanded = false }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Cerrar HUD",
                                    tint = TextMuted
                                )
                            }
                        }

                        // WhatsApp Action bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(CyberCardVariant)
                                .border(1.dp, CyberBorder, RoundedCornerShape(8.dp))
                                .clickable {
                                    onLaunchWhatsApp()
                                    isExpanded = false
                                }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Abrir WhatsApp Directo",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp,
                                color = NeonGreen
                            )
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = null,
                                tint = NeonGreen,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        var selectedFilter by remember { mutableStateOf("Todos") }
                        val categories = remember(templates) {
                            listOf("Todos") + templates.map { it.category }.distinct()
                        }

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(categories) { cat ->
                                CyberFilterChip(
                                    text = cat,
                                    selected = selectedFilter == cat,
                                    onClick = { selectedFilter = cat }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        val filteredList = if (selectedFilter == "Todos") {
                            templates
                        } else {
                            templates.filter { it.category.equals(selectedFilter, ignoreCase = true) }
                        }

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        ) {
                            items(filteredList, key = { it.id }) { template ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = CardDefaults.cardColors(containerColor = CyberCard),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberBorder)
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = template.title,
                                                fontWeight = FontWeight.Bold,
                                                color = TextWhite,
                                                fontSize = 13.sp,
                                                modifier = Modifier.weight(1f),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            NeonOutlinedButton(
                                                text = "COPIAR",
                                                color = NeonCyan,
                                                icon = Icons.Default.ContentCopy,
                                                onClick = {
                                                    onCopyTemplate(template)
                                                    isExpanded = false
                                                },
                                                testTag = "hud_copy_${template.id}"
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = template.content,
                                            color = TextMuted,
                                            fontSize = 11.5.sp,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Toca 'COPIAR' y se cerrará para pegar en WhatsApp",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = TextMuted
                            )
                        }
                    }
                }
            }
        }
    }
}

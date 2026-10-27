package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LeadEntity
import com.example.data.LeadStage
import com.example.ui.components.CyberCard
import com.example.ui.components.CyberFilterChip
import com.example.ui.components.CyberTag
import com.example.ui.components.CyberTextField
import com.example.ui.components.EditLeadDialog
import com.example.ui.components.NeonOutlinedButton
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberCard
import com.example.ui.theme.CyberCardVariant
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.util.tr
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrmPipelineScreen(
    leads: List<LeadEntity>,
    allLeads: List<LeadEntity>,
    searchQuery: String,
    selectedStage: LeadStage?,
    onSearchChange: (String) -> Unit,
    onStageFilterChange: (LeadStage?) -> Unit,
    onSaveLead: (LeadEntity) -> Unit,
    onDeleteLead: (LeadEntity) -> Unit,
    onUpdateStage: (Long, LeadStage) -> Unit,
    onOpenWhatsApp: (phone: String, name: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var editingLead by remember { mutableStateOf<LeadEntity?>(null) }
    var isCreatingNew by remember { mutableStateOf(false) }
    var aiLeadTarget by remember { mutableStateOf<LeadEntity?>(null) }

    // Metrics calculations
    val totalLeads = allLeads.size
    val wonDeals = allLeads.filter { it.stage == LeadStage.CLOSED_WON }.size
    val totalPipelineValue = allLeads.sumOf { it.estimatedValue }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = CyberBlack,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = tr("TURBOVENTAS // PIPELINE", "TURBOVENTAS // PIPELINE"),
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 2.sp,
                        color = NeonGreen,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF12121A)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isCreatingNew = true },
                containerColor = NeonGreen,
                contentColor = Color(0xFF0A0A0E),
                shape = CircleShape,
                modifier = Modifier
                    .shadow(elevation = 12.dp, shape = CircleShape, spotColor = NeonGreen)
                    .testTag("add_lead_fab")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = tr("Agregar cliente o prospecto", "Add client or lead"),
                    modifier = Modifier.size(28.dp),
                    tint = Color.Black
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // CRM Dashboard Metrics Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PipelineMetricCard(
                    title = tr("PROSPECTOS", "LEADS"),
                    value = "$totalLeads",
                    accentColor = NeonCyan,
                    modifier = Modifier.weight(1f)
                )
                PipelineMetricCard(
                    title = tr("VENTAS", "WON DEALS"),
                    value = "$wonDeals",
                    accentColor = NeonGreen,
                    modifier = Modifier.weight(1f)
                )
                PipelineMetricCard(
                    title = tr("VALOR USD", "USD VALUE"),
                    value = "\$${"%.0f".format(totalPipelineValue)}",
                    accentColor = Color(0xFFFFE600),
                    modifier = Modifier.weight(1.2f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Search Bar
            CyberTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                label = tr("BUSCAR EN CRM", "SEARCH IN CRM"),
                placeholder = tr("Buscar por cliente, teléfono, notas...", "Search by client, phone, notes..."),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = NeonGreen,
                        modifier = Modifier.size(18.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { onSearchChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = tr("Limpiar", "Clear"),
                                tint = TextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                },
                testTag = "crm_search_input"
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Stage Filter Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    CyberFilterChip(
                        text = tr("Todos (${allLeads.size})", "All (${allLeads.size})"),
                        selected = selectedStage == null,
                        onClick = { onStageFilterChange(null) },
                        activeColor = NeonGreen
                    )
                }
                items(LeadStage.values()) { stage ->
                    val count = allLeads.count { it.stage == stage }
                    CyberFilterChip(
                        text = "${stage.localizedLabel()} ($count)",
                        selected = selectedStage == stage,
                        onClick = { onStageFilterChange(stage) },
                        activeColor = Color(stage.colorHex)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (leads.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = CyberBorder,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = tr("No hay prospectos en esta etapa", "No leads in this stage"),
                            fontFamily = FontFamily.Monospace,
                            color = TextMuted,
                            fontSize = 13.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(leads, key = { it.id }) { lead ->
                        LeadCard(
                            lead = lead,
                            onOpenWhatsApp = { onOpenWhatsApp(lead.phone, lead.name) },
                            onAiSuggest = { aiLeadTarget = lead },
                            onEdit = { editingLead = lead },
                            onDelete = { onDeleteLead(lead) },
                            onStageChange = { newStage -> onUpdateStage(lead.id, newStage) }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(60.dp))
                    }
                }
            }
        }
    }

    if (isCreatingNew) {
        EditLeadDialog(
            initialLead = null,
            onDismiss = { isCreatingNew = false },
            onSave = {
                onSaveLead(it)
                isCreatingNew = false
                scope.launch { snackbarHostState.showSnackbar("Prospecto añadido al CRM") }
            }
        )
    }

    editingLead?.let { lead ->
        EditLeadDialog(
            initialLead = lead,
            onDismiss = { editingLead = null },
            onSave = {
                onSaveLead(it)
                editingLead = null
                scope.launch { snackbarHostState.showSnackbar("Datos actualizados") }
            }
        )
    }

    aiLeadTarget?.let { targetLead ->
        com.example.ui.components.AiSalesCopilotBottomSheet(
            onDismissRequest = { aiLeadTarget = null },
            onSaveAsTemplate = { _, _, _ -> },
            onUseGeneratedText = { _ ->
                onOpenWhatsApp(targetLead.phone, targetLead.name)
                aiLeadTarget = null
            },
            initialClientName = targetLead.name,
            initialProductName = targetLead.productInterest,
            initialPrompt = if (targetLead.notes.isNotBlank()) {
                "Seguimiento comercial a ${targetLead.name}: ${targetLead.notes}"
            } else {
                "Seguimiento comercial en etapa ${targetLead.stage.label}"
            }
        )
    }
}

@Composable
fun PipelineMetricCard(
    title: String,
    value: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(CyberCard)
            .border(1.dp, CyberBorder, RoundedCornerShape(10.dp))
            .padding(vertical = 10.dp, horizontal = 10.dp)
    ) {
        Column {
            Text(
                text = title,
                fontFamily = FontFamily.Monospace,
                fontSize = 9.5.sp,
                color = TextMuted,
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = accentColor
            )
        }
    }
}

@Composable
fun LeadCard(
    lead: LeadEntity,
    onOpenWhatsApp: () -> Unit,
    onAiSuggest: () -> Unit = {},
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onStageChange: (LeadStage) -> Unit,
    modifier: Modifier = Modifier
) {
    var stageMenuExpanded by remember { mutableStateOf(false) }
    val stageColor = Color(lead.stage.colorHex)
    val dateFormat = remember { SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()) }

    CyberCard(
        modifier = modifier.testTag("lead_card_${lead.id}"),
        borderColor = stageColor.copy(alpha = 0.4f)
    ) {
        Column {
            // Header: Name and Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = lead.name,
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                    Text(
                        text = lead.phone,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        color = NeonCyan
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onAiSuggest,
                        modifier = Modifier
                            .size(34.dp)
                            .testTag("lead_ai_btn_${lead.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = tr("Redactar con IA", "Draft with AI"),
                            tint = NeonPurple,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = onOpenWhatsApp,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("lead_whatsapp_btn_${lead.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = tr("Abrir WhatsApp", "Open WhatsApp"),
                            tint = NeonGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = tr("Editar", "Edit"),
                            tint = TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = tr("Eliminar", "Delete"),
                            tint = Color(0xFFFF3366).copy(alpha = 0.7f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Stage selector button
            Box {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(stageColor.copy(alpha = 0.15f))
                        .border(1.dp, stageColor.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                        .clickable { stageMenuExpanded = true }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${tr("ETAPA", "STAGE")}: ${lead.stage.localizedLabel()}",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = stageColor
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = tr("Cambiar etapa", "Change stage"),
                        tint = stageColor,
                        modifier = Modifier.size(12.dp)
                    )
                }

                DropdownMenu(
                    expanded = stageMenuExpanded,
                    onDismissRequest = { stageMenuExpanded = false },
                    modifier = Modifier.background(CyberCardVariant)
                ) {
                    LeadStage.values().forEach { st ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = st.localizedLabel(),
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 12.sp,
                                    color = Color(st.colorHex)
                                )
                            },
                            onClick = {
                                stageMenuExpanded = false
                                onStageChange(st)
                            }
                        )
                    }
                }
            }

            // Product & Value
            if (lead.productInterest.isNotBlank() || lead.estimatedValue > 0) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (lead.productInterest.isNotBlank()) {
                        Text(
                            text = "📦 ${lead.productInterest}",
                            fontSize = 12.sp,
                            color = TextWhite
                        )
                    }
                    if (lead.estimatedValue > 0) {
                        Text(
                            text = "VALOR: \$${"%.2f".format(lead.estimatedValue)} USD",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = NeonGreen
                        )
                    }
                }
            }

            // Notes
            if (lead.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "📝 ${lead.notes}",
                    fontSize = 12.sp,
                    color = TextMuted,
                    maxLines = 2
                )
            }

            // Timestamp
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Último contacto: ${dateFormat.format(Date(lead.lastContactedAt))}",
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp,
                color = TextMuted.copy(alpha = 0.7f)
            )
        }
    }
}

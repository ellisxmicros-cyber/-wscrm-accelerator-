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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
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
import com.example.data.TemplateEntity
import com.example.ui.components.CyberCard
import com.example.ui.components.CyberFilterChip
import com.example.ui.components.CyberTag
import com.example.ui.components.CyberTextField
import com.example.ui.components.EditTemplateDialog
import com.example.ui.components.NeonOutlinedButton
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberCard
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplatesScreen(
    templates: List<TemplateEntity>,
    selectedCategory: String,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onCopyTemplate: (TemplateEntity) -> Unit,
    onSaveTemplate: (TemplateEntity) -> Unit,
    onDeleteTemplate: (TemplateEntity) -> Unit,
    onLaunchWhatsAppWithTemplate: (TemplateEntity) -> Unit,
    onToggleOverlay: () -> Unit,
    isOverlayActive: Boolean,
    userSession: com.example.ui.viewmodel.UserSession = com.example.ui.viewmodel.UserSession(),
    onLoginGoogle: () -> Unit = {},
    onLoginPhone: (String) -> Unit = {},
    onLogout: () -> Unit = {},
    allTemplates: List<TemplateEntity> = templates,
    leadsCount: Int = 0,
    onLaunchWhatsAppWithCustomText: (String, String?) -> Unit = { _, _ -> },
    onCopyCustomText: (String) -> Unit = {},
    interactions: List<com.example.data.CustomerInteractionEntity> = emptyList(),
    isVipUser: Boolean = false,
    onActivateVip: () -> Unit = {},
    onDeleteInteraction: (Long) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var editingTemplate by remember { mutableStateOf<TemplateEntity?>(null) }
    var customizingTemplate by remember { mutableStateOf<TemplateEntity?>(null) }
    var isCreatingNew by remember { mutableStateOf(false) }
    var isAuthSheetOpen by remember { mutableStateOf(false) }
    var isVipSheetOpen by remember { mutableStateOf(false) }
    var isHistorySheetOpen by remember { mutableStateOf(false) }
    var isTermsDialogOpen by remember { mutableStateOf(false) }

    val categories = listOf(
        "Todos" to "⚡ Todos",
        "Sales" to "💰 Ventas",
        "Support" to "🛠️ Soporte",
        "Follow-up" to "🔄 Seguimiento",
        "Precios" to "💲 Precios",
        "Pagos" to "💳 Pagos",
        "Info" to "📌 Info",
        "Saludos" to "👋 Saludos",
        "Envíos" to "📦 Envíos",
        "Cierre" to "🎯 Cierre"
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = CyberBlack,
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                androidx.compose.material3.Snackbar(
                    snackbarData = data,
                    containerColor = Color(0xFF00E5FF),
                    contentColor = Color.Black,
                    shape = RoundedCornerShape(8.dp)
                )
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "WS CRM // DASHBOARD",
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 2.sp,
                        color = NeonCyan,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    // 💎 Botón Planes VIP
                    IconButton(
                        onClick = { isVipSheetOpen = true },
                        modifier = Modifier.testTag("action_vip_plans")
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(if (isVipUser) Color(0xFFFFD700).copy(alpha = 0.2f) else Color(0xFF1E1E2A))
                                .border(
                                    1.dp,
                                    if (isVipUser) Color(0xFFFFD700) else Color(0xFF4A4A28),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Diamond,
                                contentDescription = "Planes VIP",
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    }

                    // 📋 Botón Historial de Interacciones
                    IconButton(
                        onClick = { isHistorySheetOpen = true },
                        modifier = Modifier.testTag("action_interaction_history")
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1E1E2A))
                                .border(1.dp, CyberBorder, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = "Historial de Clientes",
                                tint = NeonCyan,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // 👤 Avatar de Sesión / Auth
                    IconButton(
                        onClick = { isAuthSheetOpen = true },
                        modifier = Modifier.testTag("action_quick_auth")
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(if (userSession.isLoggedIn) NeonGreen.copy(alpha = 0.15f) else Color(0xFF1E1E2A))
                                .border(
                                    1.dp,
                                    if (userSession.isLoggedIn) NeonGreen else CyberBorder,
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (userSession.isLoggedIn) userSession.displayName.take(1).uppercase() else "👤",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = if (userSession.isLoggedIn) NeonGreen else TextMuted,
                                fontSize = 13.sp
                            )
                        }
                    }

                    // 📜 Botón Términos y Soporte Oficial
                    IconButton(
                        onClick = { isTermsDialogOpen = true },
                        modifier = Modifier.testTag("action_terms_services")
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF161624))
                                .border(1.dp, CyberBorder, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Términos y Soporte",
                                tint = Color(0xFF00E5FF),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    IconButton(
                        onClick = onToggleOverlay,
                        modifier = Modifier.testTag("action_toggle_overlay")
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(if (isOverlayActive) NeonGreen.copy(alpha = 0.2f) else Color.Transparent)
                                .border(
                                    1.dp,
                                    if (isOverlayActive) NeonGreen else CyberBorder,
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.FlashOn,
                                contentDescription = "Activar burbuja sobre WhatsApp",
                                tint = if (isOverlayActive) NeonGreen else TextMuted,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF12121A)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isCreatingNew = true },
                containerColor = NeonCyan,
                contentColor = Color(0xFF0A0A0E),
                shape = CircleShape,
                modifier = Modifier
                    .shadow(elevation = 12.dp, shape = CircleShape, spotColor = NeonCyan)
                    .testTag("add_template_fab")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar plantilla",
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
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            // 📊 HUD Metrics Row (Cyberpunk Dashboard)
            com.example.ui.components.DashboardHudMetrics(
                totalTemplates = allTemplates.size,
                totalUsages = allTemplates.sumOf { it.usageCount },
                activeLeads = leadsCount,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            // Search Bar
            CyberTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                label = "BUSCADOR EN TIEMPO REAL",
                placeholder = "BUSCAR PLANTILLA O ATAJO...",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = Color(0xFF00E5FF),
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { onSearchChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Limpiar",
                                tint = TextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                },
                testTag = "template_search_input"
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Category Chips with dynamic count badges
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { (catKey, catLabel) ->
                    val count = if (catKey == "Todos") allTemplates.size else allTemplates.count { t ->
                        when (catKey) {
                            "Sales" -> t.category.equals("Sales", true) || t.category.equals("Ventas", true) || t.category.equals("Precios", true) || t.category.equals("Promos", true)
                            "Support" -> t.category.equals("Support", true) || t.category.equals("Soporte", true) || t.category.equals("Info", true)
                            "Follow-up" -> t.category.equals("Follow-up", true) || t.category.equals("Seguimiento", true) || t.category.equals("Cierre", true)
                            else -> t.category.equals(catKey, true)
                        }
                    }
                    CyberFilterChip(
                        text = "$catLabel ($count)",
                        selected = selectedCategory == catKey,
                        onClick = { onCategoryChange(catKey) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Section Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PLANTILLAS RÁPIDAS (${templates.size})",
                    fontFamily = FontFamily.Monospace,
                    color = TextMuted,
                    fontSize = 11.sp,
                    letterSpacing = 1.sp
                )
                if (isOverlayActive) {
                    Text(
                        text = "● BURBUJA ACTIVA",
                        fontFamily = FontFamily.Monospace,
                        color = NeonGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (templates.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = CyberBorder,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No se encontraron plantillas",
                            fontFamily = FontFamily.Monospace,
                            color = TextMuted,
                            fontSize = 13.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(templates, key = { it.id }) { item ->
                        val currentItem = item
                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = { dismissValue ->
                                if (dismissValue == SwipeToDismissBoxValue.EndToStart || dismissValue == SwipeToDismissBoxValue.StartToEnd) {
                                    onDeleteTemplate(currentItem)
                                    scope.launch {
                                        val result = snackbarHostState.showSnackbar(
                                            message = "🗑️ Plantilla eliminada: ${currentItem.title}",
                                            actionLabel = "DESHACER",
                                            duration = SnackbarDuration.Short
                                        )
                                        if (result == SnackbarResult.ActionPerformed) {
                                            onSaveTemplate(currentItem)
                                        }
                                    }
                                    true
                                } else {
                                    false
                                }
                            }
                        )

                        SwipeToDismissBox(
                            state = dismissState,
                            backgroundContent = {
                                val direction = dismissState.dismissDirection
                                val isSwiping = dismissState.targetValue != SwipeToDismissBoxValue.Settled
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(if (isSwiping) Color(0xFFFF3366) else Color(0x33FF3366))
                                        .padding(horizontal = 20.dp),
                                    contentAlignment = if (direction == SwipeToDismissBoxValue.StartToEnd) Alignment.CenterStart else Alignment.CenterEnd
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Eliminar plantilla",
                                            tint = Color.White,
                                            modifier = Modifier.size(22.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "ELIMINAR",
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.5.sp,
                                            color = Color.White
                                        )
                                    }
                                }
                            },
                            modifier = Modifier.testTag("swipe_to_dismiss_${item.id}")
                        ) {
                            TemplateItemCard(
                                template = item,
                                onCopy = {
                                    onCopyTemplate(item)
                                    scope.launch {
                                        snackbarHostState.showSnackbar("⚡ ¡Plantilla copiada al portapapeles!")
                                    }
                                },
                                onSendWa = {
                                    val hasVariables = Regex("\\{([^}]+)\\}").containsMatchIn(item.content)
                                    if (hasVariables) {
                                        customizingTemplate = item
                                    } else {
                                        onLaunchWhatsAppWithTemplate(item)
                                    }
                                },
                                onEdit = { editingTemplate = item },
                                onDelete = {
                                    onDeleteTemplate(item)
                                    scope.launch {
                                        val result = snackbarHostState.showSnackbar(
                                            message = "🗑️ Plantilla eliminada",
                                            actionLabel = "DESHACER",
                                            duration = SnackbarDuration.Short
                                        )
                                        if (result == SnackbarResult.ActionPerformed) {
                                            onSaveTemplate(item)
                                        }
                                    }
                                }
                            )
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(60.dp))
                    }
                }
            }
        }
    }

    // Modal para variables dinámicas antes de enviar
    customizingTemplate?.let { template ->
        com.example.ui.components.DynamicVariablesDialog(
            template = template,
            onDismissRequest = { customizingTemplate = null },
            onSendWhatsApp = { personalizedText, phone ->
                onLaunchWhatsAppWithCustomText(personalizedText, phone)
                customizingTemplate = null
            },
            onCopyPersonalized = { personalizedText ->
                onCopyCustomText(personalizedText)
                customizingTemplate = null
                scope.launch { snackbarHostState.showSnackbar("⚡ ¡Plantilla personalizada copiada!") }
            }
        )
    }

    if (isCreatingNew) {
        com.example.ui.components.AddTemplateBottomSheet(
            initialTemplate = null,
            onDismissRequest = { isCreatingNew = false },
            onSaveTemplate = {
                onSaveTemplate(it)
                isCreatingNew = false
                scope.launch { snackbarHostState.showSnackbar("⚡ Plantilla guardada exitosamente") }
            }
        )
    }

    editingTemplate?.let { template ->
        com.example.ui.components.AddTemplateBottomSheet(
            initialTemplate = template,
            onDismissRequest = { editingTemplate = null },
            onSaveTemplate = { updated ->
                onSaveTemplate(updated)
                editingTemplate = null
                scope.launch { snackbarHostState.showSnackbar("⚡ Plantilla actualizada exitosamente") }
            }
        )
    }

    if (isAuthSheetOpen) {
        com.example.ui.components.QuickAccessAuthBottomSheet(
            userSession = userSession,
            onLoginGoogle = onLoginGoogle,
            onLoginPhone = onLoginPhone,
            onLogout = onLogout,
            onDismissRequest = { isAuthSheetOpen = false }
        )
    }

    if (isVipSheetOpen) {
        com.example.ui.components.VipPlansModalBottomSheet(
            isCurrentVip = isVipUser,
            userId = userSession.userId,
            onActivateVip = {
                onActivateVip()
                scope.launch { snackbarHostState.showSnackbar("⭐ ¡Membresía VIP Pro Activada con Éxito!") }
            },
            onDismissRequest = { isVipSheetOpen = false }
        )
    }

    if (isHistorySheetOpen) {
        com.example.ui.components.CustomerInteractionHistorySheet(
            interactions = interactions,
            onDeleteInteraction = onDeleteInteraction,
            onDirectWhatsApp = { phone, text ->
                onLaunchWhatsAppWithCustomText(text, phone)
                isHistorySheetOpen = false
            },
            onDismissRequest = { isHistorySheetOpen = false }
        )
    }

    if (isTermsDialogOpen) {
        com.example.ui.components.TermsAndServicesDialog(
            onDismissRequest = { isTermsDialogOpen = false }
        )
    }
}

@Composable
fun TemplateItemCard(
    template: TemplateEntity,
    onCopy: () -> Unit,
    onSendWa: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("template_card_${template.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = Color(0xFF12121A)
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (template.isPinned) NeonCyan.copy(alpha = 0.5f) else Color(0xFF2A2A3D)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row (Título + Botones de Acción)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    if (template.isPinned) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Fijada",
                            tint = NeonCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text(
                        text = template.title,
                        style = androidx.compose.ui.text.TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // 📋 Botón 1: Copiar al Portapapeles
                    IconButton(
                        onClick = onCopy,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("copy_button_${template.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copiar texto",
                            tint = Color(0xFF00E5FF),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // 🚀 Botón 2: Abrir e Inyectar en WhatsApp
                    IconButton(
                        onClick = onSendWa,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("send_wa_button_${template.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Enviar por WhatsApp",
                            tint = Color(0xFF39FF14),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint = TextMuted,
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = Color(0xFFFF3366).copy(alpha = 0.7f),
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }

            // Tags & Shortcut row
            Row(
                modifier = Modifier.padding(top = 4.dp, bottom = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CyberTag(text = template.category, color = NeonCyan)
                if (template.shortcut.isNotBlank()) {
                    CyberTag(text = template.shortcut, color = NeonPurple)
                }
                if (template.usageCount > 0) {
                    Text(
                        text = "⚡ ${template.usageCount} usos",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Contenido formateado con negritas y emojis
            Text(
                text = template.content,
                maxLines = 4,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                style = androidx.compose.ui.text.TextStyle(
                    color = Color.Gray,
                    fontSize = 13.sp,
                    lineHeight = 18.2.sp
                )
            )
        }
    }
}

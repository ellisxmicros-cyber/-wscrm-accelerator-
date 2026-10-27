package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.TableView
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LeadEntity
import com.example.data.TemplateEntity
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberCard
import com.example.ui.theme.CyberCardVariant
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.util.BackupRestoreHelper
import com.example.util.tr

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupRestoreModalBottomSheet(
    templates: List<TemplateEntity>,
    leads: List<LeadEntity>,
    onRestoreData: (List<TemplateEntity>, List<LeadEntity>) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var showImportDialog by remember { mutableStateOf(false) }
    var importJsonText by remember { mutableStateOf("") }
    var importErrorMessage by remember { mutableStateOf<String?>(null) }
    var successNotice by remember { mutableStateOf<String?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = Color(0xFF101018),
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
        modifier = modifier.testTag("backup_restore_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp, top = 4.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Storage,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = tr("COPIAS DE SEGURIDAD & EXPORTACIÓN", "BACKUP & DATA EXPORT"),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan,
                        letterSpacing = 1.sp
                    )
                }
                IconButton(
                    onClick = onDismissRequest,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = tr("Cerrar", "Close"),
                        tint = TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = tr(
                    "Protege tus prospectos, historial y plantillas. Exporta a JSON o CSV (Excel) para respaldo o para migrar de teléfono.",
                    "Protect your leads, history, and templates. Export to JSON or CSV (Excel) for backup or device migration."
                ),
                fontSize = 12.sp,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Stats box
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF161624))
                    .border(1.dp, CyberBorder, RoundedCornerShape(10.dp))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "${templates.size}", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = NeonCyan, fontSize = 18.sp)
                    Text(text = tr("Plantillas", "Templates"), fontFamily = FontFamily.Monospace, fontSize = 10.sp, color = TextMuted)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "${leads.size}", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = NeonGreen, fontSize = 18.sp)
                    Text(text = tr("Leads CRM", "CRM Leads"), fontFamily = FontFamily.Monospace, fontSize = 10.sp, color = TextMuted)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "\$${"%.0f".format(leads.sumOf { it.estimatedValue })}", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700), fontSize = 18.sp)
                    Text(text = tr("Valor USD", "USD Value"), fontFamily = FontFamily.Monospace, fontSize = 10.sp, color = TextMuted)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action 1: Export Complete JSON
            BackupActionCard(
                title = tr("Exportar Respaldo Completo (JSON)", "Export Complete Backup (JSON)"),
                subtitle = tr("Guarda todas las plantillas y clientes en un archivo universal.", "Saves all templates and leads in a universal backup file."),
                badge = tr("RECOMENDADO", "RECOMMENDED"),
                badgeColor = NeonCyan,
                icon = Icons.Default.CloudUpload,
                onClick = {
                    val json = BackupRestoreHelper.generateBackupJson(templates, leads)
                    BackupRestoreHelper.shareTextOrFile(
                        context = context,
                        textContent = json,
                        fileName = "TurboVentas_Backup_${System.currentTimeMillis()}.json",
                        mimeType = "application/json",
                        title = "Respaldo TurboVentas WS"
                    )
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Action 2: Export CRM to CSV (Excel)
            BackupActionCard(
                title = tr("Exportar Prospectos a Excel / CSV", "Export Leads to Excel / CSV"),
                subtitle = tr("Genera una hoja de cálculo con nombres, teléfonos, etapas y montos.", "Generates a spreadsheet with names, phones, stages and deal amounts."),
                badge = "EXCEL / CSV",
                badgeColor = NeonGreen,
                icon = Icons.Default.TableView,
                onClick = {
                    val csv = BackupRestoreHelper.generateCsvLeads(leads)
                    BackupRestoreHelper.shareTextOrFile(
                        context = context,
                        textContent = csv,
                        fileName = "Leads_CRM_TurboVentas_${System.currentTimeMillis()}.csv",
                        mimeType = "text/csv",
                        title = "Exportación CRM Leads"
                    )
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Action 3: Restore / Import Backup
            BackupActionCard(
                title = tr("Restaurar Copia de Seguridad", "Restore Backup File"),
                subtitle = tr("Pega tu archivo JSON de respaldo para recuperar tus datos de inmediato.", "Paste your backup JSON to recover your data immediately."),
                badge = tr("IMPORTAR", "IMPORT"),
                badgeColor = NeonPurple,
                icon = Icons.Default.CloudDownload,
                onClick = {
                    showImportDialog = true
                }
            )

            if (successNotice != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(NeonGreen.copy(alpha = 0.15f))
                        .border(1.dp, NeonGreen, RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    Text(
                        text = successNotice ?: "",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonGreen
                    )
                }
            }
        }
    }

    if (showImportDialog) {
        BasicAlertDialog(onDismissRequest = { showImportDialog = false }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.5.dp, NeonPurple, RoundedCornerShape(16.dp)),
                color = CyberCard
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = tr("📥 RESTAURAR DESDE JSON", "📥 RESTORE FROM JSON"),
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = NeonPurple
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = tr("Pega el código JSON de tu copia de seguridad anterior:", "Paste the JSON content of your previous backup:"),
                        fontSize = 11.5.sp,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = importJsonText,
                        onValueChange = {
                            importJsonText = it
                            importErrorMessage = null
                        },
                        label = { Text(tr("Contenido JSON de respaldo", "Backup JSON Content"), color = TextMuted, fontSize = 11.sp) },
                        maxLines = 8,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedBorderColor = NeonPurple,
                            unfocusedBorderColor = CyberBorder
                        )
                    )

                    if (importErrorMessage != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = importErrorMessage ?: "",
                            color = Color(0xFFFF3366),
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showImportDialog = false }) {
                            Text(text = tr("CANCELAR", "CANCEL"), color = TextMuted)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (importJsonText.isBlank()) {
                                    importErrorMessage = "Pega el contenido JSON primero"
                                    return@Button
                                }
                                try {
                                    val (restoredTemplates, restoredLeads) = BackupRestoreHelper.parseBackupJson(importJsonText)
                                    onRestoreData(restoredTemplates, restoredLeads)
                                    showImportDialog = false
                                    successNotice = "¡Datos restaurados con éxito! (${restoredTemplates.size} plantillas, ${restoredLeads.size} prospectos)"
                                } catch (e: Exception) {
                                    importErrorMessage = "Formato JSON no válido: ${e.localizedMessage}"
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonPurple, contentColor = Color.White)
                        ) {
                            Text(text = tr("RESTAURAR", "RESTORE"), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BackupActionCard(
    title: String,
    subtitle: String,
    badge: String,
    badgeColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF141420))
            .border(1.dp, Color(0xFF28283C), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(badgeColor.copy(alpha = 0.15f))
                    .border(1.dp, badgeColor.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = badgeColor, modifier = Modifier.size(20.dp))
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.5.sp,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(badgeColor.copy(alpha = 0.2f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = badge,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = badgeColor
                )
            }
        }
    }
}

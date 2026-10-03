package com.example.ui.screens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.ui.components.CyberCard
import com.example.ui.components.NeonButton
import com.example.ui.components.NeonOutlinedButton
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberCardVariant
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverlaySettingsScreen(
    isSystemOverlayRunning: Boolean,
    isInAppBubbleActive: Boolean,
    hasOverlayPermission: Boolean,
    isBatteryOptimized: Boolean,
    onToggleSystemOverlay: () -> Unit,
    onToggleInAppBubble: () -> Unit,
    onRequestPermission: () -> Unit,
    onRequestBatteryBypass: () -> Unit,
    onOpenAutostartSettings: () -> Unit,
    onLaunchWhatsApp: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = CyberBlack,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "BURBUJA FLOTANTE // CONFIG",
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
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Visual Bubble Graphic Card
            CyberCard(
                borderColor = if (isSystemOverlayRunning || isInAppBubbleActive) NeonGreen else CyberBorder
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(CyberBlack)
                            .border(
                                2.5.dp,
                                if (isSystemOverlayRunning || isInAppBubbleActive) NeonGreen else CyberBorder,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlashOn,
                            contentDescription = null,
                            tint = if (isSystemOverlayRunning || isInAppBubbleActive) NeonGreen else TextMuted,
                            modifier = Modifier.size(42.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (isSystemOverlayRunning || isInAppBubbleActive) "BURBUJA FLOTANTE ACTIVA" else "BURBUJA FLOTANTE INACTIVA",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = if (isSystemOverlayRunning || isInAppBubbleActive) NeonGreen else TextMuted,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Accede a tus plantillas de venta sin salir de WhatsApp",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Switch 1: Real Android System Overlay
            CyberCard {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Burbuja del Sistema (Android)",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Flota sobre WhatsApp, Telegram, Instagram y cualquier otra app.",
                                fontSize = 12.sp,
                                color = TextMuted
                            )
                        }

                        Switch(
                            checked = isSystemOverlayRunning,
                            onCheckedChange = { onToggleSystemOverlay() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color(0xFF0A0A0E),
                                checkedTrackColor = NeonGreen,
                                uncheckedThumbColor = TextMuted,
                                uncheckedTrackColor = CyberBorder
                            ),
                            modifier = Modifier.testTag("toggle_system_overlay_switch")
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Permission badge & action
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(CyberCardVariant)
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = if (hasOverlayPermission) Icons.Default.CheckCircle else Icons.Default.Warning,
                                contentDescription = null,
                                tint = if (hasOverlayPermission) NeonGreen else Color(0xFFFFE600),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (hasOverlayPermission) "Permiso 'Mostrar sobre apps' CONCEDIDO" else "Permiso de superposición requerido",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.5.sp,
                                color = if (hasOverlayPermission) NeonGreen else Color(0xFFFFE600)
                            )
                        }

                        if (!hasOverlayPermission) {
                            NeonOutlinedButton(
                                text = "AJUSTES",
                                color = Color(0xFFFFE600),
                                icon = Icons.Default.OpenInNew,
                                onClick = onRequestPermission,
                                testTag = "open_settings_permission_btn"
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Background Retention & Battery Optimization (requested)
            CyberCard {
                Column {
                    Text(
                        text = "PERMISOS DE EJECUCIÓN EN SEGUNDO PLANO",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan,
                        fontSize = 12.sp,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Evita que Android o el fabricante (Xiaomi, Samsung, etc.) cierren la burbuja cuando chateas en WhatsApp.",
                        fontSize = 12.sp,
                        color = TextMuted
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Battery Optimization Bypass Action
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(CyberCardVariant)
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.BatteryChargingFull,
                                    contentDescription = null,
                                    tint = if (isBatteryOptimized) NeonGreen else Color(0xFFFFE600),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Optimización de Batería",
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = TextWhite
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (isBatteryOptimized) "Sin restricciones (Activo)" else "Restringido por el sistema",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.5.sp,
                                color = if (isBatteryOptimized) NeonGreen else Color(0xFFFFE600)
                            )
                        }

                        NeonOutlinedButton(
                            text = if (isBatteryOptimized) "LISTO" else "IGNORAR",
                            color = if (isBatteryOptimized) NeonGreen else Color(0xFFFFE600),
                            onClick = onRequestBatteryBypass,
                            testTag = "request_battery_bypass_btn"
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // OEM Autostart Menu (Xiaomi / MIUI / Samsung / Oppo / Vivo)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(CyberCardVariant)
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.PowerSettingsNew,
                                    contentDescription = null,
                                    tint = NeonCyan,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Inicio Automático (OEM)",
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = TextWhite
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Xiaomi MIUI/HyperOS, Samsung, Oppo",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = TextMuted
                            )
                        }

                        NeonOutlinedButton(
                            text = "ABRIR",
                            color = NeonCyan,
                            icon = Icons.Default.OpenInNew,
                            onClick = onOpenAutostartSettings,
                            testTag = "open_autostart_menu_btn"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Switch 2: In-App Simulator
            CyberCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Simulador en Pantalla",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Prueba la burbuja arrastrable y el mini HUD interactivo directamente dentro de esta app.",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }

                    Switch(
                        checked = isInAppBubbleActive,
                        onCheckedChange = { onToggleInAppBubble() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFF0A0A0E),
                            checkedTrackColor = NeonCyan,
                            uncheckedThumbColor = TextMuted,
                            uncheckedTrackColor = CyberBorder
                        ),
                        modifier = Modifier.testTag("toggle_in_app_bubble_switch")
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Instructions Guide
            CyberCard(borderColor = CyberBorder) {
                Column {
                    Text(
                        text = "CÓMO FUNCIONA EL COMPANION DE VENTAS",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = NeonCyan,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    InstructionStepItem(
                        number = "1",
                        title = "Activa la burbuja flotante",
                        description = "Enciende el interruptor superior. Si es tu primera vez, concede el permiso de superposición en los ajustes de Android."
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    InstructionStepItem(
                        number = "2",
                        title = "Abre WhatsApp o WhatsApp Business",
                        description = "La burbuja de rayo ⚡ permanecerá flotando en el borde de tu pantalla sobre tus chats."
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    InstructionStepItem(
                        number = "3",
                        title = "Toca y copia en 1 segundo",
                        description = "Toca la burbuja para abrir el HUD, presiona '⚡ COPIAR' en la plantilla deseada y pégala instantáneamente a tu cliente."
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            NeonButton(
                text = "⚡ ABRIR WHATSAPP AHORA",
                color = NeonGreen,
                icon = Icons.Default.Send,
                onClick = onLaunchWhatsApp,
                modifier = Modifier.fillMaxWidth(),
                testTag = "open_whatsapp_companion_btn"
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun InstructionStepItem(
    number: String,
    title: String,
    description: String
) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(NeonCyan.copy(alpha = 0.15f))
                .border(1.dp, NeonCyan, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = NeonCyan
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = title,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = TextWhite
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                fontSize = 12.sp,
                color = TextMuted,
                lineHeight = 16.sp
            )
        }
    }
}

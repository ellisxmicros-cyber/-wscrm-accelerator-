package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Translate
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
import androidx.compose.runtime.getValue
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
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.util.tr

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverlaySettingsScreen(
    isSystemOverlayRunning: Boolean,
    isInAppBubbleActive: Boolean,
    hasOverlayPermission: Boolean,
    isBatteryOptimized: Boolean,
    isEnglish: Boolean = false,
    onSelectLanguage: (Boolean) -> Unit = {},
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
                        text = tr("TURBOVENTAS // AJUSTES & BURBUJA", "TURBOVENTAS // SETTINGS & BUBBLE"),
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
            // 🌐 SECTION 1: LANGUAGE SWITCHER (SETTINGS)
            CyberCard(
                borderColor = NeonCyan,
                modifier = Modifier.testTag("language_switcher_card")
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(NeonCyan.copy(alpha = 0.15f))
                                    .border(1.dp, NeonCyan, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Language,
                                    contentDescription = null,
                                    tint = NeonCyan,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = tr("IDIOMA DE LA APLICACIÓN", "APPLICATION LANGUAGE"),
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = NeonCyan,
                                    fontSize = 13.sp,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = tr(
                                        "Alterna toda la interfaz entre Español e Inglés",
                                        "Toggle entire UI between Spanish and English"
                                    ),
                                    fontSize = 11.5.sp,
                                    color = TextMuted
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Segmented Language Options (ES / EN)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Option 1: Español
                        val esBorderColor by animateColorAsState(
                            targetValue = if (!isEnglish) NeonGreen else CyberBorder,
                            label = "esBorder"
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (!isEnglish) NeonGreen.copy(alpha = 0.12f) else CyberCardVariant)
                                .border(1.5.dp, esBorderColor, RoundedCornerShape(10.dp))
                                .clickable { onSelectLanguage(false) }
                                .padding(vertical = 12.dp, horizontal = 10.dp)
                                .testTag("language_option_es"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "🇪🇸 Español",
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = if (!isEnglish) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 13.sp,
                                    color = if (!isEnglish) NeonGreen else TextMuted
                                )
                                if (!isEnglish) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = NeonGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }

                        // Option 2: English
                        val enBorderColor by animateColorAsState(
                            targetValue = if (isEnglish) NeonCyan else CyberBorder,
                            label = "enBorder"
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isEnglish) NeonCyan.copy(alpha = 0.12f) else CyberCardVariant)
                                .border(1.5.dp, enBorderColor, RoundedCornerShape(10.dp))
                                .clickable { onSelectLanguage(true) }
                                .padding(vertical = 12.dp, horizontal = 10.dp)
                                .testTag("language_option_en"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "🇺🇸 English",
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = if (isEnglish) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 13.sp,
                                    color = if (isEnglish) NeonCyan else TextMuted
                                )
                                if (isEnglish) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = NeonCyan,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Status Indicator
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0F0F18))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (isEnglish)
                                "🌐 SELECTED: ENGLISH (US/GLOBAL)"
                            else
                                "🌐 SELECCIONADO: ESPAÑOL (ES/LATAM)",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isEnglish) NeonCyan else NeonGreen
                        )
                        Switch(
                            checked = isEnglish,
                            onCheckedChange = { onSelectLanguage(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color(0xFF0A0A0E),
                                checkedTrackColor = NeonCyan,
                                uncheckedThumbColor = Color(0xFF0A0A0E),
                                uncheckedTrackColor = NeonGreen
                            ),
                            modifier = Modifier.testTag("language_quick_switch")
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

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
                        text = if (isSystemOverlayRunning || isInAppBubbleActive)
                            tr("BURBUJA FLOTANTE ACTIVA", "FLOATING BUBBLE ACTIVE")
                        else
                            tr("BURBUJA FLOTANTE INACTIVA", "FLOATING BUBBLE INACTIVE"),
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = if (isSystemOverlayRunning || isInAppBubbleActive) NeonGreen else TextMuted,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = tr(
                            "Accede a tus plantillas de venta sin salir de WhatsApp",
                            "Access your sales templates without leaving WhatsApp"
                        ),
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
                                text = tr("Burbuja del Sistema (Android)", "System Overlay Bubble (Android)"),
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = tr(
                                    "Flota sobre WhatsApp, Telegram, Instagram y cualquier otra app.",
                                    "Floats on top of WhatsApp, Telegram, Instagram and any other app."
                                ),
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
                                text = if (hasOverlayPermission)
                                    tr("Permiso 'Mostrar sobre apps' CONCEDIDO", "'Display over other apps' GRANTED")
                                else
                                    tr("Permiso de superposición requerido", "Overlay permission required"),
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.5.sp,
                                color = if (hasOverlayPermission) NeonGreen else Color(0xFFFFE600)
                            )
                        }

                        if (!hasOverlayPermission) {
                            NeonOutlinedButton(
                                text = tr("AJUSTES", "SETTINGS"),
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
                        text = tr("PERMISOS DE EJECUCIÓN EN SEGUNDO PLANO", "BACKGROUND EXECUTION PERMISSIONS"),
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan,
                        fontSize = 12.sp,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = tr(
                            "Evita que Android o el fabricante (Xiaomi, Samsung, etc.) cierren la burbuja cuando chateas en WhatsApp.",
                            "Prevents Android or OEM (Xiaomi, Samsung, etc.) from closing the bubble while chatting in WhatsApp."
                        ),
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
                                    text = tr("Optimización de Batería", "Battery Optimization"),
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = TextWhite
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (isBatteryOptimized)
                                    tr("Sin restricciones (Activo)", "Unrestricted (Active)")
                                else
                                    tr("Restringido por el sistema", "Restricted by system"),
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.5.sp,
                                color = if (isBatteryOptimized) NeonGreen else Color(0xFFFFE600)
                            )
                        }

                        NeonOutlinedButton(
                            text = if (isBatteryOptimized) tr("LISTO", "READY") else tr("IGNORAR", "BYPASS"),
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
                                    text = tr("Inicio Automático (OEM)", "Autostart Settings (OEM)"),
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
                            text = tr("ABRIR", "OPEN"),
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
                            text = tr("Simulador en Pantalla", "In-App Bubble Simulator"),
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = tr(
                                "Prueba la burbuja arrastrable y el mini HUD interactivo directamente dentro de esta app.",
                                "Test the draggable bubble and mini interactive HUD directly inside this app."
                            ),
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
                        text = tr("CÓMO FUNCIONA EL COMPANION DE VENTAS", "HOW THE SALES COMPANION WORKS"),
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = NeonCyan,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    InstructionStepItem(
                        number = "1",
                        title = tr("Activa la burbuja flotante", "Turn on the floating bubble"),
                        description = tr(
                            "Enciende el interruptor superior. Si es tu primera vez, concede el permiso de superposición en los ajustes de Android.",
                            "Turn on the switch above. If it's your first time, grant the overlay permission in Android settings."
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    InstructionStepItem(
                        number = "2",
                        title = tr("Abre WhatsApp o WhatsApp Business", "Open WhatsApp or WhatsApp Business"),
                        description = tr(
                            "La burbuja de rayo ⚡ permanecerá flotando en el borde de tu pantalla sobre tus chats.",
                            "The lightning ⚡ bubble will float on your screen edge over your chat conversations."
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    InstructionStepItem(
                        number = "3",
                        title = tr("Toca y copia en 1 segundo", "Tap and copy in 1 second"),
                        description = tr(
                            "Toca la burbuja para abrir el HUD, presiona '⚡ COPIAR' en la plantilla deseada y pégala instantáneamente a tu cliente.",
                            "Tap the bubble to open HUD, click '⚡ COPY' on your chosen template, and paste it to your client instantly."
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            NeonButton(
                text = tr("⚡ ABRIR WHATSAPP AHORA", "⚡ OPEN WHATSAPP NOW"),
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

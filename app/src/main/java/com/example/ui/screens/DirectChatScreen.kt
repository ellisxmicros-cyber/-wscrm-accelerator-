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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LeadEntity
import com.example.data.TemplateEntity
import com.example.ui.components.CyberCard
import com.example.ui.components.CyberTextField
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectChatScreen(
    countryCode: String,
    phoneNumber: String,
    messageText: String,
    templates: List<TemplateEntity>,
    onCountryCodeChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onMessageChange: (String) -> Unit,
    onApplyTemplate: (TemplateEntity, String, String) -> Unit,
    onLaunchWhatsApp: (phoneWithCountry: String, message: String) -> Unit,
    onSaveAsLead: (LeadEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val isEnglish = com.example.util.LocalIsEnglish.current
    var clientName by remember { mutableStateOf("") }
    var productName by remember { mutableStateOf("") }
    var showCountryMenu by remember { mutableStateOf(false) }
    var isAiCopilotOpen by remember { mutableStateOf(false) }

    val countryCodes = listOf(
        "+58" to "Venezuela",
        "+57" to "Colombia",
        "+52" to "México",
        "+1" to "USA / Canada",
        "+44" to "UK",
        "+34" to "España",
        "+54" to "Argentina",
        "+56" to "Chile",
        "+51" to "Perú",
        "+593" to "Ecuador",
        "+507" to "Panamá"
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = CyberBlack,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = tr("TURBOVENTAS // CHAT DIRECTO", "TURBOVENTAS // DIRECT CHAT"),
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 2.sp,
                        color = NeonCyan,
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
            Text(
                text = tr("INICIAR CONVERSACIÓN SIN GUARDAR CONTACTO", "START WHATSAPP CHAT WITHOUT SAVING CONTACT"),
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                color = TextMuted,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Phone input with country code selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Country Code Button
                Box {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(CyberCardVariant)
                            .border(1.dp, CyberBorder, RoundedCornerShape(10.dp))
                            .clickable { showCountryMenu = true }
                            .padding(horizontal = 12.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = countryCode,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan,
                            fontSize = 14.sp
                        )
                    }

                    DropdownMenu(
                        expanded = showCountryMenu,
                        onDismissRequest = { showCountryMenu = false },
                        modifier = Modifier.background(CyberCardVariant)
                    ) {
                        countryCodes.forEach { (code, name) ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "$code ($name)",
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 12.sp,
                                        color = TextWhite
                                    )
                                },
                                onClick = {
                                    onCountryCodeChange(code)
                                    showCountryMenu = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Phone number
                CyberTextField(
                    value = phoneNumber,
                    onValueChange = onPhoneNumberChange,
                    label = tr("NÚMERO DE TELÉFONO", "PHONE NUMBER"),
                    placeholder = tr("ej. 4121234567", "e.g. 3051234567"),
                    modifier = Modifier.weight(1f),
                    testTag = "direct_phone_input"
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Dynamic tags fields (Nombre & Producto)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CyberTextField(
                    value = clientName,
                    onValueChange = { clientName = it },
                    label = tr("NOMBRE CLIENTE", "CLIENT NAME"),
                    placeholder = tr("ej. María", "e.g. Sarah"),
                    modifier = Modifier.weight(1f),
                    testTag = "direct_client_name"
                )
                CyberTextField(
                    value = productName,
                    onValueChange = { productName = it },
                    label = tr("PRODUCTO", "PRODUCT"),
                    placeholder = tr("ej. Opción Pro", "e.g. Pro Plan"),
                    modifier = Modifier.weight(1f),
                    testTag = "direct_product_name"
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Quick Template Picker
            Text(
                text = tr("SELECCIONAR PLANTILLA RÁPIDA", "SELECT QUICK TEMPLATE"),
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                color = TextMuted,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(templates) { template ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(CyberCardVariant)
                            .border(1.dp, CyberBorder, RoundedCornerShape(8.dp))
                            .clickable {
                                onApplyTemplate(template, clientName, productName)
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        if (isEnglish) "Template '${template.title}' applied" else "Plantilla '${template.title}' aplicada"
                                    )
                                }
                            }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = template.title,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.5.sp,
                            color = NeonCyan
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Message Header with AI button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = tr("MENSAJE DE WHATSAPP", "WHATSAPP MESSAGE"),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = TextMuted,
                    letterSpacing = 1.sp
                )
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF261238))
                        .border(1.dp, NeonPurple, RoundedCornerShape(6.dp))
                        .clickable { isAiCopilotOpen = true }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "IA",
                        tint = NeonPurple,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = tr("⚡ REDACTAR CON IA", "⚡ DRAFT WITH AI"),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonPurple
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Message Body
            CyberTextField(
                value = messageText,
                onValueChange = onMessageChange,
                label = tr("TEXTO DEL MENSAJE", "MESSAGE TEXT"),
                placeholder = tr("Escribe o selecciona una plantilla para enviar...", "Type or select a template to send..."),
                singleLine = false,
                maxLines = 8,
                testTag = "direct_message_input"
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Main Action Button
            NeonButton(
                text = tr("⚡ INICIAR CHAT EN WHATSAPP", "⚡ START WHATSAPP CHAT"),
                color = NeonGreen,
                icon = Icons.Default.Send,
                onClick = {
                    val fullPhone = if (phoneNumber.startsWith("+")) {
                        phoneNumber
                    } else {
                        "$countryCode$phoneNumber"
                    }
                    if (phoneNumber.isBlank()) {
                        scope.launch { snackbarHostState.showSnackbar("Por favor ingresa un número de teléfono / Please enter a phone number") }
                        return@NeonButton
                    }
                    onLaunchWhatsApp(fullPhone, messageText)
                },
                modifier = Modifier.fillMaxWidth(),
                testTag = "launch_direct_whatsapp_btn"
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Secondary Action: Save to CRM
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NeonOutlinedButton(
                    text = tr("GUARDAR EN CRM", "SAVE TO CRM"),
                    color = NeonCyan,
                    icon = Icons.Default.PersonAdd,
                    onClick = {
                        val fullPhone = if (phoneNumber.startsWith("+")) {
                            phoneNumber
                        } else {
                            "$countryCode$phoneNumber"
                        }
                        if (phoneNumber.isBlank()) {
                            scope.launch { snackbarHostState.showSnackbar("Ingresa un número antes de guardar") }
                            return@NeonOutlinedButton
                        }
                        val leadName = if (clientName.isNotBlank()) clientName else "Lead $fullPhone"
                        onSaveAsLead(
                            LeadEntity(
                                name = leadName,
                                phone = fullPhone,
                                productInterest = productName,
                                notes = if (isEnglish) "Started from Direct Chat" else "Iniciado desde Chat Directo"
                            )
                        )
                        val msgLeadSaved = if (isEnglish) "Lead saved to CRM" else "Lead guardado en el CRM"
                        scope.launch { snackbarHostState.showSnackbar(msgLeadSaved) }
                    },
                    modifier = Modifier.weight(1f),
                    testTag = "save_lead_from_direct_btn"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Preview Box
            CyberCard(
                borderColor = CyberBorder
            ) {
                Column {
                    Text(
                        text = tr("VISTA PREVIA DE ENLACE", "LINK PREVIEW"),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    val fullPhone = if (phoneNumber.startsWith("+")) phoneNumber else "$countryCode$phoneNumber"
                    val cleanDigits = fullPhone.replace(Regex("[^0-9]"), "")
                    Text(
                        text = "wa.me/$cleanDigits",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = NeonGreen
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = tr(
                            "Abre directamente WhatsApp o WhatsApp Business sin necesidad de agregar el número a la agenda.",
                            "Opens WhatsApp or WhatsApp Business directly without saving the phone number to your contacts."
                        ),
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }
            }
        }
    }

    if (isAiCopilotOpen) {
        com.example.ui.components.AiSalesCopilotBottomSheet(
            onDismissRequest = { isAiCopilotOpen = false },
            onSaveAsTemplate = { _, _, _ -> },
            onUseGeneratedText = { generatedText ->
                onMessageChange(generatedText)
                scope.launch {
                    snackbarHostState.showSnackbar(
                        if (isEnglish) "✨ Message drafted with AI" else "✨ Mensaje redactado con IA"
                    )
                }
            },
            initialClientName = clientName,
            initialProductName = productName
        )
    }
}

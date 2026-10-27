package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.util.tr
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberCardVariant
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.util.tr

/**
 * Pantalla completa de Términos y Servicios (Terms and Services Screen)
 * Presenta el texto legal, las políticas de suscripción, privacidad y
 * el correo oficial de atención al cliente: ellisxmicros@gmail.com
 * con diseño limpio, responsivo y scrolleable.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsAndServicesScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val supportEmail = "ellisxmicros@gmail.com"
    val scrollState = rememberScrollState()

    BackHandler {
        onNavigateBack()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = CyberBlack,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = tr("TÉRMINOS Y SERVICIOS", "TERMS & SERVICES"),
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 1.sp,
                            color = NeonCyan,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = tr("WS CRM • Políticas y Soporte Oficial", "WS CRM • Official Policies & Support"),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("btn_back_terms")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = tr("Volver atrás", "Go back"),
                            tint = NeonCyan
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:$supportEmail")
                                putExtra(Intent.EXTRA_SUBJECT, "Consulta Términos / Soporte - WS CRM")
                            }
                            try {
                                context.startActivity(intent)
                            } catch (_: Exception) {
                                Toast.makeText(context, "No se encontró cliente de correo", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.testTag("btn_email_support_header")
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
                                imageVector = Icons.Default.Email,
                                contentDescription = tr("Escribir a Soporte", "Write to Support"),
                                tint = NeonGreen,
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF101018)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .testTag("terms_services_scrollable_container"),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 700.dp)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header Hero Banner
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF12121E)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2A2A44))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(NeonCyan.copy(alpha = 0.15f))
                                    .border(1.dp, NeonCyan, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VerifiedUser,
                                    contentDescription = null,
                                    tint = NeonCyan,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = tr("CONTRATO DE LICENCIA Y SERVICIO", "LICENSE & SERVICE AGREEMENT"),
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.5.sp,
                                    color = TextWhite
                                )
                                Text(
                                    text = tr("Válido y Actualizado • Año 2026", "Valid & Updated • Year 2026"),
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    color = NeonGreen
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = tr(
                                "Por favor lee atentamente estos términos antes de utilizar WS CRM. El uso de la aplicación y la contratación de planes VIP implica la aceptación plena de estas condiciones.",
                                "Please read these terms carefully before using WS CRM. Using the app and purchasing VIP plans implies full acceptance of these conditions."
                            ),
                            fontSize = 12.5.sp,
                            lineHeight = 18.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                // TARJETA DESTACADA: ATENCIÓN AL CLIENTE Y SOPORTE OFICIAL
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("support_email_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0B1720)),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF00E5FF))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = null,
                                    tint = Color(0xFF00E5FF),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = tr("CANAL OFICIAL DE ATENCIÓN", "OFFICIAL SUPPORT CHANNEL"),
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = Color(0xFF00E5FF)
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = NeonGreen.copy(alpha = 0.15f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, NeonGreen)
                            ) {
                                Text(
                                    text = tr("SOPORTE 24/7", "24/7 SUPPORT"),
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeonGreen,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = tr(
                                "Para consultas sobre facturación digital, activaciones empresariales, dudas técnicas o cancelaciones, escribe directamente a nuestro correo oficial:",
                                "For inquiries regarding digital billing, business activations, technical doubts, or cancellations, contact our official support email directly:"
                            ),
                            fontSize = 12.5.sp,
                            lineHeight = 17.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Caja destacada del correo
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF070E14))
                                .border(1.dp, Color(0xFF1E3A4D), RoundedCornerShape(10.dp))
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Email,
                                        contentDescription = null,
                                        tint = NeonGreen,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = supportEmail,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.5.sp,
                                        color = Color.White
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString(supportEmail))
                                        Toast.makeText(context, "📋 Correo copiado: $supportEmail", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = tr("Copiar correo", "Copy email"),
                                        tint = Color(0xFF00E5FF),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Botones de acción directa
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                                        data = Uri.parse("mailto:$supportEmail")
                                        putExtra(Intent.EXTRA_SUBJECT, "Contacto Soporte Oficial - WS CRM")
                                    }
                                    try {
                                        context.startActivity(intent)
                                    } catch (_: Exception) {
                                        Toast.makeText(context, "No se encontró aplicación de correo", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(42.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF00E5FF),
                                    contentColor = Color.Black
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = tr("ENVIAR MENSAJE", "SEND MESSAGE"),
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.5.sp,
                                    color = Color.Black
                                )
                            }

                            OutlinedButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(supportEmail))
                                    Toast.makeText(context, "¡Correo copiado al portapapeles!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .weight(0.9f)
                                    .height(42.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color(0xFF00E5FF)
                                ),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF00E5FF))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = null,
                                    tint = Color(0xFF00E5FF),
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = tr("COPIAR", "COPY"),
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.5.sp
                                )
                            }
                        }
                    }
                }

                // SECCIÓN 1: LICENCIA DE USO
                TermsSectionCard(
                    icon = Icons.Default.Policy,
                    title = tr("1. Licencia de Software y Alcance", "1. Software License & Scope"),
                    accentColor = Color(0xFF00E5FF),
                    content = tr(
                        "WS CRM concede al usuario una licencia no exclusiva, personal e intransferible para la gestión de respuestas rápidas, CRM de leads y aceleración de ventas a través de aplicaciones de mensajería compatibles como WhatsApp y WhatsApp Business.\n\nEl software se provee como herramienta de productividad. El usuario conserva la total propiedad intelectual de sus plantillas, mensajes comerciales y bases de datos generadas.",
                        "WS CRM grants the user a non-exclusive, personal, and non-transferable license for quick response management, lead CRM, and sales acceleration through compatible messaging apps like WhatsApp and WhatsApp Business.\n\nThe software is provided as a productivity tool. The user retains full intellectual property ownership of their templates, sales messages, and generated databases."
                    )
                )

                // SECCIÓN 2: SUSCRIPCIONES Y FACTURACIÓN
                TermsSectionCard(
                    icon = Icons.Default.Refresh,
                    title = tr("2. Suscripciones y Facturación Automatizada", "2. Subscriptions & Automated Billing"),
                    accentColor = Color(0xFFFFD000),
                    content = tr(
                        "Los planes VIP (Mensual o Anual) otorgan acceso completo a plantillas ilimitadas, analíticas de conversión, respaldo avanzado y funciones de automatización.\n\n• Cobros Transparentes: Los pagos procesados mediante pasarelas automatizadas (Lemon Squeezy, Stripe, PayPal) emiten la factura digital correspondiente directamente al correo electrónico registrado.\n• Renovación y Control: La suscripción recurrente puede cancelarse en cualquier momento antes de la fecha de corte desde el enlace de gestión enviado a tu correo o escribiendo a ellisxmicros@gmail.com.",
                        "VIP plans (Monthly or Annual) grant full access to unlimited templates, conversion analytics, advanced backup, and automation features.\n\n• Transparent Billing: Payments processed through automated gateways (Lemon Squeezy, Stripe, PayPal) issue digital invoices directly to the registered email.\n• Renewal & Control: Recurring subscriptions can be canceled at any time before the billing cycle date from the management link or by contacting ellisxmicros@gmail.com."
                    )
                )

                // SECCIÓN 3: POLÍTICA DE REEMBOLSOS
                TermsSectionCard(
                    icon = Icons.Default.HelpOutline,
                    title = tr("3. Garantía y Política de Reembolso", "3. Warranty & Refund Policy"),
                    accentColor = Color(0xFF39FF14),
                    content = tr(
                        "Ofrecemos una garantía técnica de satisfacción de 48 horas. Si experimentas un fallo técnico comprobable atribuible a la aplicación que no pueda ser resuelto por nuestro equipo técnico, tienes derecho a solicitar la revisión y reembolso del importe pagado enviando tu comprobante o correo de facturación a ellisxmicros@gmail.com.",
                        "We provide a 48-hour satisfaction warranty. If you experience a verifiable technical issue attributable to the app that cannot be resolved by our technical team, you have the right to request a refund by submitting your invoice receipt to ellisxmicros@gmail.com."
                    )
                )

                // SECCIÓN 4: PRIVACIDAD Y DATOS
                TermsSectionCard(
                    icon = Icons.Default.Security,
                    title = tr("4. Privacidad y Soberanía de Datos", "4. Privacy & Data Sovereignty"),
                    accentColor = NeonCyan,
                    content = tr(
                        "Nos regimos por una estricta política de Privacidad por Diseño (Privacy by Design):\n\n• Tus conversaciones, números de clientes, historial de ventas y textos de plantillas se guardan exclusivamente en el almacenamiento interno de tu dispositivo móvil mediante base de datos SQLite cifrada (Room).\n• Nosotros NO almacenamos, leemos ni comercializamos tus listas de contactos ni el contenido de tus mensajes comerciales con ningún tercero.",
                        "We adhere to a strict Privacy by Design policy:\n\n• Your conversations, customer numbers, sales history, and template texts are stored exclusively in your local mobile device storage using an encrypted SQLite database (Room).\n• We do NOT store, read, or sell your contact lists or commercial message content to any third parties."
                    )
                )

                // SECCIÓN 5: USO RESPONSABLE
                TermsSectionCard(
                    icon = Icons.Default.CheckCircle,
                    title = tr("5. Uso Responsable y Antispam", "5. Responsible Use & Anti-Spam"),
                    accentColor = Color(0xFFFF7043),
                    content = tr(
                        "El usuario se compromete a no utilizar la herramienta para el envío de comunicaciones no solicitadas (SPAM), esquemas fraudulentos ni actividades que vulneren las políticas de uso de Meta / WhatsApp o las normativas locales de protección al consumidor y telecomunicaciones.\n\nCualquier conducta que atente contra estos principios exime a los desarrolladores de toda responsabilidad legal.",
                        "The user agrees not to use the tool for sending unsolicited communications (SPAM), fraudulent schemes, or activities that violate Meta / WhatsApp policies or local telecommunications and consumer protection regulations.\n\nAny conduct violating these principles frees developers from all legal liability."
                    )
                )

                // Footer de Aceptación
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF14141F))
                        .border(1.dp, Color(0xFF2E2E42), RoundedCornerShape(12.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = tr("Acelerador WS CRM // Enterprise Edition", "WS CRM Accelerator // Enterprise Edition"),
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = NeonCyan
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${tr("Atención al cliente", "Customer Support")}: ellisxmicros@gmail.com",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun TermsSectionCard(
    icon: ImageVector,
    title: String,
    accentColor: Color,
    content: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF12121A)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF262638))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = accentColor
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = content,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                color = Color.White.copy(alpha = 0.82f)
            )
        }
    }
}

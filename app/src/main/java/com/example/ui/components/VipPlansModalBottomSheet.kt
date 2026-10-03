package com.example.ui.components

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.TextMuted

/**
 * Cyberpunk VIP Plans Modal Bottom Sheet.
 * Explains and displays the VIP membership options, features, pricing, and 1-tap activation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VipPlansModalBottomSheet(
    isCurrentVip: Boolean,
    onActivateVip: () -> Unit,
    onDismissRequest: () -> Unit,
    userId: String = "usr_agente_001",
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    var isYearly by remember { mutableStateOf(true) }
    val goldColor = Color(0xFFFFD700)

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
        modifier = modifier.testTag("vip_plans_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp, top = 4.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(goldColor.copy(alpha = 0.15f))
                    .border(1.dp, goldColor.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Diamond,
                    contentDescription = null,
                    tint = goldColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "MEMBRESÍA VIP // POTENCIA TU WHATSAPP",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = goldColor,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Desbloquea el Potencial Ilimitado",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.White
            )

            Text(
                text = "Automatiza tus ventas y ahorra hasta 3 horas al día respondiendo a clientes",
                fontSize = 12.5.sp,
                color = TextMuted,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            // Billing Period Toggle (Mensual vs Anual con Descuento)
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF161622))
                    .border(1.dp, Color(0xFF2A2A3D), RoundedCornerShape(24.dp))
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (!isYearly) NeonCyan else Color.Transparent)
                        .clickable { isYearly = false }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Mensual",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.5.sp,
                        color = if (!isYearly) Color.Black else TextMuted
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isYearly) goldColor else Color.Transparent)
                        .clickable { isYearly = true }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Anual (Ahorra 35%)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.5.sp,
                            color = if (isYearly) Color.Black else TextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ==========================================
            // SECCIÓN: ACTIVAR LICENCIA PRO ⚡ (MÉTODOS DE PAGO)
            // ==========================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF141420))
                    .border(1.5.dp, goldColor.copy(alpha = 0.8f), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlashOn,
                            contentDescription = null,
                            tint = goldColor,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "ACTIVAR LICENCIA PRO ⚡",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Black,
                            fontSize = 17.sp,
                            color = goldColor,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (isYearly) "$39.99 / año (Ahorras 35%)" else "$4.99 / mes",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White
                    )

                    Text(
                        text = "Selecciona tu método de pago preferido para activar:",
                        fontSize = 11.5.sp,
                        color = TextMuted,
                        modifier = Modifier.padding(top = 2.dp, bottom = 14.dp)
                    )

                    var selectedPaymentMethod by remember { mutableStateOf<String?>(null) }
                    var copiedNotice by remember { mutableStateOf<String?>(null) }
                    var activePaymentModal by remember { mutableStateOf<SelectedPaymentConfig?>(null) }

                    val clipboardManager = androidx.compose.ui.platform.LocalClipboardManager.current
                    val context = androidx.compose.ui.platform.LocalContext.current
                    val activePrice = if (isYearly) "$39.99 USD" else "$4.99 USD"

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        // 1. 🍋 Lemon Squeezy / Suscripción Recurrente Automática (RECOMENDADO)
                        PaymentMethodCard(
                            emoji = "🍋",
                            title = "Suscripción Automática",
                            subtitle = "Tarjeta de Crédito/Débito • Factura Automática",
                            isSelected = selectedPaymentMethod == "lemonsqueezy",
                            badgeText = "AUTOMÁTICO ⚡",
                            accentColor = Color(0xFFFFD000),
                            onClick = {
                                selectedPaymentMethod = if (selectedPaymentMethod == "lemonsqueezy") null else "lemonsqueezy"
                            },
                            expandedContent = {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF0F0F16))
                                        .border(1.dp, Color(0xFF262636), RoundedCornerShape(8.dp))
                                        .padding(12.dp)
                                ) {
                                    Text(
                                        text = "PASARELA 100% AUTOMATIZADA",
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 10.sp,
                                        color = Color(0xFFFFD000)
                                    )
                                    Text(
                                        text = "El cobro se procesa en línea con Tarjeta Visa/Mastercard o Google Pay. La pasarela emite la factura legal a tu correo y activa la licencia sin esperas.",
                                        fontSize = 11.5.sp,
                                        color = TextMuted,
                                        modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
                                    )
                                    Button(
                                        onClick = {
                                            com.example.util.SubscriptionCheckoutHelper.abrirCheckoutSuscripcion(
                                                context = context,
                                                userId = userId,
                                                storeSlug = "tuapp",
                                                productId = if (isYearly) "pro-anual" else "pro-mensual"
                                            )
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(44.dp),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFFFFD000),
                                            contentColor = Color.Black
                                        )
                                    ) {
                                        Text(
                                            text = "🚀 ABRIR CHECKOUT SEGURO ($activePrice)",
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = Color.Black
                                        )
                                    }
                                }
                            }
                        )

                        // 2. 🅿️ PayPal / Tarjeta Internacional
                        PaymentMethodCard(
                            emoji = "🅿️",
                            title = "PayPal Checkout",
                            subtitle = "Pago Seguro y Facturación Directa",
                            isSelected = selectedPaymentMethod == "paypal",
                            badgeText = "GLOBAL",
                            accentColor = Color(0xFF0079C1),
                            onClick = {
                                selectedPaymentMethod = if (selectedPaymentMethod == "paypal") null else "paypal"
                            },
                            expandedContent = {
                                PaymentDetailExpanded(
                                    dataTitle = "ENLACE DIRECTO PAYPAL",
                                    dataValue = "paypal.me/wscrm",
                                    alias = "Cuenta Oficial: pagos.paypal@wscrm.app",
                                    amount = activePrice,
                                    accentColor = Color(0xFF0079C1),
                                    onCopy = {
                                        clipboardManager.setText(androidx.compose.ui.text.AnnotatedString("https://paypal.me/wscrm"))
                                        copiedNotice = "¡Enlace PayPal copiado!"
                                    },
                                    onOpenReferenceModal = {
                                        activePaymentModal = SelectedPaymentConfig(
                                            metodo = "PayPal",
                                            datosPago = "https://paypal.me/wscrm",
                                            colorNeon = Color(0xFF0079C1)
                                        )
                                    }
                                )
                            }
                        )

                        // 3. 🟡 Binance Pay
                        PaymentMethodCard(
                            emoji = "🟡",
                            title = "Binance Pay",
                            subtitle = "USDT - Cero comisión",
                            isSelected = selectedPaymentMethod == "binance",
                            badgeText = "CRIPTOMONEDA",
                            accentColor = Color(0xFFF3BA2F),
                            onClick = {
                                selectedPaymentMethod = if (selectedPaymentMethod == "binance") null else "binance"
                            },
                            expandedContent = {
                                PaymentDetailExpanded(
                                    dataTitle = "BINANCE PAY ID",
                                    dataValue = "829371042",
                                    alias = "Alias: WSCRM_PRO",
                                    amount = activePrice,
                                    accentColor = Color(0xFFF3BA2F),
                                    onCopy = {
                                        clipboardManager.setText(androidx.compose.ui.text.AnnotatedString("829371042"))
                                        copiedNotice = "¡ID de Binance Pay copiado!"
                                    },
                                    onOpenReferenceModal = {
                                        activePaymentModal = SelectedPaymentConfig(
                                            metodo = "Binance Pay",
                                            datosPago = "829371042 (ID Binance)",
                                            colorNeon = Color(0xFFF3BA2F)
                                        )
                                    }
                                )
                            }
                        )

                        // 4. 💵 Zelle
                        PaymentMethodCard(
                            emoji = "💵",
                            title = "Zelle",
                            subtitle = "USD Directo",
                            isSelected = selectedPaymentMethod == "zelle",
                            badgeText = "EE.UU.",
                            accentColor = Color(0xFF7414CA),
                            onClick = {
                                selectedPaymentMethod = if (selectedPaymentMethod == "zelle") null else "zelle"
                            },
                            expandedContent = {
                                PaymentDetailExpanded(
                                    dataTitle = "CORREO ZELLE",
                                    dataValue = "pagos.wscrm@gmail.com",
                                    alias = "Titular: WS CRM Enterprise LLC",
                                    amount = activePrice,
                                    accentColor = Color(0xFF9D4EDD),
                                    onCopy = {
                                        clipboardManager.setText(androidx.compose.ui.text.AnnotatedString("pagos.wscrm@gmail.com"))
                                        copiedNotice = "¡Correo de Zelle copiado!"
                                    },
                                    onOpenReferenceModal = {
                                        activePaymentModal = SelectedPaymentConfig(
                                            metodo = "Zelle",
                                            datosPago = "pagos.wscrm@gmail.com (WS CRM LLC)",
                                            colorNeon = Color(0xFF9D4EDD)
                                        )
                                    }
                                )
                            }
                        )

                        // 5. 💳 Zinli
                        PaymentMethodCard(
                            emoji = "💳",
                            title = "Zinli",
                            subtitle = "Visa USD Prepago",
                            isSelected = selectedPaymentMethod == "zinli",
                            badgeText = "VISA PREPAGO",
                            accentColor = Color(0xFF00E5FF),
                            onClick = {
                                selectedPaymentMethod = if (selectedPaymentMethod == "zinli") null else "zinli"
                            },
                            expandedContent = {
                                PaymentDetailExpanded(
                                    dataTitle = "CORREO ZINLI",
                                    dataValue = "pagos.zinli@wscrm.app",
                                    alias = "Titular: WS CRM Corp",
                                    amount = activePrice,
                                    accentColor = Color(0xFF00E5FF),
                                    onCopy = {
                                        clipboardManager.setText(androidx.compose.ui.text.AnnotatedString("pagos.zinli@wscrm.app"))
                                        copiedNotice = "¡Correo Zinli copiado!"
                                    },
                                    onOpenReferenceModal = {
                                        activePaymentModal = SelectedPaymentConfig(
                                            metodo = "Zinli",
                                            datosPago = "pagos.zinli@wscrm.app",
                                            colorNeon = Color(0xFF00E5FF)
                                        )
                                    }
                                )
                            }
                        )
                    }

                    if (copiedNotice != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "📋 $copiedNotice",
                            color = NeonGreen,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Botón para validar y activar
                    Button(
                        onClick = {
                            onActivateVip()
                            onDismissRequest()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("activate_vip_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = goldColor,
                            contentColor = Color.Black
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Diamond,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isCurrentVip) "PLAN VIP PRO ACTIVO ⭐" else "VALIDAR Y ACTIVAR PRO",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Footer: Términos y Servicios & Soporte Oficial
                    var isTermsDialogOpen by remember { mutableStateOf(false) }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Términos y Servicios",
                            color = Color(0xFF00E5FF),
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable { isTermsDialogOpen = true }
                                .padding(vertical = 4.dp, horizontal = 6.dp)
                        )
                        Text(
                            text = "•",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                        Text(
                            text = "Soporte: ellisxmicros@gmail.com",
                            color = TextMuted,
                            fontSize = 10.5.sp,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier
                                .clickable {
                                    val intent = android.content.Intent(android.content.Intent.ACTION_SENDTO).apply {
                                        data = android.net.Uri.parse("mailto:ellisxmicros@gmail.com")
                                        putExtra(android.content.Intent.EXTRA_SUBJECT, "Soporte - WS CRM")
                                    }
                                    try { context.startActivity(intent) } catch (_: Exception) {}
                                }
                                .padding(vertical = 4.dp, horizontal = 6.dp)
                        )
                    }

                    if (isTermsDialogOpen) {
                        TermsAndServicesDialog(onDismissRequest = { isTermsDialogOpen = false })
                    }

                    if (activePaymentModal != null) {
                        val config = activePaymentModal!!
                        PaymentConfirmationBottomSheet(
                            metodo = config.metodo,
                            datosPago = config.datosPago,
                            colorNeon = config.colorNeon,
                            montoText = activePrice,
                            onValidateAndActivate = {
                                onActivateVip()
                                activePaymentModal = null
                                onDismissRequest()
                            },
                            onDismissRequest = { activePaymentModal = null }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main VIP Card Beneficios
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF161624))
                    .border(1.dp, Color(0xFF2A2A3D), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "BENEFICIOS VIP INCLUIDOS",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = goldColor
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = goldColor,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                            Text(
                                text = "Acceso completo e ilimitado para vendedores",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Feature List
                    VipFeatureItem("Plantillas y respuestas rápidas ilimitadas", goldColor)
                    VipFeatureItem("Relleno dinámico inteligente ({nombre}, {precio}, {producto})", goldColor)
                    VipFeatureItem("Burbuja flotante de sistema 24/7 sobre WhatsApp", goldColor)
                    VipFeatureItem("Historial completo de interacciones con cada cliente", goldColor)
                    VipFeatureItem("CRM Pipeline sin límites de contactos ni ventas", goldColor)
                    VipFeatureItem("Exportación y respaldo local en CSV / JSON", goldColor)
                    VipFeatureItem("Insignia Dorada de Agente VIP", goldColor)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Plan comparison details
            Text(
                text = "¿CÓMO SE COMPARAN LOS PLANES?",
                fontFamily = FontFamily.Monospace,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                color = TextMuted,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Free Plan box
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF161622))
                        .border(1.dp, Color(0xFF2A2A3D), RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Text(
                            text = "GRATIS",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("• Hasta 15 plantillas", fontSize = 11.sp, color = TextMuted)
                        Text("• 25 leads en pipeline", fontSize = 11.sp, color = TextMuted)
                        Text("• Burbuja in-app", fontSize = 11.sp, color = TextMuted)
                        Text("• Almacenamiento local", fontSize = 11.sp, color = TextMuted)
                    }
                }

                // Team Plan box
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF161622))
                        .border(1.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Text(
                            text = "AGENCIA / TEAM",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = NeonCyan
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("\$14.99 / mes", fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = Color.White)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("• Hasta 5 vendedores", fontSize = 11.sp, color = TextMuted)
                        Text("• Plantillas sincronizadas", fontSize = 11.sp, color = TextMuted)
                        Text("• Múltiples pipelines", fontSize = 11.sp, color = TextMuted)
                        Text("• Integración Webhook", fontSize = 11.sp, color = TextMuted)
                    }
                }
            }
        }
    }
}

@Composable
private fun VipFeatureItem(text: String, accentColor: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 3.dp)
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = accentColor,
            modifier = Modifier.size(15.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = Color(0xFFE2E2EC)
        )
    }
}

@Composable
private fun PaymentMethodCard(
    emoji: String,
    title: String,
    subtitle: String,
    isSelected: Boolean,
    badgeText: String,
    accentColor: Color,
    onClick: () -> Unit,
    expandedContent: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) Color(0xFF1B1B2C) else Color(0xFF14141E))
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) accentColor else Color(0xFF282838),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = emoji, fontSize = 20.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = title,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                    Text(
                        text = subtitle,
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(accentColor.copy(alpha = 0.15f))
                    .border(1.dp, accentColor.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 7.dp, vertical = 2.dp)
            ) {
                Text(
                    text = badgeText,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )
            }
        }

        if (isSelected) {
            Spacer(modifier = Modifier.height(10.dp))
            expandedContent()
        }
    }
}

data class SelectedPaymentConfig(
    val metodo: String,
    val datosPago: String,
    val colorNeon: Color
)

@Composable
private fun PaymentDetailExpanded(
    dataTitle: String,
    dataValue: String,
    alias: String,
    amount: String,
    accentColor: Color,
    onCopy: () -> Unit,
    onOpenReferenceModal: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF0F0F16))
            .border(1.dp, Color(0xFF262636), RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "MONTO A TRANSFERIR:",
                fontFamily = FontFamily.Monospace,
                fontSize = 10.5.sp,
                color = TextMuted
            )
            Text(
                text = amount,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = Color(0xFFFFD700)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = dataTitle,
            fontFamily = FontFamily.Monospace,
            fontSize = 10.sp,
            color = Color(0xFF00E5FF)
        )

        Text(
            text = dataValue,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = Color.White
        )

        Text(
            text = alias,
            fontSize = 10.5.sp,
            color = TextMuted
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onCopy,
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF222234),
                    contentColor = Color(0xFF00E5FF)
                )
            ) {
                Text(
                    text = "📋 COPIAR DATOS",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = onOpenReferenceModal,
                modifier = Modifier
                    .weight(1.3f)
                    .height(40.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = accentColor,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "⚡ VALIDAR PAGO",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}

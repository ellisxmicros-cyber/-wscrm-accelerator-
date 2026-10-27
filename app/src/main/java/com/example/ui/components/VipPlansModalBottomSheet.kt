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
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material.icons.filled.Close
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
import com.example.util.tr

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
            // Header Badge + Botón X de cierre
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
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
                        text = tr("MEMBRESÍA VIP // POTENCIA TU WHATSAPP", "VIP MEMBERSHIP // BOOST YOUR WHATSAPP"),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = goldColor,
                        letterSpacing = 0.8.sp
                    )
                }

                CyberCloseButton(
                    onClick = onDismissRequest,
                    tint = goldColor,
                    borderColor = goldColor.copy(alpha = 0.6f),
                    sizeDp = 30,
                    modifier = Modifier.testTag("close_vip_modal_top")
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = tr("Desbloquea el Potencial Ilimitado", "Unlock Unlimited Sales Potential"),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.White
            )

            Text(
                text = tr(
                    "Automatiza tus ventas y ahorra hasta 3 horas al día respondiendo a clientes",
                    "Automate your sales and save up to 3 hours a day replying to customers"
                ),
                fontSize = 12.5.sp,
                color = TextMuted,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            // ==========================================
            // SELECCIÓN VISUAL DE PLANES (PRECIOS GRANDES Y CLAROS CON X)
            // ==========================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Tarjeta 1: Plan Mensual
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (!isYearly) Color(0xFF132238) else Color(0xFF14141E))
                        .border(
                            width = if (!isYearly) 2.dp else 1.dp,
                            color = if (!isYearly) NeonCyan else Color(0xFF282838),
                            shape = RoundedCornerShape(14.dp)
                        )
                        .clickable { isYearly = false }
                        .padding(12.dp)
                ) {
                    CyberCloseButton(
                        onClick = onDismissRequest,
                        tint = NeonCyan,
                        borderColor = NeonCyan.copy(alpha = 0.5f),
                        sizeDp = 22,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .testTag("close_monthly_plan_box")
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Text(
                            text = tr("PLAN MENSUAL", "MONTHLY PLAN"),
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = if (!isYearly) NeonCyan else TextMuted
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "$4.99",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Black,
                                fontSize = 22.sp,
                                color = Color.White
                            )
                            Text(
                                text = " USD",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = Color(0xFF00E5FF),
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        }
                        Text(
                            text = tr("por mes", "per month"),
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (!isYearly) NeonCyan.copy(alpha = 0.2f) else Color(0xFF20202E))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = if (!isYearly) tr("✓ SELECCIONADO", "✓ SELECTED") else tr("Elegir Mensual", "Choose Monthly"),
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (!isYearly) NeonCyan else Color.Gray
                            )
                        }
                    }
                }

                // Tarjeta 2: Plan Anual VIP
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isYearly) Color(0xFF2E2410) else Color(0xFF14141E))
                        .border(
                            width = if (isYearly) 2.dp else 1.dp,
                            color = if (isYearly) goldColor else Color(0xFF282838),
                            shape = RoundedCornerShape(14.dp)
                        )
                        .clickable { isYearly = true }
                        .padding(12.dp)
                ) {
                    CyberCloseButton(
                        onClick = onDismissRequest,
                        tint = goldColor,
                        borderColor = goldColor.copy(alpha = 0.6f),
                        sizeDp = 22,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .testTag("close_yearly_plan_box")
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(goldColor)
                                .padding(horizontal = 6.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = tr("AHORRA 35% 🔥", "SAVE 35% 🔥"),
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Black,
                                fontSize = 8.5.sp,
                                color = Color.Black
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "PLAN ANUAL VIP",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = if (isYearly) goldColor else TextMuted
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "$39.99",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Black,
                                fontSize = 22.sp,
                                color = goldColor
                            )
                            Text(
                                text = " USD",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = goldColor,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        }
                        Text(
                            text = "por año ($3.33/mes)",
                            fontSize = 10.5.sp,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isYearly) goldColor.copy(alpha = 0.25f) else Color(0xFF20202E))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = if (isYearly) "★ SELECCIONADO" else "Elegir Anual",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isYearly) goldColor else Color.Gray
                            )
                        }
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
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
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
                                fontSize = 16.sp,
                                color = goldColor,
                                letterSpacing = 0.8.sp
                            )
                        }

                        CyberCloseButton(
                            onClick = onDismissRequest,
                            tint = goldColor,
                            borderColor = goldColor.copy(alpha = 0.6f),
                            sizeDp = 26,
                            modifier = Modifier.testTag("close_activate_pro_box")
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val activePrice = if (isYearly) "$39.99 USD" else "$4.99 USD"

                    // Banner de Resumen de Monto Seleccionado
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF1F1B2C))
                            .border(1.dp, goldColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isYearly) "PLAN: ANUAL VIP (35% OFF)" else "PLAN: MENSUAL FLEXIBLE",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.5.sp,
                            color = if (isYearly) goldColor else NeonCyan
                        )
                        Text(
                            text = "TOTAL: $activePrice",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp,
                            color = Color(0xFFFFD700)
                        )
                    }

                    Text(
                        text = "Selecciona tu método de pago preferido para activar:",
                        fontSize = 11.5.sp,
                        color = TextMuted,
                        modifier = Modifier.padding(top = 10.dp, bottom = 12.dp)
                    )

                    var selectedPaymentMethod by remember { mutableStateOf<String?>(null) }
                    var copiedNotice by remember { mutableStateOf<String?>(null) }
                    var activePaymentModal by remember { mutableStateOf<SelectedPaymentConfig?>(null) }

                    val clipboardManager = androidx.compose.ui.platform.LocalClipboardManager.current
                    val context = androidx.compose.ui.platform.LocalContext.current

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        // 1. 🍋 Lemon Squeezy / Suscripción Recurrente Automática (RECOMENDADO)
                        PaymentMethodCard(
                            emoji = "🍋",
                            title = "Suscripción Automática",
                            subtitle = "Tarjeta de Crédito/Débito • Factura Automática",
                            isSelected = selectedPaymentMethod == "lemonsqueezy",
                            badgeText = "AUTOMÁTICO ⚡",
                            priceBadge = activePrice,
                            accentColor = Color(0xFFFFD000),
                            onClick = {
                                selectedPaymentMethod = if (selectedPaymentMethod == "lemonsqueezy") null else "lemonsqueezy"
                            },
                            onClose = {
                                if (selectedPaymentMethod == "lemonsqueezy") {
                                    selectedPaymentMethod = null
                                } else {
                                    onDismissRequest()
                                }
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
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "PASARELA 100% AUTOMATIZADA",
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 10.sp,
                                            color = Color(0xFFFFD000)
                                        )
                                        CyberCloseButton(
                                            onClick = { selectedPaymentMethod = null },
                                            tint = Color(0xFFFFD000),
                                            borderColor = Color(0xFFFFD000).copy(alpha = 0.5f),
                                            sizeDp = 22
                                        )
                                    }
                                    Text(
                                        text = "El cobro se procesa en línea con Tarjeta Visa/Mastercard o Google Pay. La pasarela emite la factura legal a tu correo y activa la licencia sin esperas.",
                                        fontSize = 11.5.sp,
                                        color = TextMuted,
                                        modifier = Modifier.padding(top = 4.dp, bottom = 10.dp)
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
                                            .heightIn(min = 52.dp),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFFFFD000),
                                            contentColor = Color.Black
                                        )
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(
                                                text = "🚀 ABRIR CHECKOUT SEGURO",
                                                fontFamily = FontFamily.Monospace,
                                                fontWeight = FontWeight.Black,
                                                fontSize = 13.sp,
                                                color = Color.Black
                                            )
                                            Text(
                                                text = "Monto a Pagar: $activePrice",
                                                fontFamily = FontFamily.Monospace,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp,
                                                color = Color.Black.copy(alpha = 0.85f)
                                            )
                                        }
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
                            priceBadge = activePrice,
                            accentColor = Color(0xFF0079C1),
                            onClick = {
                                selectedPaymentMethod = if (selectedPaymentMethod == "paypal") null else "paypal"
                            },
                            onClose = {
                                if (selectedPaymentMethod == "paypal") {
                                    selectedPaymentMethod = null
                                } else {
                                    onDismissRequest()
                                }
                            },
                            expandedContent = {
                                PaymentDetailExpanded(
                                    dataTitle = "CORREO PAYPAL OFICIAL",
                                    dataValue = "ellisxmicros@gmail.com",
                                    alias = "Cuenta Oficial: ellisxmicros@gmail.com",
                                    amount = activePrice,
                                    accentColor = Color(0xFF0079C1),
                                    onCopy = {
                                        clipboardManager.setText(androidx.compose.ui.text.AnnotatedString("ellisxmicros@gmail.com"))
                                        copiedNotice = "¡Correo PayPal copiado!"
                                    },
                                    onOpenReferenceModal = {
                                        activePaymentModal = SelectedPaymentConfig(
                                            metodo = "PayPal",
                                            datosPago = "ellisxmicros@gmail.com",
                                            colorNeon = Color(0xFF0079C1)
                                        )
                                    },
                                    onCloseExpanded = { selectedPaymentMethod = null }
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
                            priceBadge = activePrice,
                            accentColor = Color(0xFFF3BA2F),
                            onClick = {
                                selectedPaymentMethod = if (selectedPaymentMethod == "binance") null else "binance"
                            },
                            onClose = {
                                if (selectedPaymentMethod == "binance") {
                                    selectedPaymentMethod = null
                                } else {
                                    onDismissRequest()
                                }
                            },
                            expandedContent = {
                                PaymentDetailExpanded(
                                    dataTitle = "BINANCE PAY EMAIL",
                                    dataValue = "ellisxmicros@gmail.com",
                                    alias = "Alias: Binance Pay Oficial",
                                    amount = activePrice,
                                    accentColor = Color(0xFFF3BA2F),
                                    onCopy = {
                                        clipboardManager.setText(androidx.compose.ui.text.AnnotatedString("ellisxmicros@gmail.com"))
                                        copiedNotice = "¡Correo de Binance Pay copiado!"
                                    },
                                    onOpenReferenceModal = {
                                        activePaymentModal = SelectedPaymentConfig(
                                            metodo = "Binance Pay",
                                            datosPago = "ellisxmicros@gmail.com (Binance)",
                                            colorNeon = Color(0xFFF3BA2F)
                                        )
                                    },
                                    onCloseExpanded = { selectedPaymentMethod = null }
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
                            priceBadge = activePrice,
                            accentColor = Color(0xFF7414CA),
                            onClick = {
                                selectedPaymentMethod = if (selectedPaymentMethod == "zelle") null else "zelle"
                            },
                            onClose = {
                                if (selectedPaymentMethod == "zelle") {
                                    selectedPaymentMethod = null
                                } else {
                                    onDismissRequest()
                                }
                            },
                            expandedContent = {
                                PaymentDetailExpanded(
                                    dataTitle = "CORREO ZELLE",
                                    dataValue = "ellisxmicros@gmail.com",
                                    alias = "Titular: WS CRM Enterprise",
                                    amount = activePrice,
                                    accentColor = Color(0xFF9D4EDD),
                                    onCopy = {
                                        clipboardManager.setText(androidx.compose.ui.text.AnnotatedString("ellisxmicros@gmail.com"))
                                        copiedNotice = "¡Correo de Zelle copiado!"
                                    },
                                    onOpenReferenceModal = {
                                        activePaymentModal = SelectedPaymentConfig(
                                            metodo = "Zelle",
                                            datosPago = "ellisxmicros@gmail.com",
                                            colorNeon = Color(0xFF9D4EDD)
                                        )
                                    },
                                    onCloseExpanded = { selectedPaymentMethod = null }
                                )
                            }
                        )

                        // 5. 💳 Zinli
                        PaymentMethodCard(
                            emoji = "💳",
                            title = "Zinli",
                            subtitle = "Transferencia Instantánea • 0% Comisión",
                            isSelected = selectedPaymentMethod == "zinli",
                            badgeText = "⭐ RECOMENDADO",
                            priceBadge = activePrice,
                            accentColor = Color(0xFF00E5FF),
                            onClick = {
                                selectedPaymentMethod = if (selectedPaymentMethod == "zinli") null else "zinli"
                            },
                            onClose = {
                                if (selectedPaymentMethod == "zinli") {
                                    selectedPaymentMethod = null
                                } else {
                                    onDismissRequest()
                                }
                            },
                            expandedContent = {
                                PaymentDetailExpanded(
                                    dataTitle = "CORREO ZINLI OFICIAL",
                                    dataValue = "ellisxmicros@gmail.com",
                                    alias = "Titular: Cuenta Zinli Verificada",
                                    amount = activePrice,
                                    accentColor = Color(0xFF00E5FF),
                                    onCopy = {
                                        clipboardManager.setText(androidx.compose.ui.text.AnnotatedString("ellisxmicros@gmail.com"))
                                        copiedNotice = "¡Correo Zinli copiado: ellisxmicros@gmail.com!"
                                    },
                                    onOpenReferenceModal = {
                                        activePaymentModal = SelectedPaymentConfig(
                                            metodo = "Zinli",
                                            datosPago = "ellisxmicros@gmail.com",
                                            colorNeon = Color(0xFF00E5FF)
                                        )
                                    },
                                    onCloseExpanded = { selectedPaymentMethod = null }
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
                            .height(50.dp)
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
                            text = if (isCurrentVip) "PLAN VIP PRO ACTIVO ⭐" else "VALIDAR Y ACTIVAR PRO ($activePrice)",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Black,
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
                        Column(modifier = Modifier.weight(1f)) {
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

                        CyberCloseButton(
                            onClick = onDismissRequest,
                            tint = goldColor,
                            borderColor = goldColor.copy(alpha = 0.5f),
                            sizeDp = 24,
                            modifier = Modifier.testTag("close_vip_benefits_box")
                        )
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
                    CyberCloseButton(
                        onClick = onDismissRequest,
                        tint = Color.White,
                        borderColor = Color(0xFF3A3A52),
                        sizeDp = 20,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .testTag("close_free_plan_box")
                    )

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
                    CyberCloseButton(
                        onClick = onDismissRequest,
                        tint = NeonCyan,
                        borderColor = NeonCyan.copy(alpha = 0.5f),
                        sizeDp = 20,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .testTag("close_team_plan_box")
                    )

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
private fun CyberCloseButton(
    onClick: () -> Unit,
    tint: Color = Color.White,
    borderColor: Color = Color(0xFF3A3A52),
    backgroundColor: Color = Color(0xFF1E1E2E),
    sizeDp: Int = 26,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(sizeDp.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .border(1.dp, borderColor, CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Cerrar",
            tint = tint,
            modifier = Modifier.size((sizeDp * 0.58f).dp)
        )
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
    priceBadge: String,
    accentColor: Color,
    onClick: () -> Unit,
    onClose: () -> Unit = {},
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
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

            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(horizontalAlignment = Alignment.End) {
                    // Etiqueta destacada del Precio
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF222234))
                            .border(1.dp, Color(0xFFFFD700).copy(alpha = 0.7f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = priceBadge,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFFFD700)
                        )
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = badgeText,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                CyberCloseButton(
                    onClick = onClose,
                    tint = if (isSelected) accentColor else TextMuted,
                    borderColor = if (isSelected) accentColor.copy(alpha = 0.6f) else Color(0xFF3A3A52),
                    sizeDp = 24
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
    onOpenReferenceModal: () -> Unit,
    onCloseExpanded: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF0F0F16))
            .border(1.dp, Color(0xFF262636), RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        // Banner de Monto Exacto a Transferir + Botón X
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFF231C0E))
                .border(1.dp, Color(0xFFFFD700).copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "TOTAL A TRANSFERIR:",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = amount,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp,
                        color = Color(0xFFFFD700)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    CyberCloseButton(
                        onClick = onCloseExpanded,
                        tint = Color(0xFFFFD700),
                        borderColor = Color(0xFFFFD700).copy(alpha = 0.6f),
                        sizeDp = 22
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

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

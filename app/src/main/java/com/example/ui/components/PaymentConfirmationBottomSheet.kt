package com.example.ui.components

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NeonGreen

/**
 * Modal Bottom Sheet para validar y procesar el pago de la Licencia PRO.
 * Adaptación en Jetpack Compose de mostrarModalPago con estética Cyberpunk.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentConfirmationBottomSheet(
    metodo: String,
    datosPago: String,
    colorNeon: Color,
    montoText: String = "$4.99 USD",
    onValidateAndActivate: (reference: String) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    var referencia by remember { mutableStateOf("") }
    var emailFacturacion by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = Color(0xFF121212), // Fondo Cyberpunk oscuro exacto
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
        modifier = modifier
            .imePadding()
            .testTag("payment_confirmation_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(24.dp)
        ) {
            // Título: Pagar con [Método] + Botón X
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pagar con $metodo",
                    color = colorNeon,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.weight(1f)
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(colorNeon.copy(alpha = 0.15f))
                        .border(1.dp, colorNeon.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = montoText,
                        color = Color.White,
                        fontSize = 11.5.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                IconButton(
                    onClick = onDismissRequest,
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1E1E2E))
                        .border(1.dp, colorNeon.copy(alpha = 0.6f), CircleShape)
                        .testTag("close_payment_confirmation_sheet")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = colorNeon,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Transfiere a la siguiente cuenta y pega el número de referencia abajo:",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // CAJA CON LOS DATOS DE PAGO Y BOTÓN COPIAR
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Black)
                    .border(1.dp, colorNeon.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = datosPago,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(datosPago))
                            Toast.makeText(
                                context,
                                "¡$metodo copiado al portapapeles!",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copiar",
                            tint = colorNeon,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // CAMPO: CORREO ELECTRÓNICO PARA FACTURACIÓN
            OutlinedTextField(
                value = emailFacturacion,
                onValueChange = {
                    emailFacturacion = it
                    if (it.isNotBlank()) errorMessage = null
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("payment_email_input"),
                placeholder = {
                    Text(
                        text = "Tu correo para recibir factura y confirmación",
                        color = Color.White.copy(alpha = 0.38f),
                        fontSize = 13.5.sp
                    )
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF1E1E1E),
                    unfocusedContainerColor = Color(0xFF1E1E1E),
                    focusedBorderColor = colorNeon,
                    unfocusedBorderColor = Color(0xFF2A2A38),
                    cursorColor = colorNeon
                ),
                shape = RoundedCornerShape(10.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // CAMPO PARA PEGAR LA REFERENCIA
            OutlinedTextField(
                value = referencia,
                onValueChange = {
                    referencia = it
                    if (it.isNotBlank()) errorMessage = null
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("payment_reference_input"),
                placeholder = {
                    Text(
                        text = "Número de Referencia / ID de Transacción",
                        color = Color.White.copy(alpha = 0.38f),
                        fontSize = 13.5.sp
                    )
                },
                singleLine = true,
                isError = errorMessage != null,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF1E1E1E),
                    unfocusedContainerColor = Color(0xFF1E1E1E),
                    focusedBorderColor = colorNeon,
                    unfocusedBorderColor = Color(0xFF2A2A38),
                    cursorColor = colorNeon
                ),
                shape = RoundedCornerShape(10.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = errorMessage ?: "",
                    color = Color(0xFFFF5252),
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // BOTÓN DE CONFIRMACIÓN AUTOMATIZADA (SIN WHATSAPP)
            Button(
                onClick = {
                    val ref = referencia.trim()
                    val email = emailFacturacion.trim()

                    if (email.isEmpty() || !email.contains("@")) {
                        errorMessage = "Por favor ingresa un correo electrónico válido"
                        Toast.makeText(
                            context,
                            "Por favor ingresa tu correo para enviarte la factura",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    if (ref.isEmpty()) {
                        errorMessage = "Por favor ingresa el número de referencia / transacción"
                        Toast.makeText(
                            context,
                            "Por favor ingresa el número de referencia",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    focusManager.clearFocus()

                    Toast.makeText(
                        context,
                        "⚡ Transacción verificada. Factura enviada a $email. Atención al cliente: ellisxmicros@gmail.com",
                        Toast.LENGTH_LONG
                    ).show()

                    onValidateAndActivate(ref)
                    onDismissRequest()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("btn_validar_activar_pro"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorNeon,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "VALIDAR Y ACTIVAR PRO ⚡",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

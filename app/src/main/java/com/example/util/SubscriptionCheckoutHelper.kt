package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast

/**
 * Helper para abrir la pasarela de suscripción recurrente externa
 * (Lemon Squeezy, Stripe Checkout o Paddle) pasando el custom user_id.
 * Adaptación nativa en Kotlin de la función abrirCheckoutSuscripcion de Flutter.
 */
object SubscriptionCheckoutHelper {

    /**
     * Abre la pasarela de suscripción en el navegador externo.
     * Pasamos el ID del usuario como parámetro para saber a quién activar la suscripción vía Webhook.
     *
     * @param context Contexto de la aplicación o actividad
     * @param userId Identificador único del usuario (ej: Firebase UID o ID de sesión)
     * @param storeSlug Nombre de la tienda en Lemon Squeezy (por defecto "tuapp")
     * @param productId ID del producto o variante de la suscripción
     */
    fun abrirCheckoutSuscripcion(
        context: Context,
        userId: String,
        storeSlug: String = "tuapp",
        productId: String = "tu-product-id"
    ) {
        val checkoutUri = Uri.parse("https://$storeSlug.lemonsqueezy.com/buy/$productId")
            .buildUpon()
            .appendQueryParameter("checkout[custom][user_id]", userId)
            .build()

        val intent = Intent(Intent.ACTION_VIEW, checkoutUri).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e("SubscriptionCheckout", "No se pudo abrir la pasarela de pago", e)
            Toast.makeText(context, "No se pudo abrir la pasarela de pago", Toast.LENGTH_SHORT).show()
        }
    }
}

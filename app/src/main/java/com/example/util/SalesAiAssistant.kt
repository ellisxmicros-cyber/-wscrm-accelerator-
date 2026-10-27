package com.example.util

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object SalesAiAssistant {

    private const val TAG = "SalesAiAssistant"
    private const val GEMINI_MODEL = "gemini-3.5-flash"
    private const val GEMINI_BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/"

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    enum class PersuasionTone(val labelEs: String, val labelEn: String, val emoji: String) {
        URGENCY("Urgencia & Escasez", "Urgency & Scarcity", "🔥"),
        BENEFITS_AIDA("Modelo AIDA (Ventas)", "AIDA Model (Sales)", "🎯"),
        WARM_CLOSING("Cercano & Empático", "Warm & Empathetic", "🤝"),
        OBJECTION_KILLER("Manejo de Objeciones", "Objection Killer", "🛡️"),
        FORMAL_B2B("Profesional Corporativo", "Formal B2B", "💼")
    }

    /**
     * Calls Gemini 3.5 Flash via direct REST API to generate a high-converting WhatsApp sales message.
     * If the API key is not configured or network fails, falls back gracefully to local neural templates.
     */
    suspend fun generateSalesPitchWithGemini(
        instruction: String,
        tone: PersuasionTone = PersuasionTone.BENEFITS_AIDA,
        clientName: String = "",
        productName: String = "",
        isEnglish: Boolean = false
    ): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (_: Throwable) {
            ""
        }

        val isValidKey = apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY"

        if (isValidKey) {
            try {
                val promptText = buildPrompt(
                    instruction = instruction,
                    tone = tone,
                    clientName = clientName,
                    productName = productName,
                    isEnglish = isEnglish
                )

                val requestJson = JSONObject().apply {
                    val contentsArray = JSONArray().apply {
                        val contentObject = JSONObject().apply {
                            val partsArray = JSONArray().apply {
                                put(JSONObject().put("text", promptText))
                            }
                            put("parts", partsArray)
                        }
                        put(contentObject)
                    }
                    put("contents", contentsArray)

                    val generationConfig = JSONObject().apply {
                        put("temperature", 0.7)
                        put("topP", 0.95)
                        put("topK", 40)
                    }
                    put("generationConfig", generationConfig)

                    val systemInstruction = JSONObject().apply {
                        val parts = JSONArray().apply {
                            put(
                                JSONObject().put(
                                    "text",
                                    "You are TurboVentas AI, an elite WhatsApp sales closer. " +
                                            "Your task is to craft high-converting, concise WhatsApp messages with strategic emoji usage, " +
                                            "*bold* highlights, and compelling call-to-actions. Never output conversational preamble, disclaimers, or greetings like 'Here is your message:'; only output the final WhatsApp message itself."
                                )
                            )
                        }
                        put("parts", parts)
                    }
                    put("systemInstruction", systemInstruction)
                }

                val mediaType = "application/json; charset=utf-8".toMediaType()
                val requestBody = requestJson.toString().toRequestBody(mediaType)

                val endpoint = "$GEMINI_BASE_URL$GEMINI_MODEL:generateContent?key=$apiKey"
                val request = Request.Builder()
                    .url(endpoint)
                    .post(requestBody)
                    .build()

                val response = okHttpClient.newCall(request).execute()
                val responseBody = response.body?.string()

                if (response.isSuccessful && !responseBody.isNullOrBlank()) {
                    val root = JSONObject(responseBody)
                    val candidates = root.optJSONArray("candidates")
                    val firstCandidate = candidates?.optJSONObject(0)
                    val contentObj = firstCandidate?.optJSONObject("content")
                    val parts = contentObj?.optJSONArray("parts")
                    val generatedText = parts?.optJSONObject(0)?.optString("text")

                    if (!generatedText.isNullOrBlank()) {
                        return@withContext generatedText.trim()
                    }
                } else {
                    Log.w(TAG, "Gemini API error ${response.code}: $responseBody")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error invoking Gemini API: ${e.localizedMessage}", e)
            }
        }

        // Fallback to local persuasion template engine
        return@withContext optimizeTemplate(
            originalText = instruction,
            tone = tone,
            clientName = clientName,
            productName = productName
        )
    }

    private fun buildPrompt(
        instruction: String,
        tone: PersuasionTone,
        clientName: String,
        productName: String,
        isEnglish: Boolean
    ): String {
        val targetClient = if (clientName.isNotBlank()) clientName else "{nombre}"
        val targetProduct = if (productName.isNotBlank()) productName else "{producto}"
        val lang = if (isEnglish) "English" else "Spanish"

        return """
Write a high-converting WhatsApp sales message in $lang.
- Tone / Sales Strategy: ${tone.name} (${if (isEnglish) tone.labelEn else tone.labelEs})
- Context / Key Points: ${instruction.ifBlank { "Offer and close sale with immediate call to action" }}
- Recipient Name Variable: $targetClient
- Product / Service Name: $targetProduct

Formatting Rules:
1. Format for WhatsApp with *bold* on key benefits and 📲 / 🔥 / ✅ relevant emojis.
2. Structure with a warm hook, 2-3 value bullet points, and an irresistible CTA asking a question to continue the chat.
3. Keep it between 3 to 6 short lines.
4. Output ONLY the WhatsApp message text without quotes or intros.
        """.trimIndent()
    }

    fun optimizeTemplate(
        originalText: String,
        tone: PersuasionTone,
        clientName: String = "",
        productName: String = ""
    ): String {
        val nameTag = if (clientName.isNotBlank()) clientName else "{nombre}"
        val productTag = if (productName.isNotBlank()) productName else "{producto}"
        val customContext = if (originalText.isNotBlank()) "\n💡 Info: $originalText\n" else ""

        return when (tone) {
            PersuasionTone.URGENCY -> {
                """
¡Hola $nameTag! 👋 Te escribo de forma prioritaria porque hoy cerramos las últimas unidades disponibles de *$productTag*. ⏳$customContext
🔥 Si aseguras tu pedido en los próximos 60 minutos, te mantenemos el precio de preventa especial y garantía prioritaria.

¿Te reservo tu cupo antes de que se agote el stock? Escríbeme y lo confirmamos de inmediato. 🚀
                """.trimIndent()
            }

            PersuasionTone.BENEFITS_AIDA -> {
                """
¡Hola $nameTag! 👋 ¿Te gustaría optimizar tus resultados y ahorrar horas de trabajo con *$productTag*? 💡$customContext
🎯 Con esta solución obtienes:
✅ Implementación inmediata sin complicaciones
✅ Asistencia directa y personalizada
✅ Resultados visibles desde el primer día

¿Tienes 2 minutos para enviarte los detalles y la demostración rápida? Quedo atento a tu confirmación. 📲
                """.trimIndent()
            }

            PersuasionTone.WARM_CLOSING -> {
                """
¡Hola $nameTag, un gusto saludarte! 😊 Espero que estés teniendo un excelente día.$customContext
Quería saber cómo vas con la evaluación de *$productTag* y si tienes alguna duda puntual en la que te pueda orientar con gusto. 🤝

Queremos que tomes la mejor decisión con total tranquilidad. ¿Prefieres que te pase un resumen breve por aquí? Un abrazo. ✨
                """.trimIndent()
            }

            PersuasionTone.OBJECTION_KILLER -> {
                """
Entiendo perfectamente tu punto, $nameTag. 🙏 Muchos de nuestros clientes actuales pensaban exactamente lo mismo al inicio sobre *$productTag*.$customContext
Sin embargo, descubrieron que el retorno de inversión y el tiempo que ahorraron cubrió el costo en solo unas semanas:
🛡️ Cero riesgo: cuentas con respaldo y soporte técnico 1 a 1.
💳 Flexibilidad: métodos de pago directos y sin comisiones extras.

¿Te parecería bien probar una opción personalizada que se ajuste a tu presupuesto actual? Cuéntame y lo coordinamos. 🤝
                """.trimIndent()
            }

            PersuasionTone.FORMAL_B2B -> {
                """
Estimado/a $nameTag, gusto en saludarle.$customContext
Por medio del presente le hago llegar la propuesta comercial correspondiente a *$productTag*, orientada a optimizar su operativa comercial y elevar su tasa de conversión en canales digitales.

Quedamos a su entera disposición para coordinar una breve llamada informativa o solventar cualquier requerimiento técnico de su equipo.

Atentamente,
Departamento Comercial. 📑
                """.trimIndent()
            }
        }
    }
}

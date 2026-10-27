package com.example.util

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.example.data.LeadEntity
import com.example.data.LeadStage
import com.example.data.TemplateEntity
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object BackupRestoreHelper {

    fun generateBackupJson(
        templates: List<TemplateEntity>,
        leads: List<LeadEntity>
    ): String {
        val root = JSONObject()
        root.put("app", "TurboVentas WS")
        root.put("version", "2.0")
        root.put("exportDate", SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date()))
        root.put("timestamp", System.currentTimeMillis())

        val templatesArray = JSONArray()
        templates.forEach { t ->
            val obj = JSONObject()
            obj.put("title", t.title)
            obj.put("content", t.content)
            obj.put("category", t.category)
            obj.put("shortcut", t.shortcut)
            obj.put("isPinned", t.isPinned)
            obj.put("usageCount", t.usageCount)
            templatesArray.put(obj)
        }
        root.put("templates", templatesArray)

        val leadsArray = JSONArray()
        leads.forEach { l ->
            val obj = JSONObject()
            obj.put("name", l.name)
            obj.put("phone", l.phone)
            obj.put("stage", l.stage.name)
            obj.put("productInterest", l.productInterest)
            obj.put("estimatedValue", l.estimatedValue)
            obj.put("notes", l.notes)
            obj.put("lastContactedAt", l.lastContactedAt)
            obj.put("createdAt", l.createdAt)
            leadsArray.put(obj)
        }
        root.put("leads", leadsArray)

        return root.toString(2)
    }

    fun parseBackupJson(jsonString: String): Pair<List<TemplateEntity>, List<LeadEntity>> {
        val root = JSONObject(jsonString)
        val templatesList = mutableListOf<TemplateEntity>()
        val leadsList = mutableListOf<LeadEntity>()

        if (root.has("templates")) {
            val tArray = root.getJSONArray("templates")
            for (i in 0 until tArray.length()) {
                val obj = tArray.getJSONObject(i)
                templatesList.add(
                    TemplateEntity(
                        id = 0,
                        title = obj.optString("title", "Plantilla"),
                        content = obj.optString("content", ""),
                        category = obj.optString("category", "Ventas"),
                        shortcut = obj.optString("shortcut", ""),
                        isPinned = obj.optBoolean("isPinned", false),
                        usageCount = obj.optInt("usageCount", 0)
                    )
                )
            }
        }

        if (root.has("leads")) {
            val lArray = root.getJSONArray("leads")
            for (i in 0 until lArray.length()) {
                val obj = lArray.getJSONObject(i)
                val stageStr = obj.optString("stage", "NEW")
                val stage = try {
                    LeadStage.valueOf(stageStr)
                } catch (_: Exception) {
                    LeadStage.NEW
                }
                leadsList.add(
                    LeadEntity(
                        id = 0,
                        name = obj.optString("name", "Prospecto"),
                        phone = obj.optString("phone", ""),
                        stage = stage,
                        productInterest = obj.optString("productInterest", ""),
                        estimatedValue = obj.optDouble("estimatedValue", 0.0),
                        notes = obj.optString("notes", ""),
                        lastContactedAt = obj.optLong("lastContactedAt", System.currentTimeMillis()),
                        createdAt = obj.optLong("createdAt", System.currentTimeMillis())
                    )
                )
            }
        }

        return Pair(templatesList, leadsList)
    }

    fun generateCsvLeads(leads: List<LeadEntity>): String {
        val sb = StringBuilder()
        sb.append("ID,Nombre,Telefono,Etapa,Producto,Valor_USD,Notas,Ultimo_Contacto\n")
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
        leads.forEach { l ->
            val safeName = "\"${l.name.replace("\"", "\"\"")}\""
            val safePhone = "\"${l.phone}\""
            val safeStage = "\"${l.stage.label}\""
            val safeProd = "\"${l.productInterest.replace("\"", "\"\"")}\""
            val safeNotes = "\"${l.notes.replace("\"", "\"\"")}\""
            val dateStr = sdf.format(Date(l.lastContactedAt))
            sb.append("${l.id},$safeName,$safePhone,$safeStage,$safeProd,${l.estimatedValue},$safeNotes,$dateStr\n")
        }
        return sb.toString()
    }

    fun shareTextOrFile(context: Context, textContent: String, fileName: String, mimeType: String, title: String) {
        val cacheFile = File(context.cacheDir, fileName)
        cacheFile.writeText(textContent, Charsets.UTF_8)

        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            cacheFile
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, title)
            putExtra(Intent.EXTRA_TEXT, "Exportado desde TurboVentas WS")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(intent, title).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }
}

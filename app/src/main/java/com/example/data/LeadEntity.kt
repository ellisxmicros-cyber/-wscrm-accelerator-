package com.example.data

import androidx.compose.runtime.Composable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.util.tr

enum class LeadStage(val label: String, val colorHex: Long) {
    NEW("Nuevo Lead", 0xFF00E5FF),
    CONTACTED("Contactado", 0xFFBD00FF),
    QUOTED("Cotizado", 0xFFFFE600),
    NEGOTIATING("Negociación", 0xFFFF6D00),
    CLOSED_WON("Venta Ganada", 0xFF39FF14),
    CLOSED_LOST("Perdido", 0xFFFF3366);

    @Composable
    fun localizedLabel(): String = when (this) {
        NEW -> tr("Nuevo Lead", "New Lead")
        CONTACTED -> tr("Contactado", "Contacted")
        QUOTED -> tr("Cotizado", "Quoted")
        NEGOTIATING -> tr("Negociación", "Negotiating")
        CLOSED_WON -> tr("Venta Ganada", "Won Deal")
        CLOSED_LOST -> tr("Perdido", "Lost")
    }
}

@Entity(tableName = "leads")
data class LeadEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val phone: String,
    val stage: LeadStage = LeadStage.NEW,
    val productInterest: String = "",
    val estimatedValue: Double = 0.0,
    val notes: String = "",
    val lastContactedAt: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis()
)

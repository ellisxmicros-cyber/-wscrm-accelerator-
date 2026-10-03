package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class LeadStage(val label: String, val colorHex: Long) {
    NEW("Nuevo Lead", 0xFF00E5FF),
    CONTACTED("Contactado", 0xFFBD00FF),
    QUOTED("Cotizado", 0xFFFFE600),
    NEGOTIATING("Negociación", 0xFFFF6D00),
    CLOSED_WON("Venta Ganada", 0xFF39FF14),
    CLOSED_LOST("Perdido", 0xFFFF3366)
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

package com.example.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Room Entity to store customer interaction history (messages sent, templates used,
 * stage updates, notes, and direct WhatsApp chats).
 */
@Entity(
    tableName = "customer_interactions",
    indices = [
        Index(value = ["customerPhone"]),
        Index(value = ["leadId"]),
        Index(value = ["timestamp"])
    ]
)
data class CustomerInteraction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val leadId: Long? = null,
    val customerPhone: String,
    val customerName: String = "",
    val interactionType: String = TYPE_WHATSAPP_MESSAGE, // TYPE_WHATSAPP_MESSAGE, TYPE_TEMPLATE_SENT, TYPE_STAGE_CHANGE, TYPE_NOTE
    val content: String,
    val templateTitle: String? = null,
    val timestamp: Long = System.currentTimeMillis()
) {
    companion object {
        const val TYPE_WHATSAPP_MESSAGE = "WHATSAPP_MESSAGE"
        const val TYPE_TEMPLATE_SENT = "TEMPLATE_SENT"
        const val TYPE_STAGE_CHANGE = "STAGE_CHANGE"
        const val TYPE_NOTE = "NOTE"
    }
}

typealias CustomerInteractionEntity = CustomerInteraction

package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room Entity to store WhatsApp sales scripts and quick response templates.
 * Includes fields for title, content, and category for fast retrieval and filtering.
 */
@Entity(tableName = "templates")
data class Template(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val category: String = "General",
    val shortcut: String = "",
    val isPinned: Boolean = false,
    val usageCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

typealias TemplateEntity = Template

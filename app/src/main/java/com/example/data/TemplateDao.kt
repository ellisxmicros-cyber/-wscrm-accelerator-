package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TemplateDao {
    @Query("SELECT * FROM templates ORDER BY isPinned DESC, usageCount DESC, id ASC")
    fun getAllTemplates(): Flow<List<TemplateEntity>>

    @Query("SELECT * FROM templates WHERE isPinned = 1 ORDER BY usageCount DESC")
    fun getPinnedTemplates(): Flow<List<TemplateEntity>>

    @Query("SELECT * FROM templates WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%' ORDER BY isPinned DESC, usageCount DESC")
    fun searchTemplates(query: String): Flow<List<TemplateEntity>>

    @Query("SELECT COUNT(*) FROM templates")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTemplate(template: TemplateEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(templates: List<TemplateEntity>)

    @Update
    suspend fun updateTemplate(template: TemplateEntity)

    @Delete
    suspend fun deleteTemplate(template: TemplateEntity)

    @Query("UPDATE templates SET usageCount = usageCount + 1 WHERE id = :id")
    suspend fun incrementUsage(id: Long)
}

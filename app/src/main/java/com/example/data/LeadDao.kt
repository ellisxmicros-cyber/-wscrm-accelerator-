package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LeadDao {
    @Query("SELECT * FROM leads ORDER BY lastContactedAt DESC")
    fun getAllLeads(): Flow<List<LeadEntity>>

    @Query("SELECT * FROM leads WHERE stage = :stage ORDER BY lastContactedAt DESC")
    fun getLeadsByStage(stage: LeadStage): Flow<List<LeadEntity>>

    @Query("SELECT * FROM leads WHERE name LIKE '%' || :query || '%' OR phone LIKE '%' || :query || '%' OR productInterest LIKE '%' || :query || '%' ORDER BY lastContactedAt DESC")
    fun searchLeads(query: String): Flow<List<LeadEntity>>

    @Query("SELECT COUNT(*) FROM leads")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLead(lead: LeadEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(leads: List<LeadEntity>)

    @Update
    suspend fun updateLead(lead: LeadEntity)

    @Delete
    suspend fun deleteLead(lead: LeadEntity)

    @Query("UPDATE leads SET stage = :stage, lastContactedAt = :lastContacted WHERE id = :id")
    suspend fun updateStage(id: Long, stage: LeadStage, lastContacted: Long)
}

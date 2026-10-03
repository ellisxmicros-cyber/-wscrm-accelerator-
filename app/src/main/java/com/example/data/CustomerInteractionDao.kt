package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerInteractionDao {

    @Query("SELECT * FROM customer_interactions ORDER BY timestamp DESC")
    fun getAllInteractions(): Flow<List<CustomerInteractionEntity>>

    @Query("SELECT * FROM customer_interactions WHERE customerPhone = :phone ORDER BY timestamp DESC")
    fun getInteractionsByPhone(phone: String): Flow<List<CustomerInteractionEntity>>

    @Query("SELECT * FROM customer_interactions WHERE leadId = :leadId ORDER BY timestamp DESC")
    fun getInteractionsByLead(leadId: Long): Flow<List<CustomerInteractionEntity>>

    @Query("SELECT * FROM customer_interactions ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentInteractions(limit: Int = 50): Flow<List<CustomerInteractionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(interaction: CustomerInteractionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(interactions: List<CustomerInteractionEntity>)

    @Query("DELETE FROM customer_interactions WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM customer_interactions WHERE customerPhone = :phone")
    suspend fun deleteByPhone(phone: String)

    @Query("SELECT COUNT(*) FROM customer_interactions")
    suspend fun getCount(): Int

    @Query("DELETE FROM customer_interactions")
    suspend fun clearAll()
}

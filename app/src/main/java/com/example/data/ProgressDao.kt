package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDao {
    @Query("SELECT * FROM progress_record ORDER BY timestamp DESC")
    fun getAllProgress(): Flow<List<ProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(record: ProgressEntity)

    @Query("DELETE FROM progress_record")
    suspend fun clearAllProgress()
}

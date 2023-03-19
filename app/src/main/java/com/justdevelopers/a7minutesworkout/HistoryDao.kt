package com.justdevelopers.a7minutesworkout

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(HistoryEntry: HistoryEntity)

    @Query("Select * From `history-table` Order By id DESC ")
    fun fetchAll():Flow<List<HistoryEntity>>
}
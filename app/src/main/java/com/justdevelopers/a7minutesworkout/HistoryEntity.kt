package com.justdevelopers.a7minutesworkout

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history-table")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    @ColumnInfo(name="created-at")
    val createdAt: String="",

)

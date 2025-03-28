package com.example.mulga.domain.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "apps",
    indices = [Index(value=["appName"], unique = true)]
)
data class AppEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val appName: String
)

package com.example.mulga.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val appName: String?,
    val title: String?,
    val content: String?,
    val timestamp: String,
    val retryCount: Int = 0
)

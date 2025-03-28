package com.example.mulga.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "notifications",
    foreignKeys = [ForeignKey(
        entity = AppEntity::class,
        parentColumns = ["id"],
        childColumns = ["appId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["appId"])]
)
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val appId: Long,
    val appName: String,
    val title: String?,
    val content: String?,
    val timestamp: String
)

package com.example.mulga.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mulga.domain.model.NotificationEntity

@Database(entities = [NotificationEntity::class], version = 5)
abstract class NotificationDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationDao
}
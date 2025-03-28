package com.example.mulga.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mulga.domain.model.AppEntity
import com.example.mulga.domain.model.NotificationEntity

@Database(entities = [AppEntity::class, NotificationEntity::class], version = 3)
abstract class NotificationDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationDao
}
package com.ilm.mulga.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ilm.mulga.domain.model.NotificationEntity

@Database(entities = [NotificationEntity::class], version = 5)
abstract class NotificationDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationDao
}
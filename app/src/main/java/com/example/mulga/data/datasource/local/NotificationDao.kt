package com.example.mulga.data.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mulga.domain.model.NotificationEntity

@Dao
interface NotificationDao {
    @Insert
    suspend fun insertNotification(notification: NotificationEntity): Long

    @Query("SELECT * FROM notifications WHERE title = :title AND content = :content LIMIT 1")
    suspend fun getNotificationByTitleAndContent(title: String?, content: String?): NotificationEntity?

    @Query("SELECT * FROM notifications ORDER BY id LIMIT :limit")
    suspend fun getNotificationsBatch(limit: Int): List<NotificationEntity>

    @Query("DELETE FROM notifications WHERE id = :id")
    suspend fun deleteNotification(id: Long)

    @Query("UPDATE notifications SET retryCount = retryCount + 1 WHERE id = :id")
    suspend fun increaseRetryCount(id: Long)
}
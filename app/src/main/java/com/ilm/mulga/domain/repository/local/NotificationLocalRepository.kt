package com.ilm.mulga.domain.repository.local

import com.ilm.mulga.domain.model.NotificationEntity

interface NotificationLocalRepository {
    suspend fun insertNotification(notification: NotificationEntity): Long
    suspend fun getNotificationByTitleAndContent(title: String?, content: String?): NotificationEntity?
    suspend fun getNotificationsBatch(limit: Int): List<NotificationEntity>
    suspend fun deleteNotification(id: Long)
    suspend fun increaseRetryCount(id: Long)
}
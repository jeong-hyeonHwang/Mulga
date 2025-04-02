package com.ilm.mulga.data.repository.local

import com.ilm.mulga.data.datasource.local.NotificationDao
import com.ilm.mulga.domain.model.NotificationEntity
import com.ilm.mulga.domain.repository.local.NotificationLocalRepository

class NotificationLocalRepositoryImpl(
    private val dao: NotificationDao
) : NotificationLocalRepository {
    override suspend fun insertNotification(notification: NotificationEntity) = dao.insertNotification(notification)
    override suspend fun getNotificationByTitleAndContent(title: String?, content: String?) = dao.getNotificationByTitleAndContent(title, content)
    override suspend fun getNotificationsBatch(limit: Int) = dao.getNotificationsBatch(limit)
    override suspend fun deleteNotification(id: Long) = dao.deleteNotification(id)
    override suspend fun increaseRetryCount(id: Long) = dao.increaseRetryCount(id)
}
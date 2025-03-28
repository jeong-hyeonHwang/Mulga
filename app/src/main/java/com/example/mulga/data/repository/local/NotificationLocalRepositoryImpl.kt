package com.example.mulga.data.repository.local

import androidx.lifecycle.LiveData
import com.example.mulga.domain.model.AppEntity
import com.example.mulga.domain.model.NotificationEntity
import com.example.mulga.domain.repository.local.NotificationLocalRepository
import com.example.mulga.data.datasource.local.NotificationDao

class NotificationLocalRepositoryImpl(
    private val dao: NotificationDao
) : NotificationLocalRepository {
    override suspend fun insertApp(app: AppEntity): Long = dao.insertApp(app)
    override suspend fun insertNotification(notification: NotificationEntity): Long = dao.insertNotification(notification)
    override suspend fun getAppByName(appName: String): AppEntity? = dao.getAppByName(appName)
    override fun getAllApps(): LiveData<List<AppEntity>> = dao.getAllApps()
    override fun getNotificationsByAppId(appId: Long): LiveData<List<NotificationEntity>> = dao.getNotificationsByAppId(appId)
    override suspend fun getNotificationByTitleAndContent(appId: Long, title: String?, content: String?): NotificationEntity? {
        return dao.getNotificationByTitleAndContent(appId, title, content)
    }
}
package com.example.mulga.domain.repository.local

import androidx.lifecycle.LiveData
import com.example.mulga.domain.model.AppEntity
import com.example.mulga.domain.model.NotificationEntity

interface NotificationLocalRepository {
    suspend fun insertApp(app: AppEntity): Long
    suspend fun insertNotification(notification: NotificationEntity): Long
    suspend fun getAppByName(appName: String): AppEntity?
    fun getAllApps(): LiveData<List<AppEntity>>
    fun getNotificationsByAppId(appId: Long): LiveData<List<NotificationEntity>>
    suspend fun getNotificationByTitleAndContent(appId: Long, title: String?, content: String?): NotificationEntity?
}
package com.example.myapplication.notification.repository

import android.content.Context
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.notification.dao.NotificationDao
import com.example.myapplication.notification.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow

class NotificationRepository(context: Context) {
    // ApplicationContext를 사용해서 싱글턴 형태의 AppDatabase 인스턴스 가져오기
    private val notificationDao: NotificationDao = AppDatabase.getInstance(context).notificationDao()

    fun getAllNotificationsFlow(): Flow<List<NotificationEntity>> {
        return notificationDao.getAllNotificationsFlow()
    }

    fun insertNotification(notification: NotificationEntity) {
        notificationDao.insert(notification)
    }
}

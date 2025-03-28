package com.example.mulga.data.datasource.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mulga.domain.model.AppEntity
import com.example.mulga.domain.model.NotificationEntity

@Dao
interface NotificationDao {
    @Insert
    fun insertApp(app: AppEntity): Long
    @Insert
    fun insertNotification(notification: NotificationEntity): Long
    @Query("SELECT * FROM apps ORDER BY id DESC")
    fun getAllApps(): LiveData<List<AppEntity>>
    @Query("""
        SELECT * FROM notifications n
        INNER JOIN apps a ON n.appId = a.id
        WHERE appId = :appId ORDER BY n.id DESC
    """)
    fun getNotificationsByAppId(appId: Long): LiveData<List<NotificationEntity>>
    @Query("SELECT * FROM apps WHERE appName = :appName LIMIT 1")
    fun getAppByName(appName: String): AppEntity?

    @Query("""
        SELECT * FROM notifications 
        WHERE appId = :appId AND title = :title AND content = :content 
        LIMIT 1
    """)
    suspend fun getNotificationByTitleAndContent(appId: Long, title: String?, content: String?): NotificationEntity?

}
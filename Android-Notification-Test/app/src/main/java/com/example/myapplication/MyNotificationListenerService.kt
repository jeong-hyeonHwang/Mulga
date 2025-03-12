package com.example.myapplication

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.notification.entity.NotificationEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyNotificationListenerService : NotificationListenerService() {

    companion object {
        private const val TAG = "MyNotifListener"
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        // 외부 앱에서 알림이 게시되었을 때 호출됩니다.
        val packageName = sbn.packageName
        val extras = sbn.notification.extras
        val notificationTitle = extras.getCharSequence("android.title")?.toString() ?: "제목 없음"
        val notificationText = extras.getCharSequence("android.text")?.toString() ?: "내용 없음"
        val timestamp = System.currentTimeMillis()

        Log.i(TAG, extras.toString())
        Log.i(TAG, "알림 수신됨: $packageName")
        Log.i(TAG, "제목: $notificationTitle")
        Log.i(TAG, "내용: $notificationText")

        // Notification Entity Creation
        val notification = NotificationEntity(
            packageName = packageName,
            title = notificationTitle,
            content = notificationText,
            timestamp = timestamp
        )

        // Room 데이터베이스 인스턴스 가져오기
        val db = AppDatabase.getInstance(applicationContext)

        // 백그라운드에서 알림 데이터 저장
        CoroutineScope(Dispatchers.IO).launch {
            db.notificationDao().insert(notification)
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        // 알림이 제거되었을 때 호출됩니다.
        Log.i(TAG, "알림 제거됨: ${sbn.packageName}")
    }


}

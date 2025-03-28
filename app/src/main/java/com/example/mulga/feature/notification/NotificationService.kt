package com.example.mulga.feature.notification

import android.content.pm.PackageManager
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.example.mulga.domain.model.AppEntity
import com.example.mulga.domain.model.NotificationEntity
import com.example.mulga.domain.repository.local.NotificationLocalRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationService : NotificationListenerService(), KoinComponent {
    private val repository: NotificationLocalRepository by inject()

    override fun onNotificationPosted(sbn: StatusBarNotification) {
//        Log.d("🔔NotificationService", "Notification received from: ${sbn.packageName}")

        val notification = sbn.notification
        val extras = notification.extras
        val title = extras.getCharSequence("android.title")?.toString() ?: ""
        val text = extras.getCharSequence("android.text")?.toString() ?: ""


        val appName = try {
            val pm = packageManager
            val applicationInfo = pm.getApplicationInfo(sbn.packageName, 0)
            pm.getApplicationLabel(applicationInfo).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            val pName = sbn.packageName
            if (pName.startsWith("com.")) {
                val split = pName.split(".")
                if (split.size > 1) split[1] else pName
            } else {
                pName
            }
        }

//        Log.d("🔔NotificationService", "appName: $appName, title: $title, text: $text")
        CoroutineScope(Dispatchers.IO).launch {
            var appEntity = repository.getAppByName(appName)
            if (appEntity == null) {
                val newAppId = repository.insertApp(AppEntity(appName = appName))
                appEntity = AppEntity(newAppId, appName)
            }


            val existing = repository.getNotificationByTitleAndContent(appEntity.id, title, text)
            if (existing != null) {
//                Log.d("🔔NotificationService", "중복 알림으로 저장하지 않음: $title")
                return@launch
            }

            val formattedTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(
                Date()
            )
            val entity = NotificationEntity(
                appId = appEntity.id,
                appName = appName,
                title = title,
                content = text,
                timestamp = formattedTime
            )
            repository.insertNotification(entity)
//            Log.d("📦DB", "Notification saved: $entity")

        }
    }
}
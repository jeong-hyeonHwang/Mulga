package com.example.mulga.feature.notification

import android.content.pm.PackageManager
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
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
import java.util.TimeZone

class NotificationService : NotificationListenerService(), KoinComponent {
    private val repository: NotificationLocalRepository by inject()

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val notification = sbn.notification
        val extras = notification.extras
        val title = extras.getCharSequence("android.title")?.toString() ?: ""
        val text = extras.getCharSequence("android.text")?.toString() ?: ""

        if (title.isBlank() && text.isBlank()) {
            Log.d("🔁중복 필터", "빈 알림 → 저장하지 않음")
            return
        }

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

        if (appName == "시스템 UI") {
            return
        }

        val formattedTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.format(Date())

        Log.d("🔔NotificationService", "appName: $appName, title: $title, text: $text, time: $formattedTime")

        CoroutineScope(Dispatchers.IO).launch {
            val existing = repository.getNotificationByTitleAndContent(title, text)
            if (existing != null) {
                Log.d("🔁중복 필터", "중복 알림 → 저장하지 않음")
                return@launch
            }

            val entity = NotificationEntity(
                appName = appName,
                title = title,
                content = text,
                timestamp = formattedTime
            )
            repository.insertNotification(entity)
            Log.d("📦DB", "Notification saved: $entity")
        }
    }
}
package com.ilm.mulga.util.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.graphics.drawable.toBitmap
import com.ilm.mulga.data.repository.AppRepository
import com.ilm.mulga.domain.model.AppInfoEntity
import java.io.File
import java.io.FileInputStream
import java.lang.ref.WeakReference

object AppIconManager {
    private var appMatcher: AppMatcherExtention? = null
    private var appsList: List<AppInfoEntity> = emptyList()
    private var applicationContextRef: WeakReference<Context>? = null

    fun initialize(context: Context) {
        // Application Context만 저장 (메모리 누수 방지)
        val appContext = context.applicationContext
        applicationContextRef = WeakReference(appContext)

        val appRepository = AppRepository(appContext)
        appRepository.loadInstalledApps()
        appRepository.saveAppIcons()

        // 앱 리스트를 저장하고 AppMatcher 초기화
        appsList = appRepository.getAppsList()
        appMatcher = AppMatcherExtention(appsList)
    }

    fun findMostSimilarApp(query: String): AppInfoEntity? {
        return appMatcher?.findMostSimilarApp(query)
    }

    fun getAppIconBitmap(packageName: String): Bitmap? {
        val context = applicationContextRef?.get() ?: return null

        // 저장된 아이콘 파일 확인
        val iconFile = File(context.filesDir, "app_icons/${packageName}.png")
        if (iconFile.exists()) {
            try {
                return BitmapFactory.decodeStream(FileInputStream(iconFile))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // 패키지 매니저에서 직접 가져오기
        try {
            context.packageManager?.getApplicationIcon(packageName)?.let { drawable ->
                return drawable.toBitmap()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    // 메모리 정리를 위한 클리어 메서드
    fun clear() {
        appMatcher = null
        appsList = emptyList()
        applicationContextRef = null
    }
}
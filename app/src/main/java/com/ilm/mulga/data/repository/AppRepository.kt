package com.ilm.mulga.data.repository

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.core.graphics.drawable.toBitmap
import com.ilm.mulga.domain.model.AppInfoEntity
import java.io.File
import java.io.FileOutputStream

class AppRepository(private val context: Context) {

    private val appsList = mutableListOf<AppInfoEntity>()

    // 설치된 모든 앱 정보 로드
    fun loadInstalledApps() {
        val pm = context.packageManager
        val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)

        for (packageInfo in packages) {
            try {
                // 시스템 앱이 아니면 추가 (FLAG_SYSTEM이 설정되어 있지 않은 경우)
                if (packageInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
                    val appName = pm.getApplicationLabel(packageInfo).toString()
                    val appIcon = pm.getApplicationIcon(packageInfo.packageName)

                    appsList.add(AppInfoEntity(packageInfo.packageName, appName, appIcon))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // 앱 아이콘 및 이름을 내부 저장소에 저장
    fun saveAppIcons() {
        val appIconDir = File(context.filesDir, "app_icons")
        if (!appIconDir.exists()) {
            appIconDir.mkdirs()
        }

        for (app in appsList) {
            try {
                // 아이콘을 비트맵으로 변환하여 파일로 저장
                val iconFile = File(appIconDir, "${app.packageName}.png")
                val out = FileOutputStream(iconFile)
                app.appIcon.toBitmap().compress(android.graphics.Bitmap.CompressFormat.PNG, 100, out)
                out.flush()
                out.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // 앱 목록 반환
    fun getAppsList(): List<AppInfoEntity> {
        return appsList
    }
}
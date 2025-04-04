package com.ilm.mulga

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ilm.mulga.feature.component.main.MainScreen
import com.ilm.mulga.feature.login.LoginScreen
import com.ilm.mulga.ui.theme.MulGaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 알림 권한 확인 → 없으면 설정으로 이동
        if (!isNotificationServiceEnabled()) {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
        }
        
        enableEdgeToEdge()
        setContent {
            MulGaTheme {
                // 로그인 상태를 판단하는 예시 변수 (실제 구현에서는 ViewModel이나 Repository에서 처리)
                var isLoggedIn by remember { mutableStateOf(false) }

                // 조건에 따라 화면 전환
                if (isLoggedIn) {
                    // 로그인된 경우: 네비게이션이 있는 메인 화면으로 이동
                    MainScreen()
                } else {
                    // 로그인되지 않은 경우: 로그인 화면 표시
                    LoginScreen(onLoginSuccess = { isLoggedIn = true })
                }
            }
        }
    }

    private fun isNotificationServiceEnabled(): Boolean {
        val flat = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        return flat?.contains(packageName) == true
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MulGaTheme {
        LoginScreen(onLoginSuccess = {})
    }
}

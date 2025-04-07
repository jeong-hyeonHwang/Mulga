package com.ilm.mulga

import MainScreen
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ilm.mulga.data.network.RetrofitClient
import com.ilm.mulga.feature.component.dialog.GlobalErrorDialog
import com.ilm.mulga.feature.login.LoginScreen
import com.ilm.mulga.feature.login.LoginUiState
import com.ilm.mulga.ui.theme.MulGaTheme
import com.ilm.mulga.util.handler.GlobalErrorHandler
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 알림 권한 확인 → 없으면 설정으로 이동
//        if (!isNotificationServiceEnabled()) {
//            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS).apply {
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            })
//        }
        
        enableEdgeToEdge()
        setContent {
            MulGaTheme {
                GlobalErrorDialog()

                // LoginViewModel을 koin을 통해 주입받음
                val loginViewModel: com.ilm.mulga.feature.login.LoginViewModel = koinViewModel()
                val uiState by loginViewModel.uiState.collectAsState()

                when (uiState) {
                    is LoginUiState.Success -> {
                        // Firebase에서 로그인 상태면 MainScreen 표시
                        MainScreen()
                    }
                    is LoginUiState.NotLoggedIn,
                    is LoginUiState.Initial,
                    is LoginUiState.Error -> {
                        // 로그인되지 않은 상태면 LoginScreen 표시
                        LoginScreen(onLoginSuccess = {
                            // 실제 로그인은 ViewModel의 signInWithCredential 호출로 진행
                            // 예시: 구글 로그인 버튼 클릭 시 Google 로그인 플로우를 시작하고,
                            // 성공 시 signInWithCredential을 호출하도록 처리합니다.
                        })
                    }
                    is LoginUiState.Loading -> {
                        // 로딩 상태일 때 로딩 화면을 표시하거나 ProgressIndicator를 띄울 수 있음
                        // LoadingScreen()
                    }
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

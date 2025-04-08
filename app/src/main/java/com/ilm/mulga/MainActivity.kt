package com.ilm.mulga

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ilm.mulga.feature.component.dialog.GlobalErrorDialog
import com.ilm.mulga.feature.component.main.MainScreen
import com.ilm.mulga.feature.login.LoginBudgetScreen
import com.ilm.mulga.feature.login.LoginScreen
import com.ilm.mulga.feature.login.LoginUiState
import com.ilm.mulga.feature.login.UserState
import com.ilm.mulga.feature.transaction_detail.TransactionAddScreen
import com.ilm.mulga.ui.theme.MulGaTheme
import org.koin.androidx.compose.koinViewModel

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
                GlobalErrorDialog()
                // LoginViewModel을 koin을 통해 주입받음
                val loginViewModel: com.ilm.mulga.feature.login.LoginViewModel = koinViewModel()
                val uiState by loginViewModel.uiState.collectAsState()
                val userState by loginViewModel.userState.collectAsState()
                val rootNavController = rememberNavController()

                when (uiState) {
                    is LoginUiState.Loading -> {
                        // 로딩 상태일 때 로딩 화면을 표시하거나 ProgressIndicator를 띄울 수 있음
                        // LoadingScreen()
                    }
                    is LoginUiState.NotLoggedIn,
                    is LoginUiState.Initial,
                    is LoginUiState.Error -> {
                        // 로그인되지 않은 상태면 LoginScreen 표시
                        LoginScreen()
                    }
                    is LoginUiState.Success -> {
                        // Firebase 로그인 성공 상태일 경우
                        // userState 값에 따라 분기 처리
                        when (userState) {
                            is UserState.NotExists -> {
                                // 회원가입이 필요한 경우
                                LoginBudgetScreen((uiState as LoginUiState.Success).user.name, (uiState as LoginUiState.Success).user.email)
                            }

                            is UserState.Exists -> {
                                // 기존 사용자면 홈 화면으로 이동
                                NavHost(navController = rootNavController, startDestination = "main") {
                                    composable("main") {
                                        MainScreen(
                                            onNavigateToTransactionAdd = {
                                                rootNavController.navigate("transaction_add")
                                            }
                                        )
                                    }
                                    composable("transaction_add") {
                                        TransactionAddScreen(navController = rootNavController)
                                    }
                                }
                            }

                            else -> {
                                // 초기 상태 등 기타 상황이면 기본 로그인 화면 표시
                                LoginScreen()
                            }
                        }
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
        LoginScreen()
    }
}

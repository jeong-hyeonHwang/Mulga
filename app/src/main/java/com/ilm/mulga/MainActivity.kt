package com.ilm.mulga

import TransactionDetailScreen
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ilm.mulga.feature.component.dialog.GlobalErrorDialog
import com.ilm.mulga.feature.component.main.MainScreen
import com.ilm.mulga.feature.login.LoginBudgetScreen
import com.ilm.mulga.feature.login.LoginScreen
import com.ilm.mulga.feature.login.LoginUiState
import com.ilm.mulga.feature.login.LoginViewModel
import com.ilm.mulga.feature.login.SplashScreen
import com.ilm.mulga.feature.login.UserState
import com.ilm.mulga.feature.local.LocalNavController
import com.ilm.mulga.feature.transaction_detail.TransactionAddScreen
import com.ilm.mulga.feature.transaction_detail.TransactionDetailViewModel
import com.ilm.mulga.presentation.model.TransactionDetailData
import com.ilm.mulga.ui.theme.MulGaTheme
import kotlinx.serialization.json.Json
import org.koin.androidx.compose.koinViewModel
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

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

                // 최상위 NavController 생성 및 제공
                val rootNavController = rememberNavController()
                CompositionLocalProvider(LocalNavController provides rootNavController) {
                    // 예시: LoginViewModel 및 NavHost 구성
                    val loginViewModel: LoginViewModel = koinViewModel()
                    val uiState by loginViewModel.uiState.collectAsState()
                    val userState by loginViewModel.userState.collectAsState()

                    // SplashScreen 표시 여부를 제어하는 상태
                    val showSplash by loginViewModel.showSplash.collectAsState()

                    when {
                        // SplashScreen을 표시해야 하는 경우
                        showSplash -> {
                            SplashScreen(onSplashComplete = {
                                loginViewModel.hideSplash()
                            })
                        }
                        // SplashScreen이 끝난 후 실제 상태에 따른 화면 표시
                        uiState is LoginUiState.NotLoggedIn || uiState is LoginUiState.Error -> {
                            LoginScreen()
                        }
                        uiState is LoginUiState.Success -> {
                            when (userState) {
                                is UserState.NotExists -> {
                                    LoginBudgetScreen(
                                        (uiState as LoginUiState.Success).user.name,
                                        (uiState as LoginUiState.Success).user.email
                                    )
                                }
                                is UserState.Exists -> {
                                    NavHost(
                                        navController = rootNavController,
                                        startDestination = "main"
                                    ) {
                                        composable("main") {
                                            MainScreen(
                                                onNavigateToTransactionAdd = {
                                                    rootNavController.navigate("transaction_add")
                                                },
                                                rootNavController = rootNavController
                                            )
                                        }
                                        composable("transaction_add") {
                                            TransactionAddScreen(navController = rootNavController)
                                        }
                                        composable("transaction_edit/{id}") { backStackEntry ->
                                            val id = backStackEntry.arguments?.getString("id")

                                            // ✅ JSON 문자열 불러오기
                                            val json =
                                                rootNavController.previousBackStackEntry?.savedStateHandle?.get<String>(
                                                    "editDataJson"
                                                )

                                            // ✅ JSON → 객체 변환
                                            val initialData = json?.let {
                                                Json.decodeFromString<TransactionDetailData>(it)
                                            }

                                            TransactionAddScreen(
                                                navController = rootNavController,
                                                transactionId = id,
                                                initialData = initialData
                                            )
                                        }
                                        composable(
                                            route = "transaction_detail?data={data}",
                                            arguments = listOf(navArgument("data") {
                                                type = NavType.StringType
                                            })
                                        ) { backStackEntry ->
                                            val dataParam =
                                                backStackEntry.arguments?.getString("data")
                                            val transactionDetailData = dataParam?.let {
                                                val decodedData = URLDecoder.decode(
                                                    it,
                                                    StandardCharsets.UTF_8.toString()
                                                )
                                                Json.decodeFromString<TransactionDetailData>(
                                                    decodedData
                                                )
                                            }
                                            val viewModel: TransactionDetailViewModel =
                                                koinViewModel()
                                            if (transactionDetailData != null && viewModel.transactionDetailData.value == null) {
                                                viewModel.setTransactionDetail(transactionDetailData)
                                            }
                                            TransactionDetailScreen(
                                                viewModel = viewModel,
                                                navController = rootNavController,
                                                rootNavController = rootNavController
                                            )
                                        }
                                    }
                                }
                                else -> {
                                    LoginScreen()
                                }
                            }
                        }
                        else -> {
                            // Loading 상태도 SplashScreen에 포함되므로 여기는 비워둠
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

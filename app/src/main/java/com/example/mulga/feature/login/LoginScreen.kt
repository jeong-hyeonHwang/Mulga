package com.example.mulga.feature.login

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    // 간단한 로그인 화면 placeholder.
    // 실제로는 로그인 폼이나 소셜 로그인 버튼 등이 들어가겠지만, 여기서는 버튼 클릭 시 로그인 성공으로 처리.
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Button(
            onClick = onLoginSuccess,
            modifier = Modifier.padding(innerPadding)
        ) {
            Text(text = "로그인하기")
        }
    }
}
package com.ilm.mulga.feature.mypage

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ilm.mulga.feature.login.LoginViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MypageScreen(
    viewModel: MypageViewModel = koinViewModel()
) {
    Column {
        Text(text = "마이페이지 화면")
        Button(onClick = { viewModel.logout() }) {
            Text(text = "로그아웃")
        }
    }
}
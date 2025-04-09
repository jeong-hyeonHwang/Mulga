package com.ilm.mulga.feature.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilm.mulga.R
import com.ilm.mulga.ui.theme.MulGaTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashComplete: () -> Unit = {}) {
    // 2초 후에 콜백 호출
    LaunchedEffect(Unit) {
        delay(800)
        onSplashComplete()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MulGaTheme.colors.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))
        // 로고 이미지
        Image(
            painter = painterResource(id = R.drawable.ic_logo_mulga),
            contentDescription = "물가 로고",
            modifier = Modifier
                .size(102.dp)
        )

        // 아이콘과 텍스트 사이 여백
        Spacer(modifier = Modifier.height(16.dp))

        // 앱 이름, 설명
        Text(
            text = stringResource(id = R.string.app_name),
            // 여러 줄 사용 가능 (예: \n으로 줄바꿈)
            textAlign = TextAlign.Center,
            style = MulGaTheme.typography.appTitle,
            // 스타일이나 폰트 사이즈는 원하는대로 변경
            color = MulGaTheme.colors.white1
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.app_subtitle),
            // 여러 줄 사용 가능 (예: \n으로 줄바꿈)
            textAlign = TextAlign.Center,
            style = MulGaTheme.typography.appSubTitle,
            // 스타일이나 폰트 사이즈는 원하는대로 변경
            color = MulGaTheme.colors.white1
        )
        Spacer(modifier = Modifier.weight(2f))
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}

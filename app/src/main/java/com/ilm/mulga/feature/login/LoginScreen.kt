package com.ilm.mulga.feature.login

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.ilm.mulga.R
import com.ilm.mulga.ui.theme.MulGaTheme
import org.koin.androidx.compose.koinViewModel


@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel()
) {
    val context = LocalContext.current

    val userState by viewModel.userState.collectAsState()

    //GoogleSignInClient 객체 초기화
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN) //기본 로그인 방식 사용
        .requestIdToken(context.getString(R.string.web_client_id))
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, gso) // this 부분엔 context 값이 들어감

    // Google 로그인 인텐트를 실행하기 위한 ActivityResultLauncher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account?.idToken
                if (idToken != null) {
                    // Google 자격증명 생성 후 Firebase 로그인 시도
                    val credential = GoogleAuthProvider.getCredential(idToken, null)
                    viewModel.signInWithCredential(credential)
                } else {
                    Log.e("LoginScreen", "ID 토큰이 null입니다.")
                    // 사용자에게 오류 메시지 표시
                }
            } catch (e: ApiException) {
                // 오류 코드에 따른 적절한 처리
                when (e.statusCode) {
                    GoogleSignInStatusCodes.SIGN_IN_CANCELLED ->
                        Log.d("LoginScreen", "로그인이 취소되었습니다.")
                    GoogleSignInStatusCodes.NETWORK_ERROR ->
                        Log.e("LoginScreen", "네트워크 오류가 발생했습니다.")
                    else ->
                        Log.e("LoginScreen", "로그인 실패: 오류 코드 ${e.statusCode}", e)
                }
                // 사용자에게 적절한 오류 메시지 표시
            }
        } else {
            Log.d("LoginScreen", "로그인 취소됨: resultCode = ${result.resultCode}")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
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
            color = MulGaTheme.colors.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.app_subtitle),
            // 여러 줄 사용 가능 (예: \n으로 줄바꿈)
            textAlign = TextAlign.Center,
            style = MulGaTheme.typography.appSubTitle,
            // 스타일이나 폰트 사이즈는 원하는대로 변경
            color = MulGaTheme.colors.primary
        )
        Spacer(modifier = Modifier.height(200.dp))

        // Google 로그인 버튼 클릭 시 Google 로그인 플로우 시작
        GoogleLoginButton(onClick = {
            launcher.launch(googleSignInClient.signInIntent)
        })
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}

package com.ilm.mulga.feature.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilm.mulga.ui.theme.MulGaTheme
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ilm.mulga.R
import com.ilm.mulga.data.network.RetrofitClient
import com.ilm.mulga.data.repository.UserRepository
import com.ilm.mulga.feature.component.NormalButton
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginBudgetScreen(
    userName: String = "000",     // 사용자 이름
    userEmail: String
) {
    val loginViewModel: LoginViewModel = koinViewModel<LoginViewModel>()

    // 숫자만 입력 받기 위한 상태
    var budget by remember { mutableStateOf("1000000") }

    // 포커스 제어용
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            // 키보드가 올라왔을 때 버튼이 가려지지 않도록 해주는 Modifier
            .imePadding()
            .padding(horizontal = 24.dp, vertical = 0.dp)
    ) {
        // 상단 문구 + 예산 입력 필드
        // 상단 여백
        Spacer(modifier = Modifier.weight(4f))
        // 인사말
        Text(
            text = stringResource(id = R.string.budget_greeting, userName),
            style = MulGaTheme.typography.title,
            textAlign = TextAlign.Left
        )

        // 중단 여백
        Spacer(modifier = Modifier.weight(2f))

        // 예산 입력 필드
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // TextField
            TextField(
                value = budget,
                onValueChange = { newValue ->
                    val filtered = newValue.filter { it.isDigit() }
                    budget = filtered
                },
                textStyle = MulGaTheme.typography.title,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                // 밑줄만 표시하고 싶다면 TextFieldDefaults를 사용
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MulGaTheme.colors.transparent,
                    unfocusedIndicatorColor = MulGaTheme.colors.grey3,
                    unfocusedTextColor = MulGaTheme.colors.grey3,
                    focusedContainerColor = MulGaTheme.colors.transparent,
                    focusedIndicatorColor = MulGaTheme.colors.grey1,
                    focusedTextColor = MulGaTheme.colors.black1,
                ),
                visualTransformation = { text ->
                    numberCommaTransformation(text)
                },
                modifier = Modifier
                    .width(240.dp)
                    .padding(horizontal = 12.dp)
            )

            // TextField 오른쪽에 "원" 표시
            Spacer(modifier = Modifier.width(16.dp)) // 간격
            Text(text = "원", style = MulGaTheme.typography.title)
        }


        // 하단 여백
        Spacer(modifier = Modifier.weight(6f))

        // 시작하기 버튼
        NormalButton(
            text = "시작하기",
            onClick = {
                loginViewModel.performSignup(userName, userEmail, budget.replace(",", "").toInt())
            },
        )

        // 여백?
        Spacer(modifier = Modifier.weight(1.5f))
    }
}

/**
 * 숫자에 콤마(,)를 간단히 넣어주는 VisualTransformation
 */
fun numberCommaTransformation(text: AnnotatedString): TransformedText {
    val originalText = text.text
    // 1) 숫자를 세 자리마다 콤마 삽입
    val withCommas = originalText.reversed()
        .chunked(3)
        .joinToString(",")
        .reversed()

    // 2) 오프셋 매핑
    val numberOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            // offset 기준으로 콤마가 몇 개 들어갔는지 계산
            val commaCount = (0 until offset).count {
                (offset - it) % 3 == 0 && it != 0
            }
            return offset + commaCount
        }

        override fun transformedToOriginal(offset: Int): Int {
            // 실제 문자의 위치 계산(단순 예시)
            // 여기서는 정밀 계산보다는 대략적으로만 맞춰주는 용도로 작성
            return withCommas.filter { it != ',' }
                .take(offset).length
        }
    }

    return TransformedText(AnnotatedString(withCommas), numberOffsetTranslator)
}

//@Preview(showBackground = true)
//@Composable
//fun LoginBudgetScreenPreview() {
//    MulGaTheme {
//        LoginBudgetScreen(userName = "홍길동")
//    }
//}
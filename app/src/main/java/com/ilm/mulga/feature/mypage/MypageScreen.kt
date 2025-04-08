package com.ilm.mulga.feature.mypage

import DeleteConfirmDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilm.mulga.R
import com.ilm.mulga.data.repository.UserRepository
import com.ilm.mulga.feature.component.toggle.ToggleSwitch
import com.ilm.mulga.ui.theme.MulGaTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun MypageScreen(
    viewModel: MypageViewModel = koinViewModel()
) {
    val scrollState = rememberScrollState()

    val showLogoutConfirmDialog = remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 28.dp)
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        // 상단 사용자 정보
        UserInfoSection(viewModel.userName, viewModel.userEmail)

        HorizontalDivider(modifier = Modifier.padding(vertical = 28.dp), color = MulGaTheme.colors.grey3)

        // 설정 섹션
        SectionTitle("설정")

        // 알림 설정
        var notificationEnabled by remember { mutableStateOf(true) }
        SettingItem(
            title = "알림 설정",
            hasToggle = true,
            toggleState = notificationEnabled,
            onToggleChange = { notificationEnabled = it }
        )

        // 월 소비 금액 설정
        SettingItem(
            title = "월 소비 금액 설정",
            hasArrow = true,
            onClick = {}
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 28.dp), color = MulGaTheme.colors.grey3)

        // 약관 및 방침 섹션
        SectionTitle("약관 및 방침")

        SettingItem(
            title = "이용약관",
            hasArrow = true,
            onClick = {}
        )

        SettingItem(
            title = "개인정보처리방침",
            hasArrow = true,
            onClick = {}
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 28.dp), color = MulGaTheme.colors.grey3)

        // 계정 섹션
        SectionTitle("계정")

        SettingItem(
            title = "로그아웃",
            hasArrow = true,
            onClick = { showLogoutConfirmDialog.value = true }
        )

        SettingItem(
            title = "회원탈퇴",
            hasArrow = true,
            onClick = {}
        )
    }
    // 로그아웃 확인 다이얼로그를 조건부로 표시
    if (showLogoutConfirmDialog.value) {
        DeleteConfirmDialog (
            title = "로그아웃하시겠습니까?",
            message = "",
            actionText = "로그아웃",
            onCancel = { showLogoutConfirmDialog.value = false },
            onConfirm = {
                // 로그아웃 로직 처리. 예) UserRepository.logout() 등
                viewModel.logout()
                showLogoutConfirmDialog.value = false
            }
        )
    }
}

@Composable
fun UserInfoSection(userName: String, userEmail: String) {
    Column() {
        Text(
            text = userName,
            style = MulGaTheme.typography.headline
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = userEmail,
            style = MulGaTheme.typography.caption,
            color = MulGaTheme.colors.grey2
        )
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MulGaTheme.typography.subtitle,
    )
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
fun SettingItem(
    title: String,
    hasArrow: Boolean = false,
    hasToggle: Boolean = false,
    toggleState: Boolean = false,
    onToggleChange: (Boolean) -> Unit = {},
    onClick: () -> Unit = {}
) {
    Spacer(modifier = Modifier.height(14.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MulGaTheme.typography.bodyMedium
        )
        if (hasToggle) {
            var toggleIndex by remember { mutableStateOf(0) }
            ToggleSwitch(
                modifier = Modifier,
                selectedIndex = toggleIndex,
                onOptionSelected = { toggleIndex = 1 - toggleIndex },
                firstLabel = "ON",
                secondLabel = "OFF"
            )
        } else if (hasArrow) {
            Image(
                painter = painterResource(id =R.drawable.ic_util_caret_right),
                contentDescription = "thebogi icon",
                modifier = Modifier.size(12.dp)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun MyPagePreview() {
    MaterialTheme {
        MypageScreen()
    }
}
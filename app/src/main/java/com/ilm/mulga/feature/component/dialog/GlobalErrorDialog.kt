package com.ilm.mulga.feature.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.ilm.mulga.R
import com.ilm.mulga.util.handler.GlobalErrorHandler

@Composable
fun GlobalErrorDialog() {
    val errorMessage = remember { mutableStateOf<String?>(null) }

    // 전역 에러 이벤트를 구독하여 에러가 발생하면 errorMessage 업데이트
    LaunchedEffect(Unit) {
        GlobalErrorHandler.errorEvents.collect { message ->
            errorMessage.value = message
        }
    }

    // 에러 메시지가 있으면 AlertDialog 띄우기
    errorMessage.value?.let { errorCode ->
        CustomDialog(
            title = stringResource(R.string.error_title),
            message = stringResource(R.string.error_code_message, errorCode),
            onDismiss = { errorMessage.value = null },
            onConfirm = { errorMessage.value = null }
        )
    }
}

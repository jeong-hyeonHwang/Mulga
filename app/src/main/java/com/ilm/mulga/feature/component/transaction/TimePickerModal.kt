package com.ilm.mulga.feature.component.transaction

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.window.DialogProperties
import com.ilm.mulga.R
import com.ilm.mulga.ui.theme.MulGaTheme
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerModal(
    onConfirm: (LocalTime) -> Unit,
    onDismiss: () -> Unit
) {
    val state = rememberTimePickerState(is24Hour = true)

    // 앱 색상 리소스를 직접 가져옵니다
    val primaryColor = colorResource(id = R.color.primary_color)
    val grey5 = colorResource(id = R.color.grey5)
    val grey1 = colorResource(id = R.color.grey1)

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val selected = LocalTime.of(state.hour, state.minute)
                    onConfirm(selected)
                    onDismiss()
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MulGaTheme.colors.primary
                )
            ) {
                Text("확인", style = MulGaTheme.typography.bodyMedium)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = grey1
                )
            ) {
                Text("취소", style = MulGaTheme.typography.bodyMedium)
            }
        },
        title = {
            Text("시간 선택", style = MulGaTheme.typography.bodyLarge, color = grey1)
        },
        text = {
            TimeInput(state = state)
        },
        properties = DialogProperties(),
        containerColor = grey5,
        titleContentColor = grey1,
        textContentColor = grey1
    )
}
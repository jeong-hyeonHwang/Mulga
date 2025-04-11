package com.ilm.mulga.feature.component.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ilm.mulga.R
import com.ilm.mulga.ui.theme.MulGaTheme

@Composable
fun CustomDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    backgroundColor: Color
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnClickOutside = false)
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MulGaTheme.colors.white1,
            modifier = Modifier.width(360.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                // 제목
                Text(
                    text = title,
                    color = MulGaTheme.colors.black1,
                    textAlign = TextAlign.Start,
                    style = MulGaTheme.typography.subtitle,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )
                // 메시지
                Text(
                    text = message,
                    color = MulGaTheme.colors.black1,
                    style = MulGaTheme.typography.bodySmall,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(48.dp))
                // 단일 버튼 (확인 버튼)
                TextButton(
                    onClick = onConfirm,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = backgroundColor,
                        contentColor = MulGaTheme.colors.white1
                    )
                ) {
                    Text(
                        text = stringResource(R.string.btn_title_confirm),
                        style = MulGaTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

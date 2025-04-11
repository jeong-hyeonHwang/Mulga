package com.ilm.mulga.feature.component.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ilm.mulga.R
import com.ilm.mulga.ui.theme.MulGaTheme

@Composable
fun CustomDestructiveDialogWithCancel(
    title: String,
    message: String,
    actionText: String,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(dismissOnClickOutside = true)
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MulGaTheme.colors.white1,
            modifier = Modifier.width(380.dp)
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

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onConfirm,
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = MulGaTheme.colors.red1,
                            contentColor = MulGaTheme.colors.white1
                        )
                    ) {
                        Text(
                            text = actionText,
                            style = MulGaTheme.typography.bodySmall
                        )
                    }


                    OutlinedButton(
                        onClick = onCancel,
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, MulGaTheme.colors.grey3),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MulGaTheme.colors.grey1
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.btn_title_cancel),
                            style = MulGaTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

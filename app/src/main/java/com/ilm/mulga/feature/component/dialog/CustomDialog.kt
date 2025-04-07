package com.ilm.mulga.feature.component.dialog

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(dismissOnClickOutside = false)) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MulGaTheme.colors.grey5,
            modifier = Modifier.width(300.dp)
        ) {
            Column(modifier = Modifier
                .padding(16.dp)) {
                Text(text = title,
                    color = MulGaTheme.colors.black1,
                    textAlign = TextAlign.Center,
                    style = MulGaTheme.typography.subtitle
                )
                Spacer(modifier = Modifier.height(36.dp))
                Text(text = message,
                    color = MulGaTheme.colors.black1
                )
                Spacer(modifier = Modifier.height(24.dp))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.BottomEnd
                ) {

                    val interactionSource = remember { MutableInteractionSource() }
                    val isPressed by interactionSource.collectIsPressedAsState()

                    TextButton(
                        onClick = onConfirm,
                        interactionSource = interactionSource,
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = if (isPressed)  MulGaTheme.colors.primary else Color.Transparent,
                            contentColor = if (isPressed) MulGaTheme.colors.white1 else MulGaTheme.colors.black1
                        )
                    ) {
                        Text(text = stringResource(R.string.btn_title_confirm))
                    }
                }
            }
        }
    }
}

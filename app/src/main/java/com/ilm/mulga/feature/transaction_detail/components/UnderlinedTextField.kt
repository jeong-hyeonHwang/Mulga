package com.ilm.mulga.feature.transaction_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.ilm.mulga.ui.theme.MulGaTheme


@Composable
fun UnderlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    textStyle: TextStyle = MulGaTheme.typography.bodyLarge,
    errorText: String? = null

) {
    Column(modifier = modifier) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = singleLine,
            textStyle = textStyle,
            decorationBox = { innerTextField ->
                Box {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = MulGaTheme.colors.grey2,
                            style = textStyle)
                    }
                    innerTextField()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MulGaTheme.colors.grey3)
        )
        if (!errorText.isNullOrBlank()) {
            Text(
                text = errorText,
                color = MulGaTheme.colors.red1,
                style = MulGaTheme.typography.caption,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

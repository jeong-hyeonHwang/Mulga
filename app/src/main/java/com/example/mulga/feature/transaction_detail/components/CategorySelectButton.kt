package com.example.mulga.feature.transaction_detail.components

import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import com.ilm.mulga.presentation.model.type.Category
import com.ilm.mulga.ui.theme.MulGaTheme

@Composable
fun CategorySelectButton(
    category: Category?,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        colors = ButtonDefaults.textButtonColors(contentColor = MulGaTheme.colors.white1)
    ) {
        Text(
            text = category?.displayName ?: "카테고리",
            color = MulGaTheme.colors.black1,
            style = MulGaTheme.typography.bodyMedium
        )
    }
}

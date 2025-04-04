package com.ilm.mulga.feature.transaction_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.ilm.mulga.feature.transaction_detail.components.CategorySelectButton
import com.ilm.mulga.features.category.components.CategoryModal
import org.koin.androidx.compose.koinViewModel


@Composable
fun TransactionDetailScreen(
    viewModel: TransactionDetailViewModel = koinViewModel()
) {
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    var isModalVisible by remember { mutableStateOf(false) }

    Column {
        // 선택된 카테고리 표시하는 버튼
        CategorySelectButton(
            category = selectedCategory,
            onClick = { isModalVisible = true }
        )
    }

    if (isModalVisible) {
        CategoryModal(
            selectedCategory = selectedCategory,
            onCategorySelected = { viewModel.updateCategory(it) },
            onDismiss = { isModalVisible = false }
        )
    }
}
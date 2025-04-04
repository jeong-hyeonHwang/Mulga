package com.example.mulga.feature.transaction_detail

import androidx.lifecycle.ViewModel
import com.ilm.mulga.presentation.model.type.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TransactionDetailViewModel : ViewModel() {
    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory: StateFlow<Category?> = _selectedCategory

    fun updateCategory(category: Category) {
        _selectedCategory.value = category
    }
}

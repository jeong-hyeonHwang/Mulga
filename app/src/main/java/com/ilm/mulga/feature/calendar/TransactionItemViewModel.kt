package com.ilm.mulga.feature.calendar

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TransactionItemViewModel : ViewModel() {

    // 전체 삭제 모드 상태
    private val _isDeleteMode = MutableStateFlow(false)
    val isDeleteMode: StateFlow<Boolean> = _isDeleteMode

    // 선택된 transaction ID들을 저장
    private val _selectedItemIds = MutableStateFlow<Set<String>>(emptySet())
    val selectedItemIds: StateFlow<Set<String>> = _selectedItemIds

    /**
     * 개별 아이템을 길게 누르면 삭제 모드로 진입하거나,
     * 이미 삭제 모드라면 선택 상태를 토글
     */
    fun onItemLongPress(itemId: String) {
        if (!_isDeleteMode.value) {
            // 삭제 모드가 아니었다면 삭제 모드를 켜고 선택 상태로 진입
            _isDeleteMode.value = true
            _selectedItemIds.value = setOf(itemId)
        } else {
            // 이미 삭제 모드라면 단순히 선택 상태를 토글
            toggleItemSelected(itemId)
        }
    }

    /**
     * 아이템을 클릭했을 때의 처리
     * - 이미 삭제 모드인 경우: 해당 아이템의 선택 상태만 토글
     * - 삭제 모드가 아니면: 일반 onClick 로직 (예: 상세 페이지 이동)
     */
    fun onItemClick(itemId: String, normalClickAction: () -> Unit) {
        if (_isDeleteMode.value) {
            toggleItemSelected(itemId)
        } else {
            // 삭제 모드가 아니면 원래 하던 동작(상세 페이지 이동 등) 실행
            normalClickAction()
        }
    }

    /**
     * 선택 상태를 토글
     */
    private fun toggleItemSelected(itemId: String) {
        val current = _selectedItemIds.value
        _selectedItemIds.value = if (current.contains(itemId)) {
            current - itemId
        } else {
            current + itemId
        }
    }

    /**
     * 외부에서 '삭제 모드 해제' 등을 할 때
     */
    fun clearDeleteMode() {
        _isDeleteMode.value = false
        _selectedItemIds.value = emptySet()
    }
}

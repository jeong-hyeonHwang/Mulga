package com.ilm.mulga.feature.calendar

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TransactionItemViewModel : ViewModel() {

    // 전체 액션 모드 상태
    private val _isActionMode = MutableStateFlow(false)
    val isActionMode: StateFlow<Boolean> = _isActionMode

    // 선택된 transaction ID들을 저장 (빈 LinkedHashSet으로 초기화)
    private val _selectedItemIds = MutableStateFlow(LinkedHashSet<String>())
    val selectedItemIds: StateFlow<LinkedHashSet<String>> = _selectedItemIds

    /**
     * 개별 아이템을 길게 누르면 액션 모드로 진입하거나,
     * 이미 액션 모드라면 선택 상태를 토글
     */
    fun onItemLongPress(itemId: String) {
        if (!_isActionMode.value) {
            // 액션 모드가 아니었다면 액션 모드를 켜고 해당 아이템 선택
            _isActionMode.value = true
            _selectedItemIds.value = LinkedHashSet<String>().apply { add(itemId) }
        } else {
            // 이미 액션 모드인 경우 단순히 선택 상태 토글
            toggleItemSelected(itemId)
        }
    }

    /**
     * 아이템 클릭 시 처리:
     * - 액션 모드일 경우, 선택 상태만 토글
     * - 액션 모드가 아닌 경우 정상 onClick 로직 실행
     */
    fun onItemClick(itemId: String, normalClickAction: () -> Unit) {
        if (_isActionMode.value) {
            toggleItemSelected(itemId)
        } else {
            normalClickAction()
        }
    }

    /**
     * 선택 상태를 토글하는 메서드 (새 LinkedHashSet을 만들어서 업데이트)
     */
    private fun toggleItemSelected(itemId: String) {
        // 기존 집합을 복사한 후 수정하여 할당
        val current = _selectedItemIds.value
        val newSet = LinkedHashSet(current)
        if (current.contains(itemId)) {
            newSet.remove(itemId)
        } else {
            newSet.add(itemId)
        }
        _selectedItemIds.value = newSet
    }

    /**
     * 외부에서 액션 모드를 해제할 때 사용
     */
    fun clearActionMode() {
        _isActionMode.value = false
        _selectedItemIds.value = LinkedHashSet() // 빈 LinkedHashSet 할당
    }
}

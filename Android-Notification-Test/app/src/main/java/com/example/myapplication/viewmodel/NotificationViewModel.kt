package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.notification.entity.NotificationEntity
import com.example.myapplication.notification.repository.NotificationRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotificationViewModel(private val repository: NotificationRepository) : ViewModel() {

    // Flow를 StateFlow로 변환하여 UI에서 쉽게 관찰할 수 있도록 합니다.
    val notifications: StateFlow<List<NotificationEntity>> =
        repository.getAllNotificationsFlow()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun insertNotification(notification: NotificationEntity) {
        viewModelScope.launch {
            repository.insertNotification(notification)
        }
    }
}


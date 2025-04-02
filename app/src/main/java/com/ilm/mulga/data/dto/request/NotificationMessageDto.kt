package com.ilm.mulga.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class NotificationMessageDto(
    val appName: String,
    val title: String,
    val content: String,
    val timestamp: String
)

package com.ilm.mulga.domain.model

data class UserEntity(
    val id: String,
    val name: String,
    val email: String,
    val budget: Int,
    val isWithdrawn: Boolean,
    val receivesNotification: Boolean,
)
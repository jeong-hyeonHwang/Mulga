package com.ilm.mulga.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class UserDto (
    val id: String? = "",
    val name: String? = "",
    val email: String? = "",
    val budget: Int? = 0,
    val isWithdrawn: Boolean? = false,
    val receivesNotification: Boolean? = false,
    val code: String? = ""
)
package com.ilm.mulga.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequestDto(
    val budget: Int,
    val name: String,
    val email: String
)

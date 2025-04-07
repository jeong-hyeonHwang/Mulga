package com.ilm.mulga.presentation.mapper

import com.ilm.mulga.data.dto.response.AuthDto
import com.ilm.mulga.data.dto.response.UserDto
import com.ilm.mulga.domain.model.UserEntity

fun UserDto.toDomain(): UserEntity {
    return UserEntity(
        id = id ?: "",
        name = name ?: "",
        email = email ?: "",
        budget = budget ?: 0,
        isWithdrawn = isWithdrawn ?: false,
        receivesNotification = receivesNotification ?: false,
    )
}

fun UserEntity.toDto(): AuthDto {
    return AuthDto(
        uid = id,
        name = name,
        email = email,
    )
}
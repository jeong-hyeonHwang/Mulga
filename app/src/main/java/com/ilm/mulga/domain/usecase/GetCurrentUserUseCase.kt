package com.ilm.mulga.domain.usecase

import com.ilm.mulga.data.dto.response.AuthDto
import com.ilm.mulga.domain.repository.remote.AuthRepository

class GetCurrentUserUseCase(private val authRepository: AuthRepository) {
    operator fun invoke(): AuthDto? {
        return authRepository.getCurrentUser()
    }
}

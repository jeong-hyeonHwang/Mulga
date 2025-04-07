package com.ilm.mulga.domain.usecase

import com.ilm.mulga.domain.repository.remote.AuthRepository

class LogoutUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(): Result<Unit> {
        return authRepository.signOut()
    }
}
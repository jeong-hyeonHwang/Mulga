package com.ilm.mulga.domain.usecase

import com.google.firebase.auth.AuthCredential
import com.ilm.mulga.data.dto.response.AuthDto
import com.ilm.mulga.domain.repository.remote.AuthRepository

class LoginWithCredentialUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(credential: AuthCredential): Result<AuthDto> {
        return authRepository.signInWithCredential(credential)
    }
}
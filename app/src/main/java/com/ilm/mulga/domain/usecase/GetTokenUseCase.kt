package com.ilm.mulga.domain.usecase

import com.ilm.mulga.domain.repository.local.UserLocalRepository

class GetTokenUseCase(private val repository: UserLocalRepository) {
    operator fun invoke(): String? {
        return repository.getToken()
    }
}
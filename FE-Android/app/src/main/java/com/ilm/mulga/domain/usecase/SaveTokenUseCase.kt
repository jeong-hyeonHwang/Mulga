package com.ilm.mulga.domain.usecase

import com.ilm.mulga.domain.repository.local.UserLocalRepository

class SaveTokenUseCase(private val repository: UserLocalRepository) {
    operator fun invoke(token: String) {
        repository.saveToken(token)
    }
}
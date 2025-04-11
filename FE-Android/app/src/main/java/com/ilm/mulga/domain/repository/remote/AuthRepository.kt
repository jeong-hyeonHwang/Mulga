package com.ilm.mulga.domain.repository.remote

import com.google.firebase.auth.AuthCredential
import com.ilm.mulga.data.dto.response.AuthDto

interface AuthRepository {
    suspend fun signInWithCredential(credential: AuthCredential): Result<AuthDto>

    suspend fun signOut(): Result<Unit>

    fun getCurrentUser(): AuthDto?
}
package com.ilm.mulga.presentation.mapper

import com.google.firebase.auth.FirebaseUser
import com.ilm.mulga.data.dto.response.AuthDto

fun FirebaseUser.toUser(): AuthDto {
    return AuthDto(
        uid = this.uid,
        name = this.displayName ?: "",
        email = this.email ?: "",
    )
}
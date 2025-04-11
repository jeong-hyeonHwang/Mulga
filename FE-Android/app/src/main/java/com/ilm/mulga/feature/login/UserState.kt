package com.ilm.mulga.feature.login

import com.ilm.mulga.domain.model.UserEntity

sealed class UserState {
    object Initial : UserState()
    object Loading : UserState()
    object NotExists : UserState() // 회원가입 필요
    data class Exists(val user: UserEntity) : UserState() // 기존 사용자
    data class Error(val message: String) : UserState()
}
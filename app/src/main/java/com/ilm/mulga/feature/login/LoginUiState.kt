package com.ilm.mulga.feature.login

import com.ilm.mulga.data.dto.response.AuthDto

sealed class LoginUiState {
    object Initial : LoginUiState()
    object Loading : LoginUiState()
    object NotLoggedIn : LoginUiState()
    data class Success(val user: AuthDto) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}
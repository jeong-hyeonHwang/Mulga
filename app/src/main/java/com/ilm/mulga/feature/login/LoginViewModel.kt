package com.ilm.mulga.feature.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.ilm.mulga.domain.usecase.GetCurrentUserUseCase
import com.ilm.mulga.domain.usecase.LoginWithCredentialUseCase
import com.ilm.mulga.domain.usecase.LogoutUseCase
import com.ilm.mulga.presentation.mapper.toUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val firebaseAuth: FirebaseAuth,
    private val signInWithCredentialUseCase: LoginWithCredentialUseCase,
    private val signOutUseCase: LogoutUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Initial)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // Firebase 인증 상태 리스너 추가 (자동 토큰 갱신도 내부적으로 처리됨)
    private val authStateListener = FirebaseAuth.AuthStateListener { auth ->
        val firebaseUser = auth.currentUser
        if (firebaseUser != null) {
            // 로그인 상태이면 자동으로 최신 토큰을 관리함
            _uiState.value = LoginUiState.Success(firebaseUser.toUser())
        } else {
            _uiState.value = LoginUiState.NotLoggedIn
        }
    }

    init {
        firebaseAuth.addAuthStateListener(authStateListener)
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        val currentUser = getCurrentUserUseCase()
        if (currentUser != null) {
            _uiState.value = LoginUiState.Success(currentUser)
        } else {
            _uiState.value = LoginUiState.NotLoggedIn
        }
    }

    fun signInWithCredential(credential: AuthCredential) {
        Log.d("LoginViewModel", "로그인 시도")
        _uiState.value = LoginUiState.Loading

        viewModelScope.launch {
//            Log.d("LoginViewModel", "로그인 시도2")
            signInWithCredentialUseCase(credential)
                .onSuccess { user ->
                    _uiState.value = LoginUiState.Success(user)
                    Log.d("LoginViewModel", "로그인 응답: $user")
                    Log.d("LoginViewModel", "credential: $credential")
                    Log.d("LoginViewModel", "uistate: ${_uiState.value}")
                }
                .onFailure { error ->
                    _uiState.value = LoginUiState.Error(error.message ?: "로그인 실패")
                    Log.e("LoginViewModel", "로그인 실패: ${error.message}")
                }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            signOutUseCase()
                .onSuccess {
                    _uiState.value = LoginUiState.NotLoggedIn
                }
                .onFailure { error ->
                    _uiState.value = LoginUiState.Error(error.message ?: "로그아웃 실패")
                }
        }
    }
}

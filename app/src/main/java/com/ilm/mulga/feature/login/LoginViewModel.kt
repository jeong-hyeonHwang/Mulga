package com.ilm.mulga.feature.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.ilm.mulga.data.network.RetrofitClient
import com.ilm.mulga.data.repository.UserRepository
import com.ilm.mulga.domain.model.UserEntity
import com.ilm.mulga.domain.usecase.GetCurrentUserUseCase
import com.ilm.mulga.domain.usecase.LoginWithCredentialUseCase
import com.ilm.mulga.domain.usecase.LogoutUseCase
import com.ilm.mulga.presentation.mapper.toDto
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

    private val _userState = MutableStateFlow<UserState>(UserState.Initial)
    val userState: StateFlow<UserState> = _userState.asStateFlow()

    private val userRepository: UserRepository = UserRepository(RetrofitClient.userService)

    private val authStateListener = FirebaseAuth.AuthStateListener { auth ->
        val firebaseUser = auth.currentUser
        if (firebaseUser != null) {
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
                    checkUserExists()
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

    // 로그인 시 유저 존재 여부 확인
    private fun checkUserExists() {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            val user = userRepository.getUserData()
            if (user != null) {
                _userState.value = UserState.Exists(user)
            } else {
                _userState.value = UserState.NotExists
            }
        }
    }

    // 회원가입 완료
    fun performSignup(name: String, email: String, budget: Int) {
        // 상태를 로딩 중으로 변경
        _userState.value = UserState.Loading
        _uiState.value = LoginUiState.Loading

        viewModelScope.launch {
            try {
                val userEntity = userRepository.signup(name, email, budget)
                if (userEntity != null) {
                    // 회원가입 성공 시 UserState와 LoginUiState 업데이트
                    _userState.value = UserState.Exists(userEntity)
                    val authDto = userEntity.toDto() // 확장 함수 또는 변환 로직 필요
                    _uiState.value = LoginUiState.Success(authDto)
                } else {
                    // 실패 시 에러 상태 업데이트
                    _userState.value = UserState.Error("회원가입 실패")
                    _uiState.value = LoginUiState.Error("회원가입 실패")
                }
            } catch (e: Exception) {
                _userState.value = UserState.Error(e.message ?: "회원가입 실패")
                _uiState.value = LoginUiState.Error(e.message ?: "회원가입 실패")
            }
        }
    }
}

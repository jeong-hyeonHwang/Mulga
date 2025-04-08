package com.ilm.mulga.feature.mypage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilm.mulga.data.network.RetrofitClient
import com.ilm.mulga.data.repository.UserRepository
import com.ilm.mulga.domain.usecase.LogoutUseCase
import com.ilm.mulga.feature.login.UserState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// 로그아웃 후 UI 상태를 나타내는 sealed class 정의
sealed class MypageUiState {
    object Idle : MypageUiState()
    object LoggedOut : MypageUiState()
    data class Error(val message: String) : MypageUiState()
}

class MypageViewModel(
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<MypageUiState>(MypageUiState.Idle)

    private val userRepository: UserRepository = UserRepository(RetrofitClient.userService)
    val userState: StateFlow<UserState> = userRepository.userState

    val userName = (userState as? UserState.Exists)?.user?.name ?: "이름"
    val userEmail = (userState as? UserState.Exists)?.user?.email?: "이메일@email.com"

    // 로그아웃 수행 함수
    fun logout() {
        Log.d("MypageViewModel", "로그아웃 시도")
        viewModelScope.launch {
            logoutUseCase().onSuccess {
                _uiState.value = MypageUiState.LoggedOut
            }.onFailure { error ->
                _uiState.value = MypageUiState.Error(error.message ?: "로그아웃 실패")
            }
        }
    }
}
package com.ilm.mulga.feature.mypage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilm.mulga.data.network.RetrofitClient
import com.ilm.mulga.data.repository.UserRepository
import com.ilm.mulga.domain.usecase.LogoutUseCase
import com.ilm.mulga.feature.login.UserState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// 로그아웃 후 UI 상태를 나타내는 sealed class 정의
sealed class MypageUiState {
    object Idle : MypageUiState()
    object LoggedOut : MypageUiState()
    data class Error(val message: String) : MypageUiState()
}

// 사용자 정보를 저장할 데이터 클래스
data class UserInfo(
    val name: String = "",
    val email: String = ""
)

class MypageViewModel(
    private val logoutUseCase: LogoutUseCase,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MypageUiState>(MypageUiState.Idle)

    // 사용자 정보 StateFlow
    private val _userInfo = MutableStateFlow(UserInfo())
    val userInfo: StateFlow<UserInfo> = _userInfo.asStateFlow()

    // 초기화 시 사용자 정보 불러오기
    init {
        loadUserInfo()
    }

    val userNameFlow: StateFlow<String> = userInfo
        .map { it.name }
        .stateIn(viewModelScope, SharingStarted.Eagerly, userInfo.value.name)

    val userEmailFlow: StateFlow<String> = userInfo
        .map { it.email }
        .stateIn(viewModelScope, SharingStarted.Eagerly, userInfo.value.email)

    // 사용자 정보 불러오기
    fun loadUserInfo() {
        viewModelScope.launch {
            try {
                val user = userRepository.getUser()
                if (user != null) {
                    _userInfo.value = UserInfo(
                        name = user.name,
                        email = user.email
                    )
                    Log.d("MypageViewModel", "사용자 정보 : ${userInfo.value.name} ${userInfo.value.email}")
                    _uiState.value = MypageUiState.Idle
                } else {
                    Log.d("MypageViewModel", "사용자 정보 없음")
                    _uiState.value = MypageUiState.Error("사용자 정보를 찾을 수 없습니다")
                }
            } catch (e: Exception) {
                Log.e("MypageViewModel", "사용자 정보 로딩 실패", e)
                _uiState.value = MypageUiState.Error(e.message ?: "사용자 정보 로딩 실패")
            }
        }
    }

    // 로그아웃 수행 함수
    fun logout() {
        Log.d("MypageViewModel", "로그아웃 시도")
        viewModelScope.launch {
            userRepository.clearUser()
            logoutUseCase().onSuccess {
                _uiState.value = MypageUiState.LoggedOut
            }.onFailure { error ->
                _uiState.value = MypageUiState.Error(error.message ?: "로그아웃 실패")
            }
        }
    }
}
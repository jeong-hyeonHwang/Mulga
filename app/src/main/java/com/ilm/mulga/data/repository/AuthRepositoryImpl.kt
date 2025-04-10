package com.ilm.mulga.data.repository

import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.ilm.mulga.data.datasource.remote.AuthRemoteDataSource
import com.ilm.mulga.data.dto.response.AuthDto
import com.ilm.mulga.domain.repository.remote.AuthRepository
import com.ilm.mulga.presentation.mapper.toUser
import kotlinx.coroutines.tasks.await
import android.util.Log
import com.ilm.mulga.domain.repository.local.UserLocalRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AuthRepositoryImpl(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val credentialManager: CredentialManager,
    private val userLocalRepository: UserLocalRepository
) : AuthRepository {
    // 서버와의 통신을 통해 로그인, 로그아웃, 토큰 재발급, 회원 탈퇴, 온보딩 완료 등의 네트워크 작업을 수행

    // FirebaseAuth 인스턴스를 선언 및 초기화
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    init {
        // 55분마다 토큰 갱신하는 코루틴 루프 시작
        startTokenRefreshLoop()
    }

    override suspend fun signInWithCredential(credential: AuthCredential): Result<AuthDto> {
        return try {
            val firebaseUser = authRemoteDataSource.signInWithCredential(credential)
            if (firebaseUser != null) {
                getCurrentUserToken(false)
                Result.success(firebaseUser.toUser())
            } else {
                Result.failure(Exception("로그인 실패: 사용자 정보가 없습니다."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return try {
            // 원격 데이터 소스 로그아웃
            authRemoteDataSource.signOut()

            // Firebase 로그아웃
            auth.signOut()

            // 자격 증명 상태 클리어
            val clearRequest = ClearCredentialStateRequest()
            credentialManager.clearCredentialState(clearRequest)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCurrentUser(): AuthDto? {
        val firebaseUser = authRemoteDataSource.getCurrentUser()
        return firebaseUser?.toUser()
    }

    suspend fun getCurrentUserToken(forceRefresh: Boolean = false): Result<String> {
        return try {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                // 토큰을 비동기적으로 요청
                val tokenResult = currentUser.getIdToken(forceRefresh).await()
                val token = tokenResult.token
                if (token != null) {
                    Log.d("FirebaseToken", "Token: $token")
                    userLocalRepository.saveToken(token)
                    Result.success(token)
                } else {
                    Result.failure(Exception("토큰 가져오기 실패: 토큰이 null 입니다."))
                }
            } else {
                Result.failure(Exception("로그인된 사용자가 없습니다."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 55분마다 토큰을 강제 갱신하는 코루틴 루프
    private fun startTokenRefreshLoop() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                // 55분 대기 (55 * 60 * 1000 ms)
                delay(55 * 60 * 1000L)
                Log.d("AuthRepository", "55분 경과, 토큰 갱신 시도")
                getCurrentUserToken(forceRefresh = true)
            }
        }
    }
}
package com.ilm.mulga.data.network

import com.ilm.mulga.data.datasource.local.UserLocalDataSource
import com.ilm.mulga.data.repository.AuthRepositoryImpl
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val userLocalDataSource: UserLocalDataSource,
    private val authRepository: AuthRepositoryImpl
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        // 무한 루프 방지: 이미 2번 이상 시도했다면 더 이상 재시도하지 않습니다.
        if (responseCount(response) >= 2) {
            return null
        }
        // 토큰 갱신 시도
        val newToken = refreshToken() ?: return null

        // 갱신한 토큰을 로컬 저장소에 저장합니다.
        userLocalDataSource.saveToken(newToken)

        // 새로운 토큰을 사용해 요청을 재구성합니다.
        return response.request.newBuilder()
            .header("Authorization", "Bearer $newToken")
            .build()
    }

    // 재시도 횟수를 계산하여 무한 루프를 방지합니다.
    private fun responseCount(response: Response): Int {
        var count = 1
        var priorResponse = response.priorResponse
        while (priorResponse != null) {
            count++
            priorResponse = priorResponse.priorResponse
        }
        return count
    }

    // 동기적으로 토큰 갱신
    fun refreshToken(): String? = runBlocking {
        // forceRefresh를 true로 하여 새로운 토큰 요청
        val result = authRepository.getCurrentUserToken(forceRefresh = true)
        // Result가 성공이면 토큰 반환, 실패면 null 반환
        result.getOrNull()
    }
}


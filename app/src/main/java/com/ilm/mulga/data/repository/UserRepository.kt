package com.ilm.mulga.data.repository

import com.ilm.mulga.data.dto.request.SignUpRequestDto
import com.ilm.mulga.data.service.UserService
import com.ilm.mulga.domain.mapper.toDomain
import com.ilm.mulga.domain.model.UserEntity
import com.ilm.mulga.presentation.mapper.toDomain

class UserRepository (private val userService: UserService) {

    suspend fun signup(name: String, email: String, budget: Int): UserEntity? {
        val signUpRequest = SignUpRequestDto(
            name = name,
            email = email,
            budget = budget
        )

        val response = userService.signup(signUpRequest)
        return if (response.isSuccessful) {
            response.body()?.toDomain()
        } else {
            null // 에러 처리 간단하게 null 리턴
        }
    }

    suspend fun getUserData(): UserEntity? {
        val response = userService.checkUserExists()
        return if (response.isSuccessful) {
            val body = response.body()
            if (body?.code == "USER_1000") {
                null // 유저 없음
            } else {
                body?.toDomain() // 유저 있음 → 도메인 모델로 변환
            }
        } else {
            null // 에러 처리 간단하게 null 리턴
        }
    }
}
package com.ilm.mulga.data.repository

import com.ilm.mulga.data.service.UserService
import com.ilm.mulga.domain.mapper.toDomain
import com.ilm.mulga.domain.model.UserEntity

class UserRepository (private val userService: UserService) {

//    suspend fun signup(): UserEntity? {
//
//    }
//
//    suspend fun getUserData(): UserEntity? {
//        val response = userService.checkUserExists()
//        return if (response.isSuccessful) {
//            val body = response.body()
//            if (body?.code == "USER_1000") {
//                null // 유저 없음
//            } else {
//                body?.toDomain() // 유저 있음 → 도메인 모델로 변환
//            }
//        } else {
//            null // 에러 처리 간단하게 null 리턴
//        }
//    }
}
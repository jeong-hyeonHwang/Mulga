package com.ilm.mulga.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.ilm.mulga.data.dto.request.SignUpRequestDto
import com.ilm.mulga.data.service.UserService
import com.ilm.mulga.domain.model.UserEntity
import com.ilm.mulga.presentation.mapper.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository (
    private val userService: UserService,
    private val context: Context,
) {
    companion object {
        private const val PREF_NAME = "user_preferences"
        private const val KEY_USER = "user_entity"
    }

    private val gson: Gson by lazy {
        Gson()
    }

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    suspend fun saveUser(userEntity: UserEntity): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                // UserEntity를 JSON 문자열로 변환
                val userJson = gson.toJson(userEntity)

                // SharedPreferences에 JSON 문자열 저장
                sharedPreferences.edit()
                    .putString(KEY_USER, userJson)
                    .apply()

                true
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "사용자 정보 저장 실패", e)
            false
        }
    }

    suspend fun getUser(): UserEntity? {
        return try {
            withContext(Dispatchers.IO) {
                val userJson = sharedPreferences.getString(KEY_USER, null)
                if (userJson != null) {
                    gson.fromJson(userJson, UserEntity::class.java)
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "사용자 정보 조회 실패", e)
            null
        }
    }

    // 사용자 정보 삭제 메소드
    suspend fun clearUser(): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                sharedPreferences.edit()
                    .remove(KEY_USER)
                    .apply()
                true
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "사용자 정보 삭제 실패", e)
            false
        }
    }

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
                // 서버의 응답을 도메인 모델로 변환
                val userEntity = body?.toDomain()
                // userEntity가 null이 아니라면 저장 후 userEntity를 반환
                if (userEntity != null) {
                    saveUser(userEntity)
                }
                userEntity
            }
        } else {
            null // 에러 처리 간단하게 null 리턴
        }
    }
}
package com.ilm.mulga.data.service

import com.ilm.mulga.data.dto.request.SignUpRequestDto
import com.ilm.mulga.data.dto.response.AuthDto
import com.ilm.mulga.data.dto.response.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {
    // 서버 회원가입
    @POST("/signup")
    suspend fun signup(
        @Body request: SignUpRequestDto
    ): Response<UserDto>
    
    // 이미 가입한 유저인지 확인
    @GET("/me")
    suspend fun checkUserExists(): Response<UserDto>
}

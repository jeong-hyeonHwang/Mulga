package com.ilm.mulga.data.datasource.remote

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser

interface AuthRemoteDataSource {
    // 서버와 통신하여 사용자 관련 작업(로그인, 로그아웃, 토큰 재발급, 회원 탈퇴, 온보딩 완료)을 수행하는 역할 캡슐화
    suspend fun signInWithCredential(credential: AuthCredential): FirebaseUser?

    suspend fun signOut()

    fun getCurrentUser(): FirebaseUser?
}
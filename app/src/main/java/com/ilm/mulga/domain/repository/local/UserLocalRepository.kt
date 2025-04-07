package com.ilm.mulga.domain.repository.local

interface UserLocalRepository {
    fun saveToken(token: String)
    fun getToken(): String?
}
package com.ilm.mulga.data.repository.local

import com.ilm.mulga.data.datasource.local.UserLocalDataSource
import com.ilm.mulga.domain.repository.local.UserLocalRepository

class UserLocalRepositoryImpl(private val localDataSource: UserLocalDataSource) : UserLocalRepository {
    override fun saveToken(token: String) {
        localDataSource.saveToken(token)
    }

    override fun getToken(): String? {
        return localDataSource.getToken()
    }
}
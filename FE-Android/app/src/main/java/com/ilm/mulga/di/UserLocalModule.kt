package com.ilm.mulga.di

import com.ilm.mulga.data.datasource.local.UserLocalDataSource
import com.ilm.mulga.data.repository.local.UserLocalRepositoryImpl
import com.ilm.mulga.domain.repository.local.UserLocalRepository
import com.ilm.mulga.domain.usecase.GetTokenUseCase
import com.ilm.mulga.domain.usecase.SaveTokenUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val userLocalModule = module {
    single { UserLocalDataSource(androidContext()) }

    single<UserLocalRepository> { UserLocalRepositoryImpl(get())}

    factory { SaveTokenUseCase(get()) }
    factory { GetTokenUseCase(get()) }
}
package com.ilm.mulga.di

import com.google.firebase.auth.FirebaseAuth
import com.ilm.mulga.data.datasource.remote.AuthRemoteDataSource
import com.ilm.mulga.data.datasource.remote.AuthRemoteDataSourceImpl
import com.ilm.mulga.data.repository.AuthRepositoryImpl
import com.ilm.mulga.domain.repository.remote.AuthRepository
import com.ilm.mulga.domain.usecase.GetCurrentUserUseCase
import com.ilm.mulga.domain.usecase.LoginWithCredentialUseCase
import com.ilm.mulga.domain.usecase.LogoutUseCase
import com.ilm.mulga.feature.login.LoginViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import androidx.credentials.CredentialManager
import com.ilm.mulga.data.network.RetrofitClient
import com.ilm.mulga.data.repository.UserRepository

val loginModule = module {
    // Firebase
    single { FirebaseAuth.getInstance() }

    // CredentialManager
    single { CredentialManager.create(androidContext()) }

    // Data Sources
    single<AuthRemoteDataSource> { AuthRemoteDataSourceImpl(get()) }

    // Repositories
    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }
    single { AuthRepositoryImpl(get(), get(), get()) }

    single { RetrofitClient.userService }
    single { UserRepository(get(), get()) }

    // Use Cases
    factory { LoginWithCredentialUseCase(get()) }
    factory { LogoutUseCase(get()) }
    factory { GetCurrentUserUseCase(get()) }

    // ViewModels
    viewModel { LoginViewModel(get(), get(), get(), get(), get()) }
}
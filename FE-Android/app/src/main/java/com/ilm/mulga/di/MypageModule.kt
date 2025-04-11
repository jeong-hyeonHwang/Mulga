package com.ilm.mulga.di

import com.ilm.mulga.data.network.RetrofitClient
import com.ilm.mulga.data.repository.UserRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.ilm.mulga.domain.usecase.LogoutUseCase
import com.ilm.mulga.feature.mypage.MypageViewModel

val mypageModule = module {
    single { RetrofitClient.userService }
    single { UserRepository(get(), get()) }

    viewModel { MypageViewModel(logoutUseCase = get<LogoutUseCase>(), get()) }
}
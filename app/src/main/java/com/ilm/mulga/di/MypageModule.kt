package com.ilm.mulga.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.ilm.mulga.domain.usecase.LogoutUseCase
import com.ilm.mulga.feature.mypage.MypageViewModel

val mypageModule = module {
    viewModel { MypageViewModel(logoutUseCase = get<LogoutUseCase>()) }
}
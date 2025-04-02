package com.example.mulga.di

import com.example.mulga.domain.usecase.NotificationPublisherUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val useCaseModule = module {
    single { NotificationPublisherUseCase(get(), get(), androidContext())}
}
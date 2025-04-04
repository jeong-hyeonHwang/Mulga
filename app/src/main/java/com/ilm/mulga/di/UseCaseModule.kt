package com.ilm.mulga.di

import com.ilm.mulga.domain.usecase.NotificationPublisherUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val useCaseModule = module {
    single { NotificationPublisherUseCase(get(), get(), androidContext())}
}
package com.example.mulga.di

import com.example.mulga.data.repository.local.NotificationLocalRepositoryImpl
import com.example.mulga.domain.repository.local.NotificationLocalRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<NotificationLocalRepository> {
        NotificationLocalRepositoryImpl(get())
    }
}
package com.ilm.mulga.di

import com.ilm.mulga.data.repository.local.NotificationLocalRepositoryImpl
import com.ilm.mulga.domain.repository.local.NotificationLocalRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<NotificationLocalRepository> {
        NotificationLocalRepositoryImpl(get())
    }
}
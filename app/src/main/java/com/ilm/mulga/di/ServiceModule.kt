package com.ilm.mulga.di

import com.ilm.mulga.data.service.RabbitMqPublisher
import com.ilm.mulga.BuildConfig
import org.koin.dsl.module

val serviceModule = module {
    single {
        RabbitMqPublisher(
            host = BuildConfig.RABBITMQ_HOST,
            port = BuildConfig.RABBITMQ_PORT,
            username = BuildConfig.RABBITMQ_USERNAME,
            password = BuildConfig.RABBITMQ_PASSWORD,
            virtualHost = "/"
        )
    }
}
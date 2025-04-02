package com.ilm.mulga.di

import androidx.room.Room
import com.ilm.mulga.data.datasource.local.NotificationDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val roomModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            NotificationDatabase::class.java,
            "notification_database"
        ).fallbackToDestructiveMigration().build()
    }

    single { get<NotificationDatabase>().notificationDao() }
}
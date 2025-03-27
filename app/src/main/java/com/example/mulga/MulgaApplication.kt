package com.example.mulga

import android.app.Application
import com.example.mulga.di.calendarModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MulGaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MulGaApplication)
            modules(listOf(calendarModule))
        }
    }
}

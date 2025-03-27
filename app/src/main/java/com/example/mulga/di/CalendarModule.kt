package com.example.mulga.di

import com.example.mulga.feature.calendar.CalendarViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val calendarModule = module {
    viewModel { CalendarViewModel() }
}

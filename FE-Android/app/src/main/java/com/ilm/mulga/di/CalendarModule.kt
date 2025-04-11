package com.ilm.mulga.di

import com.ilm.mulga.feature.calendar.CalendarViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val calendarModule = module {
    viewModel { CalendarViewModel() }
}

package com.ilm.mulga.di

import com.ilm.mulga.feature.calendar.TransactionItemViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val transactionItemModule = module {
    viewModel { TransactionItemViewModel() }
}

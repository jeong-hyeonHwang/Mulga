package com.ilm.mulga.di

import com.ilm.mulga.feature.transaction_detail.TransactionDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val transactionDetailModule = module {
    viewModel { TransactionDetailViewModel() }
}

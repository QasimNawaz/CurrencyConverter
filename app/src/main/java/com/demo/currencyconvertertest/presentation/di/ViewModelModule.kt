package com.demo.currencyconvertertest.presentation.di

import com.demo.currencyconvertertest.presentation.ui.screens.main.MainScreenViewModel
import com.demo.currencyconvertertest.presentation.ui.screens.root.MainActivityViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainActivityViewModel(get()) }
    viewModel { MainScreenViewModel(get(), get()) }
}
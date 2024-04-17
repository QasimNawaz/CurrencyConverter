package com.demo.currencyconvertertest.domain.di

import com.demo.currencyconvertertest.domain.usecase.LocalCurrencyCodesUseCase
import com.demo.currencyconvertertest.domain.usecase.RemoteCurrencyCodesUseCase
import com.demo.currencyconvertertest.domain.usecase.RemoteConversionRatesUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { RemoteCurrencyCodesUseCase(get(), get()) }
    single { LocalCurrencyCodesUseCase(get(), get()) }
    single { RemoteConversionRatesUseCase(get(), get()) }
}
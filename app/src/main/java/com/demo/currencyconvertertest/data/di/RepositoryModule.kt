package com.demo.currencyconvertertest.data.di

import com.demo.currencyconvertertest.data.repository.LocalCurrencyCodesRepo
import com.demo.currencyconvertertest.data.repository.RemoteCurrencyCodesRepo
import com.demo.currencyconvertertest.data.repository.RemoteConversionRatesRepo
import com.demo.currencyconvertertest.data.repositoryimpl.LocalCurrencyCodesRepoImpl
import com.demo.currencyconvertertest.data.repositoryimpl.RemoteCurrencyCodesRepoImpl
import com.demo.currencyconvertertest.data.repositoryimpl.RemoteConversionRatesRepoImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<RemoteCurrencyCodesRepo> { RemoteCurrencyCodesRepoImpl(get(), get(), get()) }
    single<LocalCurrencyCodesRepo> { LocalCurrencyCodesRepoImpl(get()) }
    single<RemoteConversionRatesRepo> { RemoteConversionRatesRepoImpl(get(), get(), get()) }
}
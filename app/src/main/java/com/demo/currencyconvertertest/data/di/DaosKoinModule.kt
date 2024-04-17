package com.demo.currencyconvertertest.data.di

import com.demo.currencyconvertertest.data.database.CurrencyConverterDatabase
import org.koin.dsl.module

val daosKoinModule = module {
    includes(databaseKoinModule)

    single { get<CurrencyConverterDatabase>().currencyCodesDao() }
    single { get<CurrencyConverterDatabase>().conversionRatesDao() }
}
package com.demo.currencyconvertertest.data.di

import com.demo.currencyconvertertest.data.utils.ktorHttpClient
import org.koin.dsl.module

val networkModule = module {
    single { ktorHttpClient }
}
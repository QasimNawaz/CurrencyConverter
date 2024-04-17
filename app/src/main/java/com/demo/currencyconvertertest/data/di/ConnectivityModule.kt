package com.demo.currencyconvertertest.data.di

import com.demo.currencyconvertertest.domain.utils.Network
import com.demo.currencyconvertertest.domain.utils.NetworkConnectivity
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val connectivityModule = module {
    single<NetworkConnectivity> { Network(androidContext()) }
}
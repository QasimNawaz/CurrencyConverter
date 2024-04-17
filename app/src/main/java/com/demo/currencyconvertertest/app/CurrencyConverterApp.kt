package com.demo.currencyconvertertest.app

import android.app.Application
import com.demo.currencyconvertertest.data.di.connectivityModule
import com.demo.currencyconvertertest.data.di.daosKoinModule
import com.demo.currencyconvertertest.data.di.dispatchersModule
import com.demo.currencyconvertertest.data.di.networkModule
import com.demo.currencyconvertertest.data.di.repositoryModule
import com.demo.currencyconvertertest.domain.di.useCaseModule
import com.demo.currencyconvertertest.presentation.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext

class CurrencyConverterApp : Application() {
    override fun onCreate() {
        super.onCreate()
        GlobalContext.startKoin {
            androidLogger()
            androidContext(this@CurrencyConverterApp)
            modules(
                dispatchersModule,
                connectivityModule,
                networkModule,
                daosKoinModule,
                repositoryModule,
                useCaseModule,
                viewModelModule
            )
        }
    }
}
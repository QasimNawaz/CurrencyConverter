package com.demo.currencyconvertertest.data.di

import androidx.room.Room
import com.demo.currencyconvertertest.data.database.CurrencyConverterDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseKoinModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            CurrencyConverterDatabase::class.java,
            "currency-converter-database"
        ).build()
    }
}
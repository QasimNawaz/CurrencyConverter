package com.demo.currencyconvertertest.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.demo.currencyconvertertest.data.database.dao.CurrencyCodesDao
import com.demo.currencyconvertertest.data.database.dao.ConversionRatesDao
import com.demo.currencyconvertertest.data.database.entities.CurrencyCodesEntity
import com.demo.currencyconvertertest.data.database.entities.ConversionRatesEntity

@Database(
    entities = [CurrencyCodesEntity::class, ConversionRatesEntity::class], version = 1
)
@TypeConverters(MapTypeConverter::class)
abstract class CurrencyConverterDatabase : RoomDatabase() {
    abstract fun currencyCodesDao(): CurrencyCodesDao
    abstract fun conversionRatesDao(): ConversionRatesDao
}
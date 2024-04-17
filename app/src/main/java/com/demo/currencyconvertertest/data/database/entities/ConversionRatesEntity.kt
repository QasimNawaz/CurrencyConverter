package com.demo.currencyconvertertest.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.demo.currencyconvertertest.data.database.MapTypeConverter
import com.demo.currencyconvertertest.data.utils.CONVERSION_RATES_TABLE
import kotlinx.serialization.Serializable

@Entity(
    tableName = CONVERSION_RATES_TABLE
)
@Serializable
data class ConversionRatesEntity(

    @ColumnInfo("disclaimer")
    var disclaimer: String,

    @ColumnInfo("license")
    var license: String,

    @PrimaryKey
    @ColumnInfo("timestamp")
    var timestamp: Long = 0,

    @ColumnInfo("base")
    var base: String,

    @ColumnInfo("rates")
    @TypeConverters(MapTypeConverter::class)
    var rates: Map<String, String> = emptyMap(),
)

package com.demo.currencyconvertertest.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.demo.currencyconvertertest.data.database.MapTypeConverter
import com.demo.currencyconvertertest.data.utils.CURRENCY_CODES_TABLE
import kotlinx.serialization.Serializable

@Entity(
    tableName = CURRENCY_CODES_TABLE,
)
@Serializable
data class CurrencyCodesEntity(

    @PrimaryKey
    @ColumnInfo("timestamp")
    var timestamp: Long = 0,

    @ColumnInfo("currencies")
    @TypeConverters(MapTypeConverter::class)
    var currencies: Map<String, String> = emptyMap()
)
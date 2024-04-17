package com.demo.currencyconvertertest.data

import androidx.room.PrimaryKey
import com.demo.currencyconvertertest.data.database.entities.ConversionRatesEntity
import com.google.gson.annotations.SerializedName

data class RatesResponse(
    @SerializedName("disclaimer") val disclaimer: String? = null,

    @SerializedName("license") val license: String? = null,

    @PrimaryKey @SerializedName("timestamp") val timestamp: Long = 0,

    @SerializedName("base") val base: String? = null,

    @SerializedName("rates") val rates: Map<String, String>? = null,
)

fun RatesResponse.toRatesEntity() = ConversionRatesEntity(
    disclaimer = this.disclaimer ?: "",
    license = this.license ?: "",
    timestamp = this.timestamp,
    base = this.base ?: "",
    rates = this.rates ?: emptyMap()
)

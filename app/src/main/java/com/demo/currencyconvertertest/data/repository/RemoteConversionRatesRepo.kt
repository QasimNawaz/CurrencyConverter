package com.demo.currencyconvertertest.data.repository

import com.demo.currencyconvertertest.data.database.entities.ConversionRatesEntity
import com.demo.currencyconvertertest.domain.utils.ApiResponse

interface RemoteConversionRatesRepo {
    suspend fun getConversionRates(): ApiResponse<ConversionRatesEntity>
}
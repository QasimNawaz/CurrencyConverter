package com.demo.currencyconvertertest.data.repository

import com.demo.currencyconvertertest.domain.utils.ApiResponse

interface RemoteCurrencyCodesRepo {
    suspend fun getCurrencyCodes(): ApiResponse<Map<String, String>>
}
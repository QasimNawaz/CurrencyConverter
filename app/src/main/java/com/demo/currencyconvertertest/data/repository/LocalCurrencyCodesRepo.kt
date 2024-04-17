package com.demo.currencyconvertertest.data.repository


interface LocalCurrencyCodesRepo {
    suspend fun getCurrencyCodes(): Map<String, String>
}
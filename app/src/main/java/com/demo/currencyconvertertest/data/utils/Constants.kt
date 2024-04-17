package com.demo.currencyconvertertest.data.utils

//const val BASE_URL = "http://192.168.100.13:3002"

const val BASE_URL = "https://openexchangerates.org"
const val CURRENCIES = "${BASE_URL}/api/currencies.json"
const val RATES = "${BASE_URL}/api/latest.json"

const val CURRENCY_CODES_TABLE = "CURRENCY_CODES_TABLE"
const val CONVERSION_RATES_TABLE = "CONVERSION_RATES_TABLE"

const val EXPIRATION_TIME = (60 * 30 * 1000).toLong()
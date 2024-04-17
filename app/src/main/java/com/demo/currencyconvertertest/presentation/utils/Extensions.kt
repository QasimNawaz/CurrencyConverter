package com.demo.currencyconvertertest.presentation.utils

import java.text.DecimalFormat

fun Double.toCurrencyFormat(): String {
    val df = DecimalFormat("#.############")
    return df.format(this)
}
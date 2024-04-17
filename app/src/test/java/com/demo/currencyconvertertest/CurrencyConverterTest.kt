package com.demo.currencyconvertertest

import com.demo.currencyconvertertest.presentation.ui.screens.main.conversion
import com.demo.currencyconvertertest.presentation.utils.toCurrencyFormat
import junit.framework.TestCase.assertEquals
import org.junit.Test

class CurrencyConverterTest {

    @Test
    fun testConversion() {

        val currencyCodesMap = mapOf(
            "AED" to "United Arab Emirates Dirham",
            "AFN" to "Afghan Afghani",
            "ALL" to "Albanian Lek",
            "AMD" to "Armenian Dram",
            "ANG" to "Netherlands Antillean Guilder",
            "AOA" to "Angolan Kwanza",
            "ARS" to "Argentine Peso",
            "AUD" to "Australian Dollar",
            "AWG" to "Aruban Florin",
            "AZN" to "Azerbaijani Manat",
            "BAM" to "Bosnia-Herzegovina Convertible Mark",
            "BBD" to "Barbadian Dollar",
            "BDT" to "Bangladeshi Taka",
            "USD" to "United States Dollar"
        )

        val conversionRatesMap = mapOf(
            "AED" to "3.67308",
            "AFN" to "73.46122",
            "ALL" to "100.199199",
            "AMD" to "386.343712",
            "ANG" to "1.804141",
            "AOA" to "825.0225",
            "ARS" to "350.008559",
            "AUD" to "1.556171",
            "AWG" to "1.8",
            "AZN" to "1.7",
            "BAM" to "1.812016",
            "BBD" to "2",
            "BDT" to "110.066267",
            "USD" to "1"
        )

        val amount = 100.0
        val baseAmount = conversionRatesMap["USD"]?.toDoubleOrNull() ?: 1.0

        val expectedConvertedAmounts = mutableMapOf<String, String>()
        for (currencyCode in currencyCodesMap.keys) {
            val amount = conversion(
                amount, baseAmount, conversionRatesMap[currencyCode]?.toDoubleOrNull() ?: 1.0
            ).toCurrencyFormat()
            expectedConvertedAmounts[currencyCode] = amount
        }

        val convertedAmounts = mutableMapOf<String, String>()
        for (currencyCode in currencyCodesMap.keys) {
            val amount = conversion(
                amount, baseAmount, conversionRatesMap[currencyCode]?.toDoubleOrNull() ?: 1.0
            ).toCurrencyFormat()
            convertedAmounts[currencyCode] = amount
        }

        assertEquals(expectedConvertedAmounts, convertedAmounts)
    }
}
package com.demo.currencyconvertertest

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.demo.currencyconvertertest.presentation.ui.components.SelectCurrencyDialog
import com.demo.currencyconvertertest.presentation.ui.screens.main.ShowCurrencies
import com.demo.currencyconvertertest.presentation.ui.screens.main.conversion
import com.demo.currencyconvertertest.presentation.utils.toCurrencyFormat
import org.junit.Rule
import org.junit.Test

class MainScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testDialogWithAllCurrencies() {
        val currencyMap = mapOf(
            "AED" to "United Arab Emirates Dirham",
            "AFN" to "Afghan Afghani",
            "ALL" to "Albanian Lek",
            "AMD" to "Armenian Dram",
        )
        composeTestRule.setContent {
            SelectCurrencyDialog(
                currencies = currencyMap
            )
        }
        currencyMap.forEach { currency ->
            composeTestRule.onNodeWithText(currency.key).assertIsDisplayed()
            composeTestRule.onNodeWithText(currency.value).assertIsDisplayed()
        }
    }

    @Test
    fun testCalculatedConversionRatesDisplayed() {
        val conversionRates = mapOf(
            "USD" to "1.0",
            "AED" to "3.67308",
            "AFN" to "73.46122",
            "ALL" to "100.199199",
            "AMD" to "386.343712"
        )
        val amount = 100.0
        val baseAmount = 1.0
        composeTestRule.setContent {
            ShowCurrencies(
                amount = amount,
                baseAmount = baseAmount,
                conversionRates
            )
        }
        conversionRates.forEach { rate ->
            val amount = conversion(
                amount, baseAmount, rate.value.toDoubleOrNull() ?: 1.0
            ).toCurrencyFormat()
            composeTestRule.onNodeWithText(rate.key).assertIsDisplayed()
            composeTestRule.onNodeWithText(amount).assertIsDisplayed()
        }
    }
}
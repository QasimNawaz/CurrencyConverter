package com.demo.currencyconvertertest.presentation.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.demo.currencyconvertertest.presentation.ui.components.CurrencyOutlinedTextField
import com.demo.currencyconvertertest.presentation.ui.components.SelectCurrencyDialog
import com.demo.currencyconvertertest.presentation.utils.toCurrencyFormat
import org.koin.androidx.compose.getViewModel

@Composable
fun MainScreen(navController: NavHostController, viewModel: MainScreenViewModel = getViewModel()) {
    val currencyCodes: Map<String, String> by viewModel.currencyCodesState.collectAsStateWithLifecycle()
    val conversionRates: Map<String, String> by viewModel.conversionRatesState.collectAsStateWithLifecycle()
    var selectedCurrencyCode by remember { mutableStateOf("USD") }
    val openCurrencyDialog = remember { mutableStateOf(false) }
    if (openCurrencyDialog.value) {
        SelectCurrencyDialog(currencies = currencyCodes, onCurrencySelected = {
            selectedCurrencyCode = it
            openCurrencyDialog.value = false
        })
    }
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CurrencyOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.currencyInputState.value,
            onValueChange = { newValue ->
                viewModel.onAmountChange(newValue)
            },
            label = "Amount",
            keyboardType = KeyboardType.Decimal
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
        ) {
            Row(
                modifier = Modifier
                    .testTag("ShowCurrencyDialog")
                    .clickable {
                        openCurrencyDialog.value = true
                    }
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(2.dp)
                    )
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = selectedCurrencyCode, color = MaterialTheme.colorScheme.onBackground)
                Icon(
                    Icons.Default.KeyboardArrowDown, contentDescription = null
                )
            }
        }
        if (conversionRates.isNotEmpty()) {
            ShowCurrencies(
                viewModel.currencyInputState.value.toDoubleOrNull() ?: 0.0,
                conversionRates[selectedCurrencyCode]?.toDoubleOrNull() ?: 1.0,
                conversionRates
            )
        } else {
            // Show Shimmer
        }

    }
}

@Composable
fun ShowCurrencies(
    amount: Double, baseAmount: Double, conversionRates: Map<String, String>
) {
    LazyColumn {
        items(items = conversionRates.toList()) { rate ->
            key(rate.first) {
                val amount = conversion(
                    amount, baseAmount, rate.second.toDoubleOrNull() ?: 1.0
                ).toCurrencyFormat()
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.weight(0.2f),
                        text = rate.first,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        modifier = Modifier.weight(0.8f),
                        text = amount,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

fun conversion(
    amount: Double, baseAmount: Double, conversionRate: Double
): Double {
    val amountUSD = amount / baseAmount
    return amountUSD * conversionRate
}

package com.demo.currencyconvertertest.presentation.ui.screens.main

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.currencyconvertertest.domain.usecase.LocalCurrencyCodesUseCase
import com.demo.currencyconvertertest.domain.usecase.RemoteConversionRatesUseCase
import com.demo.currencyconvertertest.domain.utils.ApiResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val localCurrenciesUseCase: LocalCurrencyCodesUseCase,
    private val remoteRatesUseCase: RemoteConversionRatesUseCase
) : ViewModel() {

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _currencyInputState = mutableStateOf("")
    val currencyInputState = _currencyInputState

    private val _currencyCodesState = MutableStateFlow<Map<String, String>>(emptyMap())
    val currencyCodesState: StateFlow<Map<String, String>> = _currencyCodesState

    private val _conversionRatesState = MutableStateFlow<Map<String, String>>(emptyMap())
    val conversionRatesState: StateFlow<Map<String, String>> = _conversionRatesState

    init {
        getCurrencyCodes()
        getConversionRates()
    }

    fun onAmountChange(amount: String) {
        if (amount.isEmpty()) {
            _currencyInputState.value = ""
        } else {
            _currencyInputState.value = amount
        }
    }

    fun getCurrencyCodes() {
        viewModelScope.launch {
            localCurrenciesUseCase.execute(params = Unit).collect {
                _currencyCodesState.value = it
            }
        }
    }

    fun getConversionRates() {
        viewModelScope.launch {
            remoteRatesUseCase.execute(params = Unit).collect {
                when (it) {
                    is ApiResponse.Error.NoNetwork -> {
                        _error.emit("${it.cause.message}")
                    }

                    is ApiResponse.Error.HttpError -> {
                        _error.emit("${it.message}")
                    }

                    is ApiResponse.Error.SerializationError -> {
                        _error.emit("${it.message}")
                    }

                    is ApiResponse.Error.GenericError -> {
                        _error.emit("${it.message}")
                    }

                    is ApiResponse.Success -> {
                        Log.d(
                            "CurrencyConverterData",
                            "MainScreenViewModel: ${Gson().toJson(it.body)}"
                        )
                        _conversionRatesState.value = it.body.rates
                    }
                }
            }
        }
    }
}
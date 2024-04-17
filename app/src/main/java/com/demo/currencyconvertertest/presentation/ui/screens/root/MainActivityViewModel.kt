package com.demo.currencyconvertertest.presentation.ui.screens.root

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.currencyconvertertest.domain.usecase.RemoteCurrencyCodesUseCase
import com.demo.currencyconvertertest.domain.utils.ApiResponse
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class MainActivityViewModel(
    private val useCase: RemoteCurrencyCodesUseCase,
) : ViewModel() {

    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        viewModelScope.launch {
            delay(3_000L)
            Log.d("CurrencyConverterData", "MainActivityViewModel useCase called")
            useCase.execute(params = Unit).collect {
                when (it) {
                    is ApiResponse.Error.NoNetwork -> {
                        Log.d("CurrencyConverterData", "MainActivityViewModel useCase NoNetwork")
                        _error.emit("${it.cause.message}")
                        _loading.emit(false)
                    }

                    is ApiResponse.Error.HttpError -> {
                        Log.d("CurrencyConverterData", "MainActivityViewModel useCase HttpError")
                        _error.emit("${it.message}")
                        _loading.emit(false)
                    }

                    is ApiResponse.Error.SerializationError -> {
                        Log.d("CurrencyConverterData", "MainActivityViewModel useCase SerializationError")
                        _error.emit("${it.message}")
                        _loading.emit(false)
                    }

                    is ApiResponse.Error.GenericError -> {
                        Log.d("CurrencyConverterData", "MainActivityViewModel useCase GenericError")
                        _error.emit("${it.message}")
                        _loading.emit(false)
                    }

                    is ApiResponse.Success -> {
                        Log.d("CurrencyConverterData", "MainActivityViewModel useCase Success")
                        Log.d("CurrencyConverterData CurrenciesMap:", "${Gson().toJson(it.body)}")
                        _loading.emit(false)
                    }
                }
            }
        }
    }
}
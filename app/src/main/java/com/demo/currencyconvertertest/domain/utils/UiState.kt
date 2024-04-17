package com.demo.currencyconvertertest.domain.utils

sealed interface UiState<out T> {
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val code: Int = -1, val error: String) : UiState<Nothing>
    object Loading : UiState<Nothing>
    object Empty : UiState<Nothing>
}
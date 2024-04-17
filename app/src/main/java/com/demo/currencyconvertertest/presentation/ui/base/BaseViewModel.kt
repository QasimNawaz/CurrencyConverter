package com.demo.currencyconvertertest.presentation.ui.base

import androidx.lifecycle.ViewModel
import com.demo.currencyconvertertest.domain.utils.ApiResponse
import com.demo.currencyconvertertest.domain.utils.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class BaseViewModel<T> : ViewModel() {

    internal val _uiState = MutableStateFlow<UiState<T>>(UiState.Empty)
    val uiState: StateFlow<UiState<T>> = _uiState

    suspend fun Flow<ApiResponse<T>>.asUiState() {
        this.collect {
            when (it) {
                is ApiResponse.Error.NoNetwork -> {
                    _uiState.emit(UiState.Error(code = 12029, error = "${it.cause.message}"))
                }

                is ApiResponse.Error.HttpError -> {
                    _uiState.emit(
                        UiState.Error(
                            code = it.code,
                            error = "${it.message}"
                        )
                    )
                }

                is ApiResponse.Error.SerializationError -> {
                    _uiState.emit(UiState.Error(error = "${it.message}"))
                }

                is ApiResponse.Error.GenericError -> {
                    _uiState.emit(UiState.Error(error = "${it.message}"))
                }

                is ApiResponse.Success -> {
                    _uiState.emit(UiState.Success(it.body))
                }
            }
        }
    }
}
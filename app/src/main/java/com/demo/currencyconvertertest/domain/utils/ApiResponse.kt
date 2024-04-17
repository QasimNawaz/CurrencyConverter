package com.demo.currencyconvertertest.domain.utils

sealed class ApiResponse<out T> {

    data class Success<T>(val body: T) : ApiResponse<T>()

    sealed class Error : ApiResponse<Nothing>() {

        data class NoNetwork(
            val cause: Exception
        ) : Error()

        data class HttpError(
            val code: Int,
            val message: String?,
            val errorMessage: String?,
        ) : Error()

        data class SerializationError(
            val message: String?,
            val errorMessage: String?,
        ) : Error()

        data class GenericError(
            val message: String?,
            val errorMessage: String?,
        ) : Error()
    }
}
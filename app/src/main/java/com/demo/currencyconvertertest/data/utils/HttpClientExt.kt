package com.demo.currencyconvertertest.data.utils

import com.demo.currencyconvertertest.domain.utils.ApiResponse
import com.demo.currencyconvertertest.domain.utils.ConnectivityException
import com.demo.currencyconvertertest.domain.utils.HttpExceptions
import com.demo.currencyconvertertest.domain.utils.NetworkConnectivity
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import kotlinx.serialization.SerializationException

suspend inline fun <reified T> HttpClient.safeRequest(
    networkConnectivity: NetworkConnectivity,
    block: HttpRequestBuilder.() -> Unit,
    saveToDatabase: (T) -> Unit
): ApiResponse<T> {
    return try {
        if (networkConnectivity.isConnected()) {
            val response = request { block() }
            saveToDatabase.invoke(response.body())
            ApiResponse.Success(response.body())
        } else {
            ApiResponse.Error.NoNetwork(ConnectivityException())
        }
    } catch (exception: ClientRequestException) {
        ApiResponse.Error.HttpError(
            code = exception.response.status.value,
            message = exception.message,
            errorMessage = "Status Code: ${exception.response.status.value} - API Key Missing",
        )
    } catch (exception: HttpExceptions) {
        ApiResponse.Error.HttpError(
            code = exception.response.status.value,
            message = exception.message,
            errorMessage = exception.message,
        )
    } catch (exception: SerializationException) {
        ApiResponse.Error.SerializationError(
            message = exception.message,
            errorMessage = "Something went wrong",
        )
    } catch (exception: Exception) {
        ApiResponse.Error.GenericError(
            message = exception.message,
            errorMessage = "Something went wrong",
        )
    }
}

fun Long.isExpired(): Boolean {
    return System.currentTimeMillis() - this > EXPIRATION_TIME
}
package com.demo.currencyconvertertest.domain.utils

import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse

class HttpExceptions(
    response: HttpResponse,
    failureReason: String?,
    cachedResponseText: String,
) : ResponseException(response, cachedResponseText) {
    override val message: String = "$failureReason"
}
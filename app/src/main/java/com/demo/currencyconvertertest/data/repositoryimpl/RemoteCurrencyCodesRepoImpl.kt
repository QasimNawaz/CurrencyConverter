package com.demo.currencyconvertertest.data.repositoryimpl

import com.demo.currencyconvertertest.data.database.dao.CurrencyCodesDao
import com.demo.currencyconvertertest.data.database.entities.CurrencyCodesEntity
import com.demo.currencyconvertertest.data.repository.RemoteCurrencyCodesRepo
import com.demo.currencyconvertertest.data.utils.CURRENCIES
import com.demo.currencyconvertertest.data.utils.isExpired
import com.demo.currencyconvertertest.data.utils.safeRequest
import com.demo.currencyconvertertest.domain.utils.ApiResponse
import com.demo.currencyconvertertest.domain.utils.NetworkConnectivity
import io.ktor.client.HttpClient
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType

class RemoteCurrencyCodesRepoImpl(
    private val client: HttpClient,
    private val networkConnectivity: NetworkConnectivity,
    private val currencyDao: CurrencyCodesDao
) : RemoteCurrencyCodesRepo {
    override suspend fun getCurrencyCodes(): ApiResponse<Map<String, String>> {
        currencyDao.getCurrencyEntitiesStream().let {
            return if (it.isNotEmpty()) {
                val currencyEntity = it.first()
                if (currencyEntity.timestamp.isExpired()) {
                    getFromNetwork()
                } else {
                    ApiResponse.Success(currencyEntity.currencies)
                }
            } else {
                getFromNetwork()
            }
        }
    }

    suspend fun getFromNetwork(): ApiResponse<Map<String, String>> {
        return client.safeRequest(networkConnectivity, {
            method = HttpMethod.Get
            url {
                url(CURRENCIES)
                parameters.append("app_id", "efae4f51d1544110807e1890469b5ecb")
            }
            contentType(ContentType.Application.FormUrlEncoded)
        }, {
            currencyDao.deleteAllCurrencyEntities()
            currencyDao.addCurrencyEntity(
                CurrencyCodesEntity(
                    timestamp = System.currentTimeMillis(), currencies = it
                )
            )
        })
    }
}
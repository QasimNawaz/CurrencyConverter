package com.demo.currencyconvertertest.data.repositoryimpl

import android.util.Log
import com.demo.currencyconvertertest.data.database.dao.ConversionRatesDao
import com.demo.currencyconvertertest.data.database.entities.ConversionRatesEntity
import com.demo.currencyconvertertest.data.repository.RemoteConversionRatesRepo
import com.demo.currencyconvertertest.data.utils.RATES
import com.demo.currencyconvertertest.data.utils.isExpired
import com.demo.currencyconvertertest.data.utils.safeRequest
import com.demo.currencyconvertertest.domain.utils.ApiResponse
import com.demo.currencyconvertertest.domain.utils.NetworkConnectivity
import io.ktor.client.HttpClient
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType

class RemoteConversionRatesRepoImpl(
    private val client: HttpClient,
    private val networkConnectivity: NetworkConnectivity,
    private val ratesDao: ConversionRatesDao
) : RemoteConversionRatesRepo {

    override suspend fun getConversionRates(): ApiResponse<ConversionRatesEntity> {
        ratesDao.getRatesEntitiesStream().let {
            return if (it.isNotEmpty()) {
                val ratesEntity = it.first()
                if (ratesEntity.timestamp.isExpired()) {
                    getFromNetwork()
                } else {
                    ApiResponse.Success(ratesEntity)
                }
            } else {
                getFromNetwork()
            }
        }
    }

    suspend fun getFromNetwork(): ApiResponse<ConversionRatesEntity> {
        return client.safeRequest(networkConnectivity, {
            method = HttpMethod.Get
            url {
                url(RATES)
                parameters.append("app_id", "efae4f51d1544110807e1890469b5ecb")
            }
            contentType(ContentType.Application.FormUrlEncoded)
        }, {
            ratesDao.deleteAllRatesEntities()
            ratesDao.addRatesEntity(
                it
            )
        })
    }
}
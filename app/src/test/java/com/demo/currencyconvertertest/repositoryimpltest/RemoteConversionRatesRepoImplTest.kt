package com.demo.currencyconvertertest.repositoryimpltest

import com.demo.currencyconvertertest.data.database.dao.ConversionRatesDao
import com.demo.currencyconvertertest.data.database.entities.ConversionRatesEntity
import com.demo.currencyconvertertest.data.repositoryimpl.RemoteConversionRatesRepoImpl
import com.demo.currencyconvertertest.domain.utils.ApiResponse
import com.demo.currencyconvertertest.domain.utils.Network
import com.demo.currencyconvertertest.domain.utils.NetworkConnectivity
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.fullPath
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals

class RemoteConversionRatesRepoImplTest {

    @Mock
    private lateinit var mockRemoteConversionRatesRepoImpl: RemoteConversionRatesRepoImpl

    private lateinit var remoteConversionRatesRepoImpl: RemoteConversionRatesRepoImpl

    private lateinit var mockWebServer: MockWebServer

    @Mock
    private lateinit var mockHttpClient: HttpClient

    @Mock
    private lateinit var mockNetworkConnectivity: NetworkConnectivity

    @Mock
    private lateinit var mockConversionRatesDao: ConversionRatesDao

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        mockWebServer = MockWebServer()
        mockWebServer.start()
        mockNetworkConnectivity = Network(null)
        mockHttpClient = HttpClient(MockEngine) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            install(DefaultRequest) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
            engine {
                addHandler { request ->
                    when (request.url.fullPath) {
                        "/api/latest.json?app_id=efae4f51d1544110807e1890469b5ecb" -> {
                            val responseHeaders =
                                headersOf(HttpHeaders.ContentType, "application/json")
                            val responseData = """{
    "disclaimer": "Usage subject to terms: https://openexchangerates.org/terms",
    "license": "https://openexchangerates.org/license",
    "timestamp": 1693886401,
    "base": "USD",
    "rates": {
          "AED": 3.67308,
          "AFN": 73.46122,
          "ALL": 100.199199,
          "AMD": 386.343712
          }
}"""
                            respond(responseData, HttpStatusCode.OK, responseHeaders)
                        }

                        else -> error("Unhandled ${request.url.fullPath}")
                    }
                }
            }
        }
        remoteConversionRatesRepoImpl =
            RemoteConversionRatesRepoImpl(
                mockHttpClient,
                mockNetworkConnectivity,
                mockConversionRatesDao
            )
    }

    @After
    fun teardown() {
        mockHttpClient.close()
        mockWebServer.close()

    }

    @Test
    fun `getConversionRates should return data from network`() = runTest {
        val mockApiResponse = ApiResponse.Success(
            ConversionRatesEntity(
                disclaimer = "Usage subject to terms: https://openexchangerates.org/terms",
                license = "https://openexchangerates.org/license",
                timestamp = 1693886401,
                base = "USD",
                rates = mapOf(
                    "AED" to "3.67308",
                    "AFN" to "73.46122",
                    "ALL" to "100.199199",
                    "AMD" to "386.343712"
                )
            )
        )
        Mockito.`when`(mockRemoteConversionRatesRepoImpl.getFromNetwork())
            .thenReturn(mockApiResponse)
        val result = remoteConversionRatesRepoImpl.getFromNetwork()
        assertEquals(mockApiResponse, result)
    }

    @Test
    fun `getConversionRates should return data from cache`() = runTest {
        val mockConversionRatesEntity = ConversionRatesEntity(
            disclaimer = "Usage subject to terms: https://openexchangerates.org/terms",
            license = "https://openexchangerates.org/license",
            timestamp = 1693886401,
            base = "USD",
            rates = mapOf(
                "AED" to "3.67308",
                "AFN" to "73.46122",
                "ALL" to "100.199199",
                "AMD" to "386.343712"
            )
        )
        Mockito.`when`(mockConversionRatesDao.getRatesEntitiesStream())
            .thenReturn(listOf(mockConversionRatesEntity))
        val result = remoteConversionRatesRepoImpl.getConversionRates()
        assertEquals(ApiResponse.Success(mockConversionRatesEntity), result)
    }

    @Test
    fun `getConversionRates should return data from network if database is empty`() = runTest {
        val mockApiResponse = ApiResponse.Success(
            ConversionRatesEntity(
                disclaimer = "Usage subject to terms: https://openexchangerates.org/terms",
                license = "https://openexchangerates.org/license",
                timestamp = 1693886401,
                base = "USD",
                rates = mapOf(
                    "AED" to "3.67308",
                    "AFN" to "73.46122",
                    "ALL" to "100.199199",
                    "AMD" to "386.343712"
                )
            )
        )
        Mockito.`when`(mockConversionRatesDao.getRatesEntitiesStream()).thenReturn(emptyList())
        Mockito.`when`(mockRemoteConversionRatesRepoImpl.getConversionRates())
            .thenReturn(mockApiResponse)
        val result = remoteConversionRatesRepoImpl.getConversionRates()
        assertEquals(mockApiResponse, result)
    }

    @Test
    fun `getConversionRates should return cached data when it's not expired`() = runTest {
        val mockConversionRatesEntity = ConversionRatesEntity(
            disclaimer = "Usage subject to terms: https://openexchangerates.org/terms",
            license = "https://openexchangerates.org/license",
            timestamp = System.currentTimeMillis(),
            base = "USD",
            rates = mapOf(
                "AED" to "3.67308",
                "AFN" to "73.46122",
                "ALL" to "100.199199",
                "AMD" to "386.343712"
            )
        )
        Mockito.`when`(mockConversionRatesDao.getRatesEntitiesStream())
            .thenReturn(listOf(mockConversionRatesEntity))
        val result = remoteConversionRatesRepoImpl.getConversionRates()
        assertEquals(ApiResponse.Success(mockConversionRatesEntity), result)
        Mockito.verify(mockConversionRatesDao, Mockito.never())
            .addRatesEntity(mockConversionRatesEntity)
    }

    @Test
    fun `getConversionRates should return data from network when cached data is expired`() =
        runTest {
            val mockConversionRatesEntity = ConversionRatesEntity(
                disclaimer = "Usage subject to terms: https://openexchangerates.org/terms",
                license = "https://openexchangerates.org/license",
                timestamp = 1693886401,
                base = "USD",
                rates = mapOf(
                    "AED" to "3.67308",
                    "AFN" to "73.46122",
                    "ALL" to "100.199199",
                    "AMD" to "386.343712"
                )
            )
            val mockApiResponse = ApiResponse.Success(mockConversionRatesEntity)
            Mockito.`when`(mockConversionRatesDao.getRatesEntitiesStream())
                .thenReturn(listOf(mockConversionRatesEntity.copy(timestamp = 0)))
            Mockito.`when`(mockRemoteConversionRatesRepoImpl.getFromNetwork())
                .thenReturn(mockApiResponse)
            val result = remoteConversionRatesRepoImpl.getConversionRates()
            assertEquals(mockApiResponse, result)
            Mockito.verify(mockConversionRatesDao)
                .addRatesEntity(mockConversionRatesEntity)
        }
}
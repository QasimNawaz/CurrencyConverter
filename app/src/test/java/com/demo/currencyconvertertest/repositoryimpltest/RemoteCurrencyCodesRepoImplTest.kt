package com.demo.currencyconvertertest.repositoryimpltest

import com.demo.currencyconvertertest.data.database.dao.CurrencyCodesDao
import com.demo.currencyconvertertest.data.database.entities.CurrencyCodesEntity
import com.demo.currencyconvertertest.data.repositoryimpl.RemoteCurrencyCodesRepoImpl
import com.demo.currencyconvertertest.data.utils.safeRequest
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
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals

class RemoteCurrencyCodesRepoImplTest {

    @Mock
    private lateinit var mockRemoteCurrencyCodesRepoImpl: RemoteCurrencyCodesRepoImpl

    private lateinit var remoteCurrencyCodesRepoImpl: RemoteCurrencyCodesRepoImpl

    private lateinit var mockWebServer: MockWebServer

    @Mock
    private lateinit var mockHttpClient: HttpClient

    @Mock
    private lateinit var mockNetworkConnectivity: NetworkConnectivity

    @Mock
    private lateinit var mockCurrencyDao: CurrencyCodesDao

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
                        "/api/currencies.json?app_id=efae4f51d1544110807e1890469b5ecb" -> {
                            val responseHeaders =
                                headersOf(HttpHeaders.ContentType, "application/json")
                            val responseData = """{"USD":"United States Dollar"}"""
                            respond(responseData, HttpStatusCode.OK, responseHeaders)
                        }

                        else -> error("Unhandled ${request.url.fullPath}")
                    }
                }
            }
        }
        remoteCurrencyCodesRepoImpl =
            RemoteCurrencyCodesRepoImpl(mockHttpClient, mockNetworkConnectivity, mockCurrencyDao)
    }

    @After
    fun teardown() {
        mockHttpClient.close()
        mockWebServer.close()

    }

    @Test
    fun `getCurrencyCodes should return data from network`() = runTest {
        val mockApiResponse = ApiResponse.Success(mapOf("USD" to "United States Dollar"))
        `when`(mockRemoteCurrencyCodesRepoImpl.getFromNetwork()).thenReturn(mockApiResponse)
        val result = remoteCurrencyCodesRepoImpl.getFromNetwork()
        assertEquals(mockApiResponse, result)
    }

    @Test
    fun `getCurrencyCodes should return data from cache`() = runTest {
        val mockCurrencyEntity = CurrencyCodesEntity(
            System.currentTimeMillis(), mapOf("USD" to "United States Dollar")
        )
        `when`(mockCurrencyDao.getCurrencyEntitiesStream()).thenReturn(listOf(mockCurrencyEntity))
        val result = remoteCurrencyCodesRepoImpl.getCurrencyCodes()
        assertEquals(ApiResponse.Success(mockCurrencyEntity.currencies), result)
    }

    @Test
    fun `getCurrencyCodes should return data from network if database is empty`() = runTest {
        val mockApiResponse = ApiResponse.Success(mapOf("USD" to "United States Dollar"))
        `when`(mockCurrencyDao.getCurrencyEntitiesStream()).thenReturn(emptyList())
        `when`(mockRemoteCurrencyCodesRepoImpl.getCurrencyCodes()).thenReturn(mockApiResponse)
        val result = remoteCurrencyCodesRepoImpl.getCurrencyCodes()
        assertEquals(mockApiResponse, result)
    }

    @Test
    fun `getCurrencyCodes should return cached data when it's not expired`() = runTest {
        val cachedData = mapOf("USD" to "United States Dollar")
        val currencyEntity =
            CurrencyCodesEntity(timestamp = System.currentTimeMillis(), currencies = cachedData)
        `when`(mockCurrencyDao.getCurrencyEntitiesStream()).thenReturn(listOf(currencyEntity))
        val result = remoteCurrencyCodesRepoImpl.getCurrencyCodes()
        assertEquals(ApiResponse.Success(cachedData), result)
        verify(mockCurrencyDao, never()).addCurrencyEntity(currencyEntity)
    }

    @Test
    fun `getCurrencyCodes should return data from network when cached data is expired`() = runTest {
        val cachedData = mapOf("USD" to "United States Dollar")
        val mockApiResponse = ApiResponse.Success(mapOf("USD" to "United States Dollar"))
        val expiredTimestamp = System.currentTimeMillis() - System.currentTimeMillis() + 600000
        val currencyEntity =
            CurrencyCodesEntity(timestamp = expiredTimestamp, currencies = cachedData)
        `when`(mockCurrencyDao.getCurrencyEntitiesStream()).thenReturn(listOf(currencyEntity)) // Simulate cached but expired data
        `when`(mockRemoteCurrencyCodesRepoImpl.getFromNetwork()).thenReturn(mockApiResponse)
        val result = remoteCurrencyCodesRepoImpl.getCurrencyCodes()
        mockCurrencyDao.addCurrencyEntity(currencyEntity)
        assertEquals(mockApiResponse, result)

        verify(mockCurrencyDao).addCurrencyEntity(currencyEntity) // Verify that insert is called to update the cache
    }
}
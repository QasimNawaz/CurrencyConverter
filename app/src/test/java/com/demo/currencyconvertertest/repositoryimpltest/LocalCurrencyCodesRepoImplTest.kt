package com.demo.currencyconvertertest.repositoryimpltest

import com.demo.currencyconvertertest.data.database.dao.CurrencyCodesDao
import com.demo.currencyconvertertest.data.database.entities.CurrencyCodesEntity
import com.demo.currencyconvertertest.data.repositoryimpl.LocalCurrencyCodesRepoImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class LocalCurrencyCodesRepoImplTest {

    @Mock
    private lateinit var currencyDao: CurrencyCodesDao

    private lateinit var localCurrencyCodesRepo: LocalCurrencyCodesRepoImpl

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        localCurrencyCodesRepo = LocalCurrencyCodesRepoImpl(currencyDao)
    }

    @Test
    fun `getCurrencyCodes should return the expected map`() = runTest {
        val expectedCurrencyMap = mapOf(
            "AED" to "United Arab Emirates Dirham",
            "AFN" to "Afghan Afghani",
            "ALL" to "Albanian Lek",
            "AMD" to "Armenian Dram",
        )
        val currencyEntities = CurrencyCodesEntity(
            timestamp = System.currentTimeMillis(),
            currencies = expectedCurrencyMap
        )

        Mockito.`when`(currencyDao.getCurrencyEntitiesStream()).thenReturn(listOf(currencyEntities))

        val result = localCurrencyCodesRepo.getCurrencyCodes()

        assertEquals(result, expectedCurrencyMap)
    }
}
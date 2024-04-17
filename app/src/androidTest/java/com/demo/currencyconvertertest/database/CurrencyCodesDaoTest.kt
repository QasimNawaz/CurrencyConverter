package com.demo.currencyconvertertest.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.demo.currencyconvertertest.data.database.CurrencyConverterDatabase
import com.demo.currencyconvertertest.data.database.dao.CurrencyCodesDao
import com.demo.currencyconvertertest.data.database.entities.CurrencyCodesEntity
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
@SmallTest
class CurrencyCodesDaoTest : TestCase() {

    private lateinit var database: CurrencyConverterDatabase
    private lateinit var currencyCodesDao: CurrencyCodesDao
    private val entity = CurrencyCodesEntity(
        timestamp = 1693886401, currencies = mapOf(
            "AED" to "United Arab Emirates Dirham",
            "AFN" to "Afghan Afghani",
            "ALL" to "Albanian Lek",
            "AMD" to "Armenian Dram",
        )
    )

    @Before
    public override fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), CurrencyConverterDatabase::class.java
        ).allowMainThreadQueries().build()

        currencyCodesDao = database.currencyCodesDao()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun addCurrencyCodesTest() = kotlinx.coroutines.test.runTest {
        currencyCodesDao.addCurrencyEntity(entity)
        val currencyCodes = currencyCodesDao.getCurrencyEntitiesStream()
        assertEquals(currencyCodes.contains(entity), true)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getCurrencyCodesTest() = kotlinx.coroutines.test.runTest {
        currencyCodesDao.addCurrencyEntity(entity)
        assertEquals(currencyCodesDao.getCurrencyEntitiesStream().isNotEmpty(), true)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteCurrencyCodesTest() = kotlinx.coroutines.test.runTest {
        currencyCodesDao.addCurrencyEntity(entity)
        currencyCodesDao.deleteAllCurrencyEntities()
        assertEquals(currencyCodesDao.getCurrencyEntitiesStream().isEmpty(), true)
    }

    @After
    @Throws(IOException::class)
    public override fun tearDown() {
        database.close()
    }
}
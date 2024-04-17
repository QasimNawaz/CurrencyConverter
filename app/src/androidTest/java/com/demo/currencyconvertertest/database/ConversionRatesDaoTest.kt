package com.demo.currencyconvertertest.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.demo.currencyconvertertest.data.database.CurrencyConverterDatabase
import com.demo.currencyconvertertest.data.database.dao.ConversionRatesDao
import com.demo.currencyconvertertest.data.database.entities.ConversionRatesEntity
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
@SmallTest
class ConversionRatesDaoTest : TestCase() {

    private lateinit var database: CurrencyConverterDatabase
    private lateinit var conversionRatesDao: ConversionRatesDao
    private val entity = ConversionRatesEntity(
        disclaimer = "Usage subject to terms: https://openexchangerates.org/terms",
        license = "https://openexchangerates.org/license",
        timestamp = 1693886401,
        base = "USD",
        rates = mapOf(
            "AED" to "3.67308", "AFN" to "73.46122", "ALL" to "100.199199", "AMD" to "386.343712"
        )
    )

    @Before
    public override fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), CurrencyConverterDatabase::class.java
        ).allowMainThreadQueries().build()

        conversionRatesDao = database.conversionRatesDao()
    }

    @After
    @Throws(IOException::class)
    public override fun tearDown() {
        database.close()
    }

    @Test
    fun `addConversionRatesTest`() = runTest {
        conversionRatesDao.addRatesEntity(entity)
        val rates = conversionRatesDao.getRatesEntitiesStream()
        assertEquals(rates.contains(entity), true)
    }

    @Test
    fun getConversionRatesTest() = runTest {
        conversionRatesDao.addRatesEntity(entity)
        assertEquals(conversionRatesDao.getRatesEntitiesStream().isNotEmpty(), true)
    }

    @Test
    fun deleteConversionRatesTest() = runTest {
        conversionRatesDao.addRatesEntity(entity)
        conversionRatesDao.deleteAllRatesEntities()
        assertEquals(conversionRatesDao.getRatesEntitiesStream().isEmpty(), true)
    }

}
package com.demo.currencyconvertertest.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.demo.currencyconvertertest.data.database.entities.ConversionRatesEntity

@Dao
interface ConversionRatesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addRatesEntity(ratesEntity: ConversionRatesEntity)

    @Query(value = "SELECT * FROM CONVERSION_RATES_TABLE")
    suspend fun getRatesEntitiesStream(): List<ConversionRatesEntity>

    @Update
    suspend fun updateRatesEntity(ratesEntity: ConversionRatesEntity)

    @Query("DELETE FROM CONVERSION_RATES_TABLE")
    suspend fun deleteAllRatesEntities()
}
package com.demo.currencyconvertertest.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.demo.currencyconvertertest.data.database.entities.CurrencyCodesEntity

@Dao
interface CurrencyCodesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCurrencyEntity(currencyEntity: CurrencyCodesEntity)

    @Query(value = "SELECT * FROM CURRENCY_CODES_TABLE")
    suspend fun getCurrencyEntitiesStream(): List<CurrencyCodesEntity>

    @Update
    suspend fun updateCurrencyEntity(currencyEntity: CurrencyCodesEntity)

    @Query("DELETE FROM CURRENCY_CODES_TABLE")
    suspend fun deleteAllCurrencyEntities()
}
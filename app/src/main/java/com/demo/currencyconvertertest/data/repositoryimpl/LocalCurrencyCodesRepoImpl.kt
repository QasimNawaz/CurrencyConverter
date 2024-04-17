package com.demo.currencyconvertertest.data.repositoryimpl

import com.demo.currencyconvertertest.data.database.dao.CurrencyCodesDao
import com.demo.currencyconvertertest.data.repository.LocalCurrencyCodesRepo

class LocalCurrencyCodesRepoImpl(
    private val currencyDao: CurrencyCodesDao
) : LocalCurrencyCodesRepo {
    override suspend fun getCurrencyCodes(): Map<String, String> {
        return currencyDao.getCurrencyEntitiesStream().takeIf { it.isNotEmpty() }
            ?.first()?.currencies ?: emptyMap()
    }
}
package com.demo.currencyconvertertest.domain.usecase

import com.demo.currencyconvertertest.data.repository.LocalCurrencyCodesRepo
import com.demo.currencyconvertertest.domain.usecase.base.SuspendUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class LocalCurrencyCodesUseCase(
    private val localCurrenciesRepo: LocalCurrencyCodesRepo,
    private val ioDispatcher: CoroutineDispatcher,
) : SuspendUseCase<Unit, @JvmSuppressWildcards Flow<Map<String, String>>> {

    override suspend fun execute(params: Unit): Flow<Map<String, String>> {
        return flow {
            emit(localCurrenciesRepo.getCurrencyCodes())
        }.flowOn(ioDispatcher)
    }
}
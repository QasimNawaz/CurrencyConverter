package com.demo.currencyconvertertest.domain.usecase

import com.demo.currencyconvertertest.data.repository.RemoteCurrencyCodesRepo
import com.demo.currencyconvertertest.domain.usecase.base.SuspendUseCase
import com.demo.currencyconvertertest.domain.utils.ApiResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteCurrencyCodesUseCase(
    private val remoteCurrenciesRepo: RemoteCurrencyCodesRepo,
    private val ioDispatcher: CoroutineDispatcher,
) : SuspendUseCase<Unit, @JvmSuppressWildcards Flow<ApiResponse<Map<String, String>>>> {

    override suspend fun execute(params: Unit): Flow<ApiResponse<Map<String, String>>> {
        return flow {
            emit(remoteCurrenciesRepo.getCurrencyCodes())
        }.flowOn(ioDispatcher)
    }
}
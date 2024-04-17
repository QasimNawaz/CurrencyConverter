package com.demo.currencyconvertertest.domain.usecase

import com.demo.currencyconvertertest.data.database.entities.ConversionRatesEntity
import com.demo.currencyconvertertest.data.repository.RemoteConversionRatesRepo
import com.demo.currencyconvertertest.domain.usecase.base.SuspendUseCase
import com.demo.currencyconvertertest.domain.utils.ApiResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteConversionRatesUseCase(
    private val remoteRatesRepo: RemoteConversionRatesRepo,
    private val ioDispatcher: CoroutineDispatcher,
) : SuspendUseCase<Unit, @JvmSuppressWildcards Flow<ApiResponse<ConversionRatesEntity>>> {

    override suspend fun execute(params: Unit): Flow<ApiResponse<ConversionRatesEntity>> {
        return flow {
            emit(remoteRatesRepo.getConversionRates())
        }.flowOn(ioDispatcher)
    }
}
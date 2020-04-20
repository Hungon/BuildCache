package com.trials.mobiledeveloperchallenge

interface ExchangeRateRepository {

    suspend fun getCurrencyList(): ExchangeRateRepositoryImpl.Result<*>

    suspend fun getRealTimeRate(source: String): ExchangeRateRepositoryImpl.Result<*>
}

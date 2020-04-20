package com.trials.mobiledeveloperchallenge

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ExchangeRateRepositoryImpl(context: Context) : ExchangeRateRepository, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.IO
    private val scope = CoroutineScope(coroutineContext)
    private val url = context.getString(R.string.api_url)
    private val apiKey = context.getString(R.string.api_key)
    private var currentCurrency: Currency? = null
    private var currentRate: Rate? = null
    private var updatedAt = 0L
    private var updateJob: Job? = null

    sealed class Result<out T> {
        data class FetchedCurrency<out T>(val value: Currency) : Result<T>()
        data class FetchedRate<out T>(val value: Rate) : Result<T>()
        data class Failure(val message: String) : Result<Nothing>()
    }

    override suspend fun getCurrencyList() = suspendCoroutine<Result<*>> { continuation ->
        scope.launch {
            val endPoint = String.format("list?access_key=%s", apiKey)
            val apiUrl = String.format(url, endPoint)
            try {
                getResponse(apiUrl).use {
                    val result = when {
                        it.isSuccessful -> {
                            val response = it.body?.string()
                            Gson().fromJson(response, Currency::class.java)
                                ?.let { value ->
                                    if (value.success) {
                                        currentCurrency = value
                                        Result.FetchedCurrency<Currency>(value)
                                    } else {
                                        Result.Failure("error message: ${it.message}")
                                    }
                                } ?: Result.Failure("response data is null")
                        }
                        else -> {
                            Result.Failure("failed to retrieve currency list. reason -> ${it.message}")
                        }
                    }
                    continuation.resume(result)
                }
            } catch (e: IOException) {
                continuation.resume(Result.Failure("failed to execute api to fetch currency list. error -> ${e.message}"))
            }
        }
    }

    // Current Subscription Plan does not support Source Currency Switching.
    override suspend fun getRealTimeRate(source: String) =
        suspendCoroutine<Result<*>> { continuation ->
            if (currentRate != null && (updatedAt + LIMIT_BANDWIDTH_USAGE) > System.currentTimeMillis()) {
                continuation.resume(Result.FetchedRate<Rate>(currentRate!!))
                return@suspendCoroutine
            }
            updateJob?.run {
                if (isActive) {
                    continuation.resume(Result.FetchedRate<Rate>(currentRate!!))
                    return@suspendCoroutine
                }
            }
            Log.e("Exchange", "fetch updatedAt: $updatedAt")
            updateJob = scope.launch {
                val currenciesStr = currentCurrency?.currencies?.keys?.joinToString()
                val endPoint =
                    String.format("live?access_key=%s&currencies=%s&source=USD", apiKey, currenciesStr)
                val apiUrl = String.format(url, endPoint)
                try {
                    getResponse(apiUrl).use {
                        val result = when {
                            it.isSuccessful -> {
                                val response = it.body?.string()
                                Gson().fromJson(response, Rate::class.java)
                                    ?.let { value ->
                                        if (value.success) {
                                            updatedAt = System.currentTimeMillis()
                                            currentRate = value
                                            Result.FetchedRate<Rate>(value)
                                        } else {
                                            Result.Failure("error message: $response")
                                        }
                                    } ?: Result.Failure("response data is null")
                            }
                            else -> {
                                Result.Failure("failed to retrieve exchange rate. reason -> ${it.message}")
                            }
                        }
                        continuation.resume(result)
                    }
                } catch (e: IOException) {
                    continuation.resume(Result.Failure("failed to execute api to fetch exchange rate. error -> ${e.message}"))
                }
            }
        }

    private fun getResponse(apiUrl: String): Response {
        val client = OkHttpClient().newBuilder()
            .connectTimeout(TIMEOUT_IN_SEC, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_IN_SEC, java.util.concurrent.TimeUnit.SECONDS)
            .build()
        val request = Request.Builder()
            .url(apiUrl)
            .build()
        return client.newCall(request).execute()
    }

    companion object {
        private const val TIMEOUT_IN_SEC = 10L
        private const val LIMIT_BANDWIDTH_USAGE = 1_000L * 60 * 30
    }
}
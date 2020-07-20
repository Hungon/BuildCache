package com.trials.sample001.record

import android.content.Context
import com.google.gson.Gson
import com.trials.sample001.R
import com.trials.sample001.db.ContentEntity
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class RecordRepositoryImpl(context: Context) : RecordRepository, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.IO
    private val scope = CoroutineScope(coroutineContext)
    private val baseUrl = context.getString(R.string.api_base_url)
    private val realmConfiguration = RealmConfiguration.Builder().build()

    sealed class Result<out T> {
        data class FetchedRecords<out T>(val value: Record) : Result<T>()
        data class FetchedContents<out T>(val value: ContentEntity) : Result<T>()
        data class InsertedContent<out T>(val value: ContentEntity) : Result<T>()
        data class Failure(val message: String) : Result<Nothing>()
    }

    override suspend fun getRecordFromServer() = suspendCoroutine<Result<*>> { continuation ->
        val endPoint = "/mock/book/all"
        val url = baseUrl.plus(endPoint)
        scope.launch {
            try {
                getResponse(url).use {
                    val result = when {
                        it.isSuccessful -> {
                            val response = it.body?.string()
                            Gson().fromJson(response, Record::class.java)
                                ?.let { value ->
                                    Result.FetchedRecords<Record>(
                                        value
                                    )
                                } ?: Result.Failure(
                                "response data is null"
                            )
                        }
                        else -> {
                            Result.Failure(
                                "failed to retrieve records. reason -> ${it.message}"
                            )
                        }
                    }
                    continuation.resume(result)
                }
            } catch (e: IOException) {
                continuation.resume(
                    Result.Failure(
                        "failed to execute api to fetch records. error -> ${e.message}"
                    )
                )
            }
        }
    }

    override suspend fun getContentFromDbById(id: String) =
        suspendCoroutine<Result<*>> { continuation ->
            scope.launch {
                try {
                    Realm.getInstance(realmConfiguration).use { realm ->
                        realm.executeTransaction {
                            val content: ContentEntity? =
                                realm.where(ContentEntity::class.java).equalTo("idBook", id)
                                    .findFirst()
                            val res = content?.let {
                                Result.FetchedContents<ContentEntity>(value = content.copy())
                            } ?: Result.Failure("no data")
                            continuation.resume(res)
                        }
                    }
                } catch (e: IllegalStateException) {
                    continuation.resume(Result.Failure(message = e.message.toString()))
                }
            }
        }

    override suspend fun insertOrUpdate(contentEntity: ContentEntity) =
        suspendCoroutine<Result<*>> { continuation ->
            scope.launch {
                try {
                    Realm.getInstance(realmConfiguration).use { realm ->
                        realm.executeTransaction {
                            realm.insertOrUpdate(contentEntity)
                            continuation.resume(Result.InsertedContent<ContentEntity>(contentEntity))
                        }
                    }
                } catch (e: IllegalStateException) {
                    continuation.resume(Result.Failure(e.message.toString()))
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
    }
}
package com.trials.sample001.record

import com.trials.sample001.db.ContentEntity

interface RecordRepository  {
    suspend fun getRecordFromServer(): RecordRepositoryImpl.Result<*>
    suspend fun getContentFromDbById(id: String): RecordRepositoryImpl.Result<*>
    suspend fun insertOrUpdate(contentEntity: ContentEntity): RecordRepositoryImpl.Result<*>
}

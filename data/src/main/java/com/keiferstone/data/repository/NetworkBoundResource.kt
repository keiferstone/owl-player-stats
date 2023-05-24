package com.keiferstone.data.repository


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class NetworkBoundResource<DbDataType, DataType>(
    query: (CoroutineContext) -> Flow<DbDataType?>,
    private val map: suspend CoroutineScope.(DbDataType) -> DataType,
    private val fetch: suspend CoroutineScope.() -> DataType,
    private val persist: suspend (DataType) -> Unit,
    private val shouldFetch: suspend (DbDataType) -> Boolean,
    private val coroutineContext: CoroutineContext = Dispatchers.IO
) {
    val flow: Flow<DataType> = query(coroutineContext).map { data -> // Observe the query() Flow
        withContext(coroutineContext) {
            if (data == null || shouldFetch(data)) { // Check if local storage needs to be updated
                fetch().let { // Fetch remote data
                    persist(it) // Persist data
                    it // Emit data
                }
            } else map(data) // Emit Success with data from local storage
        }
    }
        .flowOn(coroutineContext) // Set dispatcher
        .distinctUntilChanged() // Prevent duplicate emissions

    suspend fun forceRefresh() = withContext(coroutineContext) { persist(fetch()) } // Fetch remote data and store it locally
}
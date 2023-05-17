package com.keiferstone.data.api

import com.keiferstone.data.model.Summary
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface OwlApi {
    @GET("owl/v1/owl2")
    suspend fun getSummary(
        @Header("Authorization") authorization: String,
        @Query("region") region: String = "us"): Summary
}
package com.keiferstone.data.api

import com.keiferstone.data.model.PlayerDetail
import com.keiferstone.data.model.Summary
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface OwlApi {
    @GET("owl/v1/owl2")
    suspend fun getSummary(
        @Header("Authorization") authorization: String,
        @Query("region") region: String = "us"): Summary

    @GET("owl/v1/players")
    suspend fun getPlayer(
        @Header("Authorization") authorization: String,
        @Query("region") region: String = "us",
        @Query("playerId") playerId: Long): PlayerDetail
}
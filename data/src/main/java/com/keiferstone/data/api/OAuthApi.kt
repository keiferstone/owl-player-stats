package com.keiferstone.data.api

import com.keiferstone.data.model.AccessToken
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface OAuthApi {
    @FormUrlEncoded
    @POST("token")
    suspend fun requestAccessToken(
        @Field("grant_type") grantType: String = "client_credentials") : AccessToken
}
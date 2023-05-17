package com.keiferstone.data.api

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

class OwlPlayerStatsClient @Inject constructor() {
    private val authOkHttpClient = OkHttpClient.Builder()
        .addInterceptor(BasicAuthInterceptor(CLIENT_ID, CLIENT_SECRET))
        .build()

    val oAuthClient: OAuthApi = Retrofit.Builder()
        .baseUrl("https://oauth.battle.net/")
        .addConverterFactory(MoshiConverterFactory.create())
        .client(authOkHttpClient)
        .build()
        .create(OAuthApi::class.java)

    val owlClient: OwlApi = Retrofit.Builder()
        .baseUrl("https://us.api.blizzard.com/") // TODO: Localization
        .addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder()
                    .add(SummaryAdapter())
                    .build()))
        .build()
        .create(OwlApi::class.java)

    companion object {
        private const val CLIENT_ID = "8a7d2d6926c9433abd3ee0e9bd96bdf4"
        // FIXME: MOVE SECRET TO READ FROM ENV AT BUILD TIME, THEN DESTROY THIS ONE
        private const val CLIENT_SECRET = "If6XyA4GmE2afbXb8flrDXTN1McFLFw4"
    }
}
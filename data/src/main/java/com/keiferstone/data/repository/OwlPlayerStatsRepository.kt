package com.keiferstone.data.repository

import android.util.Log
import com.keiferstone.data.api.OwlPlayerStatsClient
import com.keiferstone.data.model.AccessToken
import com.keiferstone.data.model.PlayerDetail
import com.keiferstone.data.model.Summary
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OwlPlayerStatsRepository @Inject constructor(private val client: OwlPlayerStatsClient) {
    private var accessToken: AccessToken? = null

    private suspend fun requireAccessToken(): AccessToken {
        if (accessToken == null) {
            accessToken = client.oAuthClient.requestAccessToken()
        }
        return accessToken!!
    }

    suspend fun getSummary(): Summary? {
        return runCatching {
            requireAccessToken().let { accessToken ->
                client.owlClient.getSummary(
                    authorization = accessToken.toBearerString()) // TODO: Localization
            }
        }.getOrElse {
            Log.e("ops", "Error getting summary: $it", it)
            null
        }
    }

    suspend fun getPlayer(playerId: Long): PlayerDetail? {
        return runCatching {
            requireAccessToken().let { accessToken ->
                client.owlClient.getPlayer(
                    authorization = accessToken.toBearerString(),
                    playerId = playerId) // TODO: Localization
            }
        }.getOrElse {
            Log.e("ops", "Error getting summary: $it", it)
            null
        }
    }
}
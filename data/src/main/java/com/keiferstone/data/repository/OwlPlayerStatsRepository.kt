package com.keiferstone.data.repository

import android.util.Log
import com.keiferstone.data.api.OwlPlayerStatsClient
import com.keiferstone.data.extension.isStale
import com.keiferstone.data.extension.toDbRow
import com.keiferstone.data.extension.toPlayerDetail
import com.keiferstone.data.extension.toPlayerDetailTeam
import com.keiferstone.data.model.AccessToken
import com.keiferstone.data.model.PlayerDetail
import com.keiferstone.data.model.Summary
import com.keiferstone.owlplayerstats.Database
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
class OwlPlayerStatsRepository @Inject constructor(
    private val client: OwlPlayerStatsClient,
    private val database: Database) {
    private var accessToken: AccessToken? = null

    private suspend fun requireAccessToken(): AccessToken {
        if (accessToken == null) {
            accessToken = client.oAuthClient.requestAccessToken()
        }
        return accessToken!!
    }

    suspend fun getSummary(forceRefresh: Boolean = false): Summary? = withContext(Dispatchers.IO) {
        client.owlClient.getSummary(
            authorization = requireAccessToken().toBearerString()).also {
            it.players.forEach {
                // TODO: database.playerQueries.insert()
            }
            it.teams.forEach { team ->
                database.teamQueries.insert(team.toDbRow())
            }
        } // TODO: Localization
    }

    suspend fun getPlayer(playerId: Long, forceRefresh: Boolean = false): PlayerDetail? = withContext(Dispatchers.IO) {
        Log.d("ops", "getPlayer($playerId, $forceRefresh)")

        var player = if (!forceRefresh) {
            // Check cache
            database.playerQueries.selectById(playerId).executeAsOneOrNull()?.let { cachedPlayer ->
                if (!cachedPlayer.isStale()) cachedPlayer.toPlayerDetail { teamIds ->
                    database.teamQueries.selectByIds(teamIds).executeAsList().map { cachedTeam ->
                        cachedTeam.toPlayerDetailTeam()
                    }
                }
                else null
            }
        } else null
        Log.d("ops", "cached player = $player")

        player = if (forceRefresh || player == null) {
            Log.d("ops", "refreshing player")
            // Fetch from api
            client.owlClient.getPlayer(
                authorization = requireAccessToken().toBearerString(),
                playerId = playerId
            ).also {
                database.playerQueries.insert(it.toDbRow())
            } // TODO: Localization
        } else player
        Log.d("ops", "player = $player")
        player
    }
}
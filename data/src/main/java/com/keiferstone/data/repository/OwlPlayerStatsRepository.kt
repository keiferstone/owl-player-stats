package com.keiferstone.data.repository


import android.util.Log
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.keiferstone.data.api.OwlPlayerStatsClient
import com.keiferstone.data.db.Summary_ids
import com.keiferstone.data.extension.hasDetails
import com.keiferstone.data.extension.isStale
import com.keiferstone.data.extension.toDbRow
import com.keiferstone.data.extension.toPlayerDetail
import com.keiferstone.data.extension.toPlayerDetailTeam
import com.keiferstone.data.extension.toPlayerSummary
import com.keiferstone.data.extension.toSummaryIds
import com.keiferstone.data.extension.toTeamSummary
import com.keiferstone.data.model.AccessToken
import com.keiferstone.data.model.PlayerDetail
import com.keiferstone.data.model.Summary
import com.keiferstone.data.model.SummaryIds
import com.keiferstone.owlplayerstats.Database
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@Singleton
class OwlPlayerStatsRepository @Inject constructor(
    private val client: OwlPlayerStatsClient,
    private val database: Database) {
    private var accessToken: AccessToken? = null

    val summaryResource = NetworkBoundResource(
        query = { database.summaryIdsQueries.select().asFlow().mapToOneOrNull(it) },
        map = { it.toSummaryIds() },
        fetch = {
            client.owlClient.getSummary(
                authorization = requireAccessToken().toBearerString()
            ).toSummaryIds()
        },
        persist = { database.summaryIdsQueries.insert(it.toDbRow()) },
        shouldFetch = { it.isStale() || it.player_ids.isNullOrEmpty() || it.team_ids.isNullOrEmpty() }
    )

    val allPlayerDetailsResource = NetworkBoundResource(
        query = { dispatcher ->
            database.playerQueries.selectAll().asFlow().mapToList(dispatcher)
        },
        map = {
            it.map { player ->
                player.toPlayerDetail { teamIds ->
                    database.teamQueries.selectByIds(teamIds).executeAsList().map { team ->
                        team.toPlayerDetailTeam()
                    }
                }
            }
        },
        fetch = {
            summaryResource.flow.first().playerIds.map { playerId ->
                async {
                    client.owlClient.getPlayer(
                        authorization = requireAccessToken().toBearerString(),
                        playerId = playerId
                    )
                }
            }.awaitAll()
        },
        persist = {
            database.transaction {
                it.forEach { player ->
                    database.playerQueries.insert(player.toDbRow())
                    player.teams.forEach { team ->
                        database.teamQueries.insert(team.toDbRow())
                    }
                }
            }
        },
        shouldFetch = {
            it.any { player -> player.isStale() }
        }
    )

    private suspend fun requireAccessToken(): AccessToken {
        if (accessToken == null) {
            accessToken = client.oAuthClient.requestAccessToken()
        }
        return accessToken!!
    }

    suspend fun getPlayerDetail(playerId: Long, forceRefresh: Boolean = false): PlayerDetail = withContext(Dispatchers.IO) {
        /*
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

        player = if (forceRefresh || player == null) {
            // Fetch from api
            client.owlClient.getPlayer(
                authorization = requireAccessToken().toBearerString(),
                playerId = playerId
            ).also {
                database.playerQueries.insert(it.toDbRow())
            }
        } else player
        player

         */
        client.owlClient.getPlayer(
            authorization = requireAccessToken().toBearerString(),
            playerId = playerId
        )
    }
}
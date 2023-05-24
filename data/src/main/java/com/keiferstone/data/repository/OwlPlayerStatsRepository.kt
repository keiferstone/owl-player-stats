package com.keiferstone.data.repository


import android.util.Log
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.keiferstone.data.api.OwlPlayerStatsClient
import com.keiferstone.data.extension.isStale
import com.keiferstone.data.extension.toDbRow
import com.keiferstone.data.extension.toPlayerDetail
import com.keiferstone.data.extension.toPlayerDetailTeam
import com.keiferstone.data.extension.toSummaryIds
import com.keiferstone.data.model.AccessToken
import com.keiferstone.data.model.PlayerDetail
import com.keiferstone.owlplayerstats.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

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
        query = { database.playerQueries.selectAll().asFlow().mapToList(it) },
        map = {
            database.teamQueries.transactionWithResult {
                it.map { player ->
                    player.toPlayerDetail { teamIds ->
                        database.teamQueries.selectByIds(teamIds).executeAsList().map { team ->
                            team.toPlayerDetailTeam()
                        }
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
        shouldFetch = { it.isEmpty() || it.any { player -> player.isStale() } }
    )

    fun playerDetailResource(playerId: Long) = NetworkBoundResource(
        query = { database.playerQueries.selectById(playerId).asFlow().mapToOneOrNull(it) },
        map = {
            database.teamQueries.transactionWithResult {
                it.toPlayerDetail { teamIds ->
                    database.teamQueries.selectByIds(teamIds).executeAsList().map { team ->
                        team.toPlayerDetailTeam()
                    }
                }
            }
        },
        fetch = {
            client.owlClient.getPlayer(
                authorization = requireAccessToken().toBearerString(),
                playerId = playerId
            )
        },
        persist = { database.playerQueries.insert(it.toDbRow()) },
        shouldFetch = { it.isStale() }
    )

    private suspend fun requireAccessToken(): AccessToken {
        if (accessToken == null) {
            accessToken = client.oAuthClient.requestAccessToken()
        }
        return accessToken!!
    }
}
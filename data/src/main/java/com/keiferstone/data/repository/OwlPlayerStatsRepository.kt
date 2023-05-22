package com.keiferstone.data.repository

import android.util.Log
import com.keiferstone.data.api.OwlPlayerStatsClient
import com.keiferstone.data.extension.hasDetails
import com.keiferstone.data.extension.isStale
import com.keiferstone.data.extension.toDbRow
import com.keiferstone.data.extension.toPlayerDetail
import com.keiferstone.data.extension.toPlayerDetailTeam
import com.keiferstone.data.extension.toPlayerSummary
import com.keiferstone.data.extension.toTeamSummary
import com.keiferstone.data.model.AccessToken
import com.keiferstone.data.model.PlayerDetail
import com.keiferstone.data.model.Summary
import com.keiferstone.owlplayerstats.Database
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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

    suspend fun getSummary(forceRefresh: Boolean = false): Summary = withContext(Dispatchers.IO) {
        var summary = if (!forceRefresh) {
            // Check cache
            database.summaryQueries.select().executeAsOneOrNull()?.let { cachedSummary ->
                if (!cachedSummary.isStale()) {
                    Summary(
                        players = database.playerQueries.selectAll().executeAsList().map { it.toPlayerSummary() },
                        teams = database.teamQueries.selectAll().executeAsList().mapNotNull { it.toTeamSummary() },
                        matches = emptyList(),
                        segments = emptyList()
                    )
                } else null
            }
        } else null

        if (forceRefresh || summary == null) {
            summary = client.owlClient.getSummary(
                authorization = requireAccessToken().toBearerString()
            )
            database.summaryQueries.insert(System.currentTimeMillis())
            database.playerQueries.transaction {
                summary.players.forEach { player ->
                    database.playerQueries.insertSummary(
                        id = player.id,
                        name = player.name,
                        number = player.number,
                        role = player.role,
                        preferred_slot = player.preferredSlot,
                        current_teams = listOfNotNull(player.currentTeam),
                        given_name = player.givenName,
                        family_name = player.familyName,
                        headshot_url = player.headshotUrl
                    )
                }
            }
            database.teamQueries.transaction {
                summary.teams.forEach { team ->
                    database.teamQueries.insert(team.toDbRow())
                }
            }
        }
        summary
    }

    suspend fun getPlayerDetail(playerId: Long, forceRefresh: Boolean = false): PlayerDetail = withContext(Dispatchers.IO) {
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
    }

    suspend fun getPlayerDetails(
        playerIds: List<Long>,
        forceRefresh: Boolean = false): List<PlayerDetail> = withContext(Dispatchers.IO) {

        val players = mutableListOf<PlayerDetail>()
        if (!forceRefresh) {
            database.playerQueries.transactionWithResult {
                playerIds.forEach { playerId ->
                    database.playerQueries.selectById(playerId).executeAsOneOrNull()?.let { cachedPlayer ->
                        if (!cachedPlayer.isStale()) {
                            cachedPlayer.toPlayerDetail { teamIds ->
                                database.teamQueries.transactionWithResult {
                                    database.teamQueries.selectByIds(teamIds).executeAsList().map { cachedTeam ->
                                        cachedTeam.toPlayerDetailTeam()
                                    }
                                }
                            }.also {
                                players.add(it)
                            }
                        }
                    }
                }
            }
        }

        playerIds.mapNotNull { playerId ->
            val player = players.find { it.id == playerId }
            if (forceRefresh || player == null || !player.hasDetails()) {
                // Fetch from api
                async {
                    client.owlClient.getPlayer(
                        authorization = requireAccessToken().toBearerString(),
                        playerId = playerId
                    )
                }
            } else null
        }.awaitAll().also { playerDetails ->
            players.addAll(playerDetails)
            database.playerQueries.transaction {
                playerDetails.forEach {
                    database.playerQueries.insert(it.toDbRow())
                }
            }
        }
        players
    }
}
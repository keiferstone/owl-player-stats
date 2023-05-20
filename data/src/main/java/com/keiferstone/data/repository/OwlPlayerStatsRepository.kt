package com.keiferstone.data.repository

import android.util.Log
import com.keiferstone.data.api.OwlPlayerStatsClient
import com.keiferstone.data.extension.hasStats
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
        Log.d("ops", "getSummary($forceRefresh)")

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
                    Log.d("ops", "inserting player summary $player")
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
                    Log.d("ops", "inserting team summary $team")
                    database.teamQueries.insert(team.toDbRow())
                }
            }
        }
        summary
    }

    suspend fun getPlayerDetail(playerId: Long, forceRefresh: Boolean = false): PlayerDetail = withContext(Dispatchers.IO) {
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
            }
        } else player
        Log.d("ops", "player = $player")
        player
    }

    suspend fun getPlayerDetails(
        playerIds: List<Long>,
        forceRefresh: Boolean = false): List<PlayerDetail> = withContext(Dispatchers.IO) {
        Log.d("ops", "getPlayerDetails(${playerIds.joinToString()}, $forceRefresh)")

        val players = mutableListOf<PlayerDetail>()
        if (!forceRefresh) {
            Log.d("ops", "checking player cache")
            database.playerQueries.transactionWithResult {
                playerIds.forEach { playerId ->
                    database.playerQueries.selectById(playerId).executeAsOneOrNull()?.let { cachedPlayer ->
                        Log.d("ops", "loaded cached player with id $playerId")
                        if (!cachedPlayer.isStale()) {
                            Log.d("ops", "cached player not stale")
                            cachedPlayer.toPlayerDetail { teamIds ->
                                database.teamQueries.transactionWithResult {
                                    Log.d("ops", "loading teams ${teamIds.joinToString()}")
                                    database.teamQueries.selectByIds(teamIds).executeAsList().map { cachedTeam ->
                                        Log.d("ops", "loaded cached team with id ${cachedTeam.id}")
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
        Log.d("ops", "loaded ${players.size} players")

        playerIds.mapNotNull { playerId ->
            val player = players.find { it.id == playerId }
            if (forceRefresh || player == null || !player.hasStats()) {
                Log.d("ops", "fetching player with id $playerId")
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
package com.keiferstone.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlayerDetail(
    val id: Long,
    val name: String,
    val number: Long,
    val role: String?,
    val preferredSlot: Long,
    val currentTeams: List<Long>,
    val givenName: String,
    val familyName: String,
    val headshotUrl: String?,
    val teams: List<PlayerDetailTeam>) {
    val currentTeam: PlayerDetailTeam? = teams.find { it.id == currentTeams.firstOrNull() }
}
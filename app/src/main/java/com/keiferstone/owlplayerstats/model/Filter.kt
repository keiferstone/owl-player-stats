package com.keiferstone.owlplayerstats.model

import android.content.res.Resources
import com.keiferstone.data.extension.hasStats
import com.keiferstone.data.model.PlayerDetail
import com.keiferstone.data.model.PlayerRoles
import com.keiferstone.data.model.PlayerSummary
import com.keiferstone.data.model.StatType
import com.keiferstone.owlplayerstats.R
import com.keiferstone.owlplayerstats.extension.extractValue
import com.keiferstone.owlplayerstats.extension.nameResId

sealed class Filter(val id: String) {
    open fun checkPlayer(player: PlayerSummary): Boolean = false
    open fun checkPlayer(player: PlayerDetail): Boolean = false

    open fun toString(resources: Resources) = when(this) {
        OnTeam -> resources.getString(R.string.on_team)
        is PlaysRole -> when (role) {
            PlayerRoles.TANK -> resources.getString(R.string.tank)
            PlayerRoles.DPS -> resources.getString(R.string.dps)
            PlayerRoles.SUPPORT -> resources.getString(R.string.support)
            else -> ""
        }
        HasStats -> resources.getString(R.string.has_stats)
        is HasStat -> resources.getString(R.string.has_stat, resources.getString(statType.nameResId()))
        is TimePlayed -> resources.getString(R.string.time_played_min, seconds / 60)
    }

    object OnTeam : Filter(ON_TEAM) {
        override fun checkPlayer(player: PlayerSummary) = player.currentTeam != null
        override fun checkPlayer(player: PlayerDetail) = player.currentTeam != null
    }

    data class PlaysRole(val role: String) : Filter(PLAYS_ROLE + role) {
        override fun checkPlayer(player: PlayerSummary) = player.role == role
        override fun checkPlayer(player: PlayerDetail) = player.role == role
    }

    object HasStats : Filter(HAS_STATS) {
        override fun checkPlayer(player: PlayerSummary) = true
        override fun checkPlayer(player: PlayerDetail) = player.hasStats()
    }

    data class HasStat(val statType: StatType) : Filter(HAS_STAT + statType) {
        override fun checkPlayer(player: PlayerSummary) = true
        override fun checkPlayer(player: PlayerDetail) = player.stats.extractValue(statType) != null
    }

    data class TimePlayed(val seconds: Long) : Filter(TIME_PLAYED + seconds) {
        override fun checkPlayer(player: PlayerSummary) = true
        override fun checkPlayer(player: PlayerDetail) = (player.stats.timePlayed ?: 0) > (5 * 60)
    }

    companion object {
        const val ON_TEAM = "on-team"
        const val PLAYS_ROLE = "plays-role-"
        const val HAS_STATS = "has-stats"
        const val HAS_STAT = "has-stat-"
        const val TIME_PLAYED = "time-played-"
    }
}
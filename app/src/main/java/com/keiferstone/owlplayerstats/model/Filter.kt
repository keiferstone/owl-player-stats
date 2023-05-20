package com.keiferstone.owlplayerstats.model

import com.keiferstone.data.extension.hasStats
import com.keiferstone.data.model.PlayerDetail
import com.keiferstone.data.model.PlayerRoles
import com.keiferstone.data.model.PlayerSummary
import com.keiferstone.data.model.StatType
import com.keiferstone.owlplayerstats.extension.extractValue

sealed class Filter {
    open fun checkPlayer(player: PlayerSummary): Boolean = false
    open fun checkPlayer(player: PlayerDetail): Boolean = false

    object OnTeam : Filter() {
        override fun checkPlayer(player: PlayerSummary) = player.currentTeam != null
        override fun checkPlayer(player: PlayerDetail) = player.currentTeam != null
    }

    object PlaysTank : Filter() {
        override fun checkPlayer(player: PlayerSummary) = player.role == PlayerRoles.TANK
        override fun checkPlayer(player: PlayerDetail) = player.role == PlayerRoles.TANK
    }

    object PlaysDps : Filter() {
        override fun checkPlayer(player: PlayerSummary) = player.role == PlayerRoles.DPS
        override fun checkPlayer(player: PlayerDetail) = player.role == PlayerRoles.DPS
    }

    object PlaysSupport : Filter() {
        override fun checkPlayer(player: PlayerSummary) = player.role == PlayerRoles.SUPPORT
        override fun checkPlayer(player: PlayerDetail) = player.role == PlayerRoles.SUPPORT
    }

    object HasStats : Filter() {
        override fun checkPlayer(player: PlayerSummary) = true
        override fun checkPlayer(player: PlayerDetail) = player.hasStats()
    }

    data class HasStat(private val statType: StatType) : Filter() {
        override fun checkPlayer(player: PlayerSummary) = true
        override fun checkPlayer(player: PlayerDetail) = player.stats.extractValue(statType) != null
    }

    object PlayedFiveMinutes : Filter() {
        override fun checkPlayer(player: PlayerSummary) = true
        override fun checkPlayer(player: PlayerDetail) = (player.stats.timePlayed ?: 0) > (5 * 60)
    }
}
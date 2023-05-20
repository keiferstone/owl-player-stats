package com.keiferstone.owlplayerstats.model

import com.keiferstone.data.extension.hasDetails
import com.keiferstone.data.extension.hasStats
import com.keiferstone.data.model.PlayerDetail
import com.keiferstone.data.model.PlayerRoles
import com.keiferstone.data.model.PlayerSummary
import java.lang.UnsupportedOperationException

sealed class PlayerFilter {
    open fun checkPlayer(player: PlayerSummary): Boolean = false
    open fun checkPlayer(player: PlayerDetail): Boolean = false

    object OnTeam : PlayerFilter() {
        override fun checkPlayer(player: PlayerSummary) = player.currentTeam != null
        override fun checkPlayer(player: PlayerDetail) = player.currentTeam != null
    }

    object PlaysTank : PlayerFilter() {
        override fun checkPlayer(player: PlayerSummary) = player.role == PlayerRoles.TANK
        override fun checkPlayer(player: PlayerDetail) = player.role == PlayerRoles.TANK
    }

    object PlaysDps : PlayerFilter() {
        override fun checkPlayer(player: PlayerSummary) = player.role == PlayerRoles.DPS
        override fun checkPlayer(player: PlayerDetail) = player.role == PlayerRoles.DPS
    }

    object PlaysSupport : PlayerFilter() {
        override fun checkPlayer(player: PlayerSummary) = player.role == PlayerRoles.SUPPORT
        override fun checkPlayer(player: PlayerDetail) = player.role == PlayerRoles.SUPPORT
    }

    object HasStats : PlayerFilter() {
        override fun checkPlayer(player: PlayerSummary) = true
        override fun checkPlayer(player: PlayerDetail) = player.hasStats()
    }

    object PlayedFiveMinutes : PlayerFilter() {
        override fun checkPlayer(player: PlayerSummary) = true
        override fun checkPlayer(player: PlayerDetail) = (player.stats.timePlayed ?: 0) > (5 * 60)
    }
}
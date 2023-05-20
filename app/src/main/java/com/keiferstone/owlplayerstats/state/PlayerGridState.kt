package com.keiferstone.owlplayerstats.state

import com.keiferstone.data.model.PlayerRoles
import com.keiferstone.data.model.PlayerSummary
import com.keiferstone.data.model.TeamSummary

sealed class PlayerGridState {
    object Loading : PlayerGridState()
    data class Content(val data: List<PlayerDatum>) : PlayerGridState()
    data class Error(val message: String? = null) : PlayerGridState()
}

sealed class PlayerFilter {
    abstract fun checkPlayer(player: PlayerSummary): Boolean
    object OnTeam : PlayerFilter() {
        override fun checkPlayer(player: PlayerSummary) = player.currentTeam != null
    }

    object PlaysTank : PlayerFilter() {
        override fun checkPlayer(player: PlayerSummary) = player.role == PlayerRoles.TANK
    }

    object PlaysDps : PlayerFilter() {
        override fun checkPlayer(player: PlayerSummary) = player.role == PlayerRoles.DPS
    }

    object PlaysSupport : PlayerFilter() {
        override fun checkPlayer(player: PlayerSummary) = player.role == PlayerRoles.SUPPORT
    }
}

data class PlayerDatum(val player: PlayerSummary, val team: TeamSummary?)
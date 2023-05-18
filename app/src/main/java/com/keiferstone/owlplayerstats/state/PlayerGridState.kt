package com.keiferstone.owlplayerstats.state

import com.keiferstone.data.model.PlayerSummary
import com.keiferstone.data.model.TeamSummary

sealed class PlayerGridState {
    object Loading : PlayerGridState()
    data class Content(val playerData: List<PlayerDatum>) : PlayerGridState()
    data class Error(val message: String? = null) : PlayerGridState()
}

data class PlayerDatum(val player: PlayerSummary, val team: TeamSummary?)
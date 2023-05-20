package com.keiferstone.owlplayerstats.state

import com.keiferstone.data.model.PlayerDetail
import com.keiferstone.data.model.PlayerRoles
import com.keiferstone.data.model.PlayerSummary
import com.keiferstone.data.model.TeamSummary

sealed class PlayerGridState {
    object Loading : PlayerGridState()
    data class Content(val players: List<PlayerDetail>) : PlayerGridState()
    data class Error(val message: String? = null) : PlayerGridState()
}
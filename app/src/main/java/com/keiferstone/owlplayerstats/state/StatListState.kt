package com.keiferstone.owlplayerstats.state

import com.keiferstone.data.model.PlayerSummary
import com.keiferstone.data.model.TeamSummary

sealed class StatListState {
    object Loading : StatListState()
    data class Content(val playerData: List<PlayerDatum>) : StatListState()
    data class Error(val message: String? = null) : StatListState()
}
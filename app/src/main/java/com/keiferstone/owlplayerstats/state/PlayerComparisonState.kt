package com.keiferstone.owlplayerstats.state

import com.keiferstone.data.model.PlayerDetail

sealed class PlayerComparisonState {
    object Loading : PlayerComparisonState()
    data class Content(val player1: PlayerDetail, val player2: PlayerDetail) : PlayerComparisonState()
    data class Error(val message: String? = null) : PlayerComparisonState()
}
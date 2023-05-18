package com.keiferstone.owlplayerstats.state

import com.keiferstone.data.model.PlayerDetail

sealed class PlayerDetailState {
    object Loading : PlayerDetailState()
    data class Content(val player: PlayerDetail) : PlayerDetailState()
    data class Error(val message: String? = null) : PlayerDetailState()
}
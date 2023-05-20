package com.keiferstone.owlplayerstats.state


import com.keiferstone.data.model.PlayerDetail

sealed class StatDetailState {
    object Loading : StatDetailState()
    data class Content(val players: List<PlayerDetail>) : StatDetailState()
    data class Error(val message: String? = null) : StatDetailState()
}
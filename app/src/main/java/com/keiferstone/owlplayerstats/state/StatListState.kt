package com.keiferstone.owlplayerstats.state


import com.keiferstone.data.model.PlayerDetail
import com.keiferstone.data.model.StatType

sealed class StatListState {
    object Loading : StatListState()
    data class Content(val data: List<StatLeaderDatum>) : StatListState()
    data class Error(val message: String? = null) : StatListState()
}

data class StatLeaderDatum(val statType: StatType, val leaders: List<PlayerDetail>)
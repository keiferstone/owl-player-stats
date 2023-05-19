package com.keiferstone.owlplayerstats.state

import androidx.annotation.StringRes
import com.keiferstone.data.model.PlayerDetail
import com.keiferstone.data.model.PlayerSummary
import com.keiferstone.data.model.TeamSummary

sealed class StatListState {
    object Loading : StatListState()
    data class Content(val data: List<StatLeaderDatum>) : StatListState()
    data class Error(val message: String? = null) : StatListState()
}

data class StatLeaderDatum(@StringRes val nameResId: Int, val leaders: List<PlayerDetail>)
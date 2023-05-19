package com.keiferstone.owlplayerstats.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keiferstone.data.model.PlayerDetail
import com.keiferstone.data.model.StatType
import com.keiferstone.data.repository.OwlPlayerStatsRepository
import com.keiferstone.owlplayerstats.extension.nameResId
import com.keiferstone.owlplayerstats.state.StatLeaderDatum
import com.keiferstone.owlplayerstats.state.StatListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class StatListViewModel @Inject constructor(private val repository: OwlPlayerStatsRepository) : ViewModel() {
    val uiState = MutableStateFlow<StatListState>(StatListState.Loading)

    init {
        viewModelScope.launch {
            runCatching {
                repository.getSummary()?.let { summary ->
                    val playerDetails = summary.players.map { playerSummary ->
                        async { repository.getPlayer(playerSummary.id) }
                    }.awaitAll()
                    val data = StatType.allStatTypes().map { statType ->
                        //StatLeaderDatum(statType.nameResId(), )
                    }
                } ?: run {
                    uiState.value = StatListState.Error()
                }
            }.getOrElse {
                uiState.value = StatListState.Error(it.message)
            }
        }
    }

    private fun List<PlayerDetail>.top5(statType: StatType) {

    }
}
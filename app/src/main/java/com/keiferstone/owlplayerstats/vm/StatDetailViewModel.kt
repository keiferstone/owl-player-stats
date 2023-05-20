package com.keiferstone.owlplayerstats.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keiferstone.data.model.PlayerDetail
import com.keiferstone.data.model.StatType
import com.keiferstone.data.repository.OwlPlayerStatsRepository
import com.keiferstone.owlplayerstats.extension.extractValue
import com.keiferstone.owlplayerstats.model.PlayerFilter
import com.keiferstone.owlplayerstats.state.StatDetailState
import com.keiferstone.owlplayerstats.state.StatLeaderDatum
import com.keiferstone.owlplayerstats.state.StatListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class StatDetailViewModel @Inject constructor(private val repository: OwlPlayerStatsRepository) : ViewModel() {
    val uiState = MutableStateFlow<StatDetailState>(StatDetailState.Loading)

    init {
        loadPlayerDetails()
    }

    fun filterPlayers(filters: List<PlayerFilter>) = loadPlayerDetails(filters)

    private fun loadPlayerDetails(filters: List<PlayerFilter> = emptyList()) {
        viewModelScope.launch {
            runCatching {
                repository.getSummary().let { summary ->
                    val players = repository.getPlayerDetails(
                        playerIds = summary.players
                            .filter { playerSummary ->
                                if (filters.isEmpty()) true
                                else filters.all { it.checkPlayer(playerSummary) }
                            }
                            .map { it.id }
                    ).filter { playerDetail ->
                        if (filters.isEmpty()) true
                        else filters.all { it.checkPlayer(playerDetail) }
                    }
                    uiState.value = StatDetailState.Content(players)
                }
            }.getOrElse {
                uiState.value = StatDetailState.Error(it.message)
            }
        }
    }
}
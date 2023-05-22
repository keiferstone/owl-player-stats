package com.keiferstone.owlplayerstats.vm


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keiferstone.data.model.PlayerDetail
import com.keiferstone.data.model.StatType
import com.keiferstone.data.repository.OwlPlayerStatsRepository
import com.keiferstone.owlplayerstats.extension.extractValue
import com.keiferstone.owlplayerstats.model.Filter
import com.keiferstone.owlplayerstats.state.StatLeaderDatum
import com.keiferstone.owlplayerstats.state.StatListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class StatListViewModel @Inject constructor(private val repository: OwlPlayerStatsRepository) : ViewModel() {
    val uiState = MutableStateFlow<StatListState>(StatListState.Loading)

    init {
        loadPlayerDetails()
    }

    fun filterData(filters: List<Filter>) = loadPlayerDetails(filters)

    private fun loadPlayerDetails(filters: List<Filter> = emptyList()) {
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
                    val data = StatType.allStatTypes().map { statType ->
                        StatLeaderDatum(statType, players.top5(statType))
                    }
                    uiState.value = StatListState.Content(data)
                }
            }.getOrElse {
                uiState.value = StatListState.Error(it.message)
            }
        }
    }

    private fun List<PlayerDetail>.top5(statType: StatType): List<PlayerDetail> {
        return sortedByDescending {
            it.stats.extractValue(statType)
        }.take(5)
    }
}
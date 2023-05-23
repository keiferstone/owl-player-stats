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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class StatListViewModel @Inject constructor(repository: OwlPlayerStatsRepository) : ViewModel() {
    private val selectedFilters = MutableStateFlow(emptyList<Filter>())

    val uiState = combine(repository.allPlayerDetailsResource.flow, selectedFilters) { playerDetails, selectedFilters ->
        playerDetails.filter { playerSummary ->
            if (selectedFilters.isEmpty()) true
            else selectedFilters.all { it.checkPlayer(playerSummary) }
        }
    }
    .map<List<PlayerDetail>, StatListState> { playerDetails ->
        val data = StatType.allStatTypes().map { statType ->
            StatLeaderDatum(statType, playerDetails.top5(statType))
        }
        StatListState.Content(data)
    }
    .catch { emit(StatListState.Error(it.message)) }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = StatListState.Loading)

    fun filterData(filters: List<Filter>) {
        selectedFilters.value = filters
    }

    private fun List<PlayerDetail>.top5(statType: StatType): List<PlayerDetail> {
        return sortedByDescending {
            it.stats.extractValue(statType)
        }.take(5)
    }
}
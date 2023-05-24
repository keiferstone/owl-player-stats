package com.keiferstone.owlplayerstats.vm


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keiferstone.data.model.PlayerDetail
import com.keiferstone.data.repository.OwlPlayerStatsRepository
import com.keiferstone.owlplayerstats.model.Filter
import com.keiferstone.owlplayerstats.state.PlayerGridState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class PlayerGridViewModel @Inject constructor(repository: OwlPlayerStatsRepository) : ViewModel() {
    private val selectedFilters = MutableStateFlow(emptyList<Filter>())

    val uiState = combine(repository.allPlayerDetailsResource.flow, selectedFilters) { playerDetails, selectedFilters ->
        playerDetails.filter { playerSummary ->
            if (selectedFilters.isEmpty()) true
            else selectedFilters.all { it.checkPlayer(playerSummary) }
        }
    }
    .map<List<PlayerDetail>, PlayerGridState> {
        PlayerGridState.Content(it)
    }
    .catch { emit(PlayerGridState.Error(it.message)) }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = PlayerGridState.Loading)

    fun filterData(filters: List<Filter>) {
        selectedFilters.value = filters
    }
}
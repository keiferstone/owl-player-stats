package com.keiferstone.owlplayerstats.vm


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keiferstone.data.repository.OwlPlayerStatsRepository
import com.keiferstone.owlplayerstats.model.Filter
import com.keiferstone.owlplayerstats.state.PlayerGridState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PlayerGridViewModel @Inject constructor(private val repository: OwlPlayerStatsRepository) : ViewModel() {
    val uiState = MutableStateFlow<PlayerGridState>(PlayerGridState.Loading)

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
                    uiState.value = PlayerGridState.Content(players)
                }
            }.getOrElse {
                uiState.value = PlayerGridState.Error(it.message)
            }
        }
    }
}
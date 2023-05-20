package com.keiferstone.owlplayerstats.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keiferstone.data.model.PlayerRoles
import com.keiferstone.data.model.Summary
import com.keiferstone.data.repository.OwlPlayerStatsRepository
import com.keiferstone.owlplayerstats.state.PlayerDatum
import com.keiferstone.owlplayerstats.state.PlayerFilter
import com.keiferstone.owlplayerstats.state.PlayerGridState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PlayerGridViewModel @Inject constructor(private val repository: OwlPlayerStatsRepository) : ViewModel() {
    val uiState = MutableStateFlow<PlayerGridState>(PlayerGridState.Loading)

    init {
        viewModelScope.launch {
            runCatching {
                repository.getSummary().let { summary ->
                    uiState.value = PlayerGridState.Content(
                        data = summary.players.map { playerSummary ->
                            PlayerDatum(playerSummary, summary.teams.find { it.id == playerSummary.currentTeam })
                        })
                }
            }.getOrElse {
                uiState.value = PlayerGridState.Error(it.message)
            }
        }
    }
}
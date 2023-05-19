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
                repository.getSummary()?.let { summary ->
                    uiState.value = PlayerGridState.Content(
                        filters = filters(summary),
                        data = summary.players.map { playerSummary ->
                            PlayerDatum(playerSummary, summary.teams.find { it.id == playerSummary.currentTeam })
                        })
                } ?: run {
                    uiState.value = PlayerGridState.Error()
                }
            }.getOrElse {
                uiState.value = PlayerGridState.Error(it.message)
            }
        }
    }

    private fun filters(summary: Summary) = buildList {
        if (summary.players.any { it.currentTeam != null }) {
            add(PlayerFilter.OnTeam)
        }
        if (summary.players.any { it.role == PlayerRoles.TANK } ) {
            add(PlayerFilter.PlaysTank)
        }
        if (summary.players.any { it.role == PlayerRoles.DPS } ) {
            add(PlayerFilter.PlaysDps)
        }
        if (summary.players.any { it.role == PlayerRoles.SUPPORT } ) {
            add(PlayerFilter.PlaysSupport)
        }
    }
}
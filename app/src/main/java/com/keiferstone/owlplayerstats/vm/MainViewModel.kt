package com.keiferstone.owlplayerstats.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keiferstone.data.model.PlayerSummary
import com.keiferstone.data.model.TeamSummary
import com.keiferstone.data.repository.OwlPlayerStatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(private val repository: OwlPlayerStatsRepository) : ViewModel() {
    val playerSummariesFlow = MutableStateFlow(listOf<PlayerSummary>())
    val teamSummariesFlow = MutableStateFlow(listOf<TeamSummary>())

    fun getSummary() {
        viewModelScope.launch {
            repository.getSummary()?.let {
                playerSummariesFlow.value = it.players
                teamSummariesFlow.value = it.teams
            }
        }
    }

    fun getTeamSummary(teamId: Long): TeamSummary? {
        return teamSummariesFlow.value.find { it.id == teamId }
    }
}
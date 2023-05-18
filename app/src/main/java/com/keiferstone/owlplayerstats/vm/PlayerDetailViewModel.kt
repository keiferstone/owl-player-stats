package com.keiferstone.owlplayerstats.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keiferstone.data.model.PlayerDetail
import com.keiferstone.data.model.PlayerSummary
import com.keiferstone.data.model.TeamSummary
import com.keiferstone.data.repository.OwlPlayerStatsRepository
import com.keiferstone.owlplayerstats.state.PlayerDetailState
import com.keiferstone.owlplayerstats.state.PlayerGridState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PlayerDetailViewModel @Inject constructor(private val repository: OwlPlayerStatsRepository) : ViewModel() {
    val uiState = MutableStateFlow<PlayerDetailState>(PlayerDetailState.Loading)

    fun loadPlayer(playerId: Long) {
        viewModelScope.launch {
            runCatching {
                repository.getPlayer(playerId)?.let {
                    uiState.value = PlayerDetailState.Content(it)
                } ?: run {
                    uiState.value = PlayerDetailState.Error()
                }
            }.getOrElse {
                uiState.value = PlayerDetailState.Error(it.message)
            }
        }
    }
}
package com.keiferstone.owlplayerstats.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keiferstone.data.model.PlayerDetail
import com.keiferstone.data.model.PlayerSummary
import com.keiferstone.data.model.TeamSummary
import com.keiferstone.data.repository.OwlPlayerStatsRepository
import com.keiferstone.owlplayerstats.state.PlayerComparisonState
import com.keiferstone.owlplayerstats.state.PlayerDetailState
import com.keiferstone.owlplayerstats.state.PlayerGridState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PlayerComparisonViewModel @Inject constructor(private val repository: OwlPlayerStatsRepository) : ViewModel() {
    val uiState = MutableStateFlow<PlayerComparisonState>(PlayerComparisonState.Loading)

    fun loadPlayers(player1Id: Long, player2Id: Long) {
        viewModelScope.launch {
            runCatching {
                val player1Async = async { repository.getPlayer(player1Id) }
                val player2Async = async { repository.getPlayer(player2Id) }

                val player1 = player1Async.await()
                val player2 = player2Async.await()

                uiState.value = if (player1 != null && player2 != null) {
                    PlayerComparisonState.Content(player1, player2)
                } else PlayerComparisonState.Error()
            }.getOrElse {
                uiState.value = PlayerComparisonState.Error(it.message)
            }
        }
    }
}
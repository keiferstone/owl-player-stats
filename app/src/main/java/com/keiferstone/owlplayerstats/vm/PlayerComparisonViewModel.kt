package com.keiferstone.owlplayerstats.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keiferstone.data.repository.OwlPlayerStatsRepository
import com.keiferstone.owlplayerstats.state.PlayerComparisonState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class PlayerComparisonViewModel @Inject constructor(private val repository: OwlPlayerStatsRepository) : ViewModel() {
    private val playerId1 = MutableStateFlow(-1L)
    private val playerId2 = MutableStateFlow(-1L)

    @OptIn(FlowPreview::class)
    val uiState = combine(playerId1, playerId2) { playerId1, playerId2 -> Pair(playerId1, playerId2) }
        .filter { it.first != -1L && it.second != -1L }
        .flatMapMerge {
            combine(
                repository.playerDetailResource(it.first).flow,
                repository.playerDetailResource(it.second).flow
            ) { player1, player2 ->
                PlayerComparisonState.Content(player1, player2) as PlayerComparisonState
            }
        }
        .catch { emit(PlayerComparisonState.Error(it.message)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = PlayerComparisonState.Loading)

    fun setPlayerIds(id1: Long, id2: Long) {
        playerId1.value = id1
        playerId2.value = id2
    }
}
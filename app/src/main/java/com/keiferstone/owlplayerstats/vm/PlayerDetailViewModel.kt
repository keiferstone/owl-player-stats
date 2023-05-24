package com.keiferstone.owlplayerstats.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keiferstone.data.model.PlayerDetail
import com.keiferstone.data.repository.OwlPlayerStatsRepository
import com.keiferstone.owlplayerstats.state.PlayerDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class PlayerDetailViewModel @Inject constructor(private val repository: OwlPlayerStatsRepository) : ViewModel() {
    private val playerId = MutableStateFlow(-1L)

    @OptIn(FlowPreview::class)
    val uiState = playerId
        .filter { it != -1L }
        .flatMapMerge { repository.playerDetailResource(it).flow }
        .map<PlayerDetail, PlayerDetailState> { PlayerDetailState.Content(it) }
        .catch { emit(PlayerDetailState.Error(it.message)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = PlayerDetailState.Loading)

    fun setPlayerId(id: Long) {
        playerId.value = id
    }
}
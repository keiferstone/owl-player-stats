package com.keiferstone.owlplayerstats.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keiferstone.data.model.Player
import com.keiferstone.data.repository.OwlPlayerStatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(private val repository: OwlPlayerStatsRepository) : ViewModel() {
    val playersFlow = MutableStateFlow(listOf<Player>())

    fun getSummary() {
        viewModelScope.launch {
            repository.getSummary()?.let {
                playersFlow.value = it.players
            }
        }
    }
}
package com.keiferstone.owlplayerstats.ui.screen


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.keiferstone.owlplayerstats.model.PlayerFilter
import com.keiferstone.owlplayerstats.state.StatListState
import com.keiferstone.owlplayerstats.ui.component.FilterLabel
import com.keiferstone.owlplayerstats.ui.component.StatLeadersRow
import com.keiferstone.owlplayerstats.vm.StatListViewModel

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun StatListScreen(
    viewModel: StatListViewModel = hiltViewModel()) {

    val playerFilters = remember { listOf(PlayerFilter.PlaysTank, PlayerFilter.PlaysDps, PlayerFilter.PlaysSupport, PlayerFilter.PlayedFiveMinutes) }
    val selectedPlayerFilters = remember { mutableStateListOf<PlayerFilter>() }

    val scrollState = rememberScrollState()

    when (val uiState = viewModel.uiState.collectAsState().value) {
        StatListState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator(
                   modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        is StatListState.Content -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                FlowRow(
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 16.dp, bottom = 0.dp)
                ) {
                    playerFilters.forEach { filter ->
                        FilterChip(
                            selected = selectedPlayerFilters.contains(filter),
                            onClick = {
                                if (selectedPlayerFilters.contains(filter)) selectedPlayerFilters.remove(filter)
                                else selectedPlayerFilters.add(filter)
                                viewModel.filterPlayers(selectedPlayerFilters.toList())
                            },
                            label = { FilterLabel(filter = filter) },
                            modifier = Modifier.padding(start = 4.dp, end = 4.dp, top = 0.dp, bottom = 0.dp),
                        )
                    }
                }
                LazyColumn {
                    items(uiState.data) {
                        StatLeadersRow(
                            statType = it.statType,
                            leaders = it.leaders
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        is StatListState.Error -> {} // TODO
    }
}
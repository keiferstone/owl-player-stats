package com.keiferstone.owlplayerstats.ui.screen


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.keiferstone.data.model.PlayerDetail
import com.keiferstone.owlplayerstats.R
import com.keiferstone.owlplayerstats.extension.parseFilterArgs
import com.keiferstone.owlplayerstats.model.Filter
import com.keiferstone.owlplayerstats.ui.component.PlayerItem
import com.keiferstone.owlplayerstats.state.PlayerGridState
import com.keiferstone.owlplayerstats.ui.navigation.NavArgs
import com.keiferstone.owlplayerstats.vm.PlayerGridViewModel

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PlayerGridScreen(
    navController: NavController,
    viewModel: PlayerGridViewModel = hiltViewModel(),
    onMoreFiltersSelected: (List<Filter>) -> Unit = {},
    onPlayerSelected: (PlayerDetail) -> Unit = {},
    onPlayerPairSelected: (PlayerDetail, PlayerDetail) -> Unit = { _, _ -> }) {
    val initialFilters = navController.currentBackStackEntryAsState().value?.arguments
        ?.getString(NavArgs.FILTERS)
        .parseFilterArgs()

    val defaultVisibleFilters = remember {
        listOf(
            Filter.OnTeam,
            Filter.PlaysTank,
            Filter.PlaysDps,
            Filter.PlaysSupport,
            Filter.HasStats
        )
    }
    val selectedFilters = remember { mutableStateListOf(*initialFilters.toTypedArray()) }
    val visibleFilters = (defaultVisibleFilters + selectedFilters)
        .toSet()
        .let {
            if (it.size > defaultVisibleFilters.size) {
                it.filter { filter ->
                    selectedFilters.contains(filter)
                }
            } else it
        }
    var searchQuery by remember { mutableStateOf("") }
    var selectedPlayer by remember { mutableStateOf<PlayerDetail?>(null) }

    when (val uiState = viewModel.uiState.collectAsState().value) {
        is PlayerGridState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        is PlayerGridState.Content -> {
            Scaffold(
                floatingActionButton = {
                    FloatingActionButton(onClick = {
                        onMoreFiltersSelected(selectedFilters)
                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_rounded_outlined_tune_24),
                            contentDescription = "More filters"
                        )
                    }
                }
            ) { padding ->
                Column(modifier = Modifier.padding(padding)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 4.dp),
                            value = searchQuery,
                            onValueChange = {
                                searchQuery = it
                            },
                            label = {
                                Text(stringResource(R.string.search))
                            })
                    }
                    FlowRow(
                        modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 0.dp, bottom = 0.dp)
                    ) {
                        visibleFilters.forEach { filter ->
                            FilterChip(
                                selected = selectedFilters.contains(filter),
                                onClick = {
                                    if (selectedFilters.contains(filter)) selectedFilters.remove(filter)
                                    else selectedFilters.add(filter)
                                    viewModel.filterData(selectedFilters.toList())
                                },
                                label = { Text(filter.toString(LocalContext.current.resources)) },
                                modifier = Modifier
                                    .height(40.dp)
                                    .padding(start = 4.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
                            )
                        }
                    }
                    selectedPlayer?.let { selectedPlayer ->
                        Text(
                            text = "${selectedPlayer.name} vs ???",
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 4.dp)
                                .align(Alignment.CenterHorizontally),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(120.dp),
                        contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 4.dp)
                    ) {
                        items(uiState.players
                            .filter { player ->
                                player.name.contains(searchQuery, ignoreCase = true)
                            }
                            .sortedBy { it.name }) { player ->
                            Box {
                                PlayerItem(
                                    player = player,
                                    selected = player.id == selectedPlayer?.id,
                                    onPlayerClick = { player ->
                                        selectedPlayer?.let {
                                            if (player.id != it.id) {
                                                onPlayerPairSelected(it, player)
                                            }
                                            selectedPlayer = null
                                        } ?: onPlayerSelected(player)
                                    },
                                    onPlayerLongClick = { player ->
                                        selectedPlayer = player
                                    })
                            }
                        }
                    }
                }
            }
        }
        is PlayerGridState.Error -> Unit // TODO
    }
}
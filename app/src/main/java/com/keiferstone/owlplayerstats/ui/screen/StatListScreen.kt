package com.keiferstone.owlplayerstats.ui.screen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.keiferstone.data.model.StatType
import com.keiferstone.owlplayerstats.R
import com.keiferstone.owlplayerstats.extension.parseFilterArgs
import com.keiferstone.owlplayerstats.model.Filter
import com.keiferstone.owlplayerstats.state.StatListState
import com.keiferstone.owlplayerstats.ui.component.StatLeadersRow
import com.keiferstone.owlplayerstats.ui.navigation.NavArgs
import com.keiferstone.owlplayerstats.vm.StatListViewModel
import java.util.Locale.filter

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun StatListScreen(
    navController: NavController,
    viewModel: StatListViewModel = hiltViewModel(),
    onMoreFiltersSelected: (List<Filter>) -> Unit = {},
    onStatSelected: (StatType) -> Unit = {}) {
    val initialFilters = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.get<String>(NavArgs.FILTERS)
        .parseFilterArgs()
    val defaultVisibleFilters = remember {
        listOf(
            Filter.PlaysTank,
            Filter.PlaysDps,
            Filter.PlaysSupport,
            Filter.TimePlayed(5 * 60)
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        FlowRow(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 12.dp, end = 0.dp, top = 16.dp, bottom = 0.dp)
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
                    }
                    LazyColumn {
                        items(uiState.data) {
                            StatLeadersRow(
                                statType = it.statType,
                                leaders = it.leaders
                            ) { statType ->
                                onStatSelected(statType)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
        is StatListState.Error -> {} // TODO
    }
}
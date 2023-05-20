package com.keiferstone.owlplayerstats.ui.screen


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.keiferstone.data.model.StatType
import com.keiferstone.owlplayerstats.R
import com.keiferstone.owlplayerstats.extension.extractValue
import com.keiferstone.owlplayerstats.extension.formatStatValue
import com.keiferstone.owlplayerstats.extension.nameResId
import com.keiferstone.owlplayerstats.model.Filter
import com.keiferstone.owlplayerstats.state.StatDetailState
import com.keiferstone.owlplayerstats.ui.component.FilterLabel
import com.keiferstone.owlplayerstats.vm.StatDetailViewModel

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun StatDetailScreen(
    statType: StatType,
    viewModel: StatDetailViewModel = hiltViewModel()) {

    val filters = remember {
        listOf(
            Filter.PlaysTank,
            Filter.PlaysDps,
            Filter.PlaysSupport,
            Filter.HasStat(statType)
        )
    }
    val selectedFilters = remember { mutableStateListOf<Filter>(Filter.HasStat(statType)) }

    val scrollState = rememberScrollState()

    when (val uiState = viewModel.uiState.collectAsState().value) {
        StatDetailState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator(
                   modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        is StatDetailState.Content -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                FlowRow(
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 16.dp, bottom = 0.dp)
                ) {
                    filters.forEach { filter ->
                        FilterChip(
                            selected = selectedFilters.contains(filter),
                            onClick = {
                                if (selectedFilters.contains(filter)) selectedFilters.remove(filter)
                                else selectedFilters.add(filter)
                                viewModel.filterPlayers(selectedFilters.toList())
                            },
                            label = { FilterLabel(filter = filter) },
                            modifier = Modifier.padding(start = 4.dp, end = 4.dp, top = 0.dp, bottom = 0.dp),
                        )
                    }
                }
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                    text = stringResource(statType.nameResId()),
                    style = MaterialTheme.typography.displayLarge,
                )
                Row {
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(
                        modifier = Modifier
                            .weight(2f)
                            .padding(horizontal = 2.dp),
                        text = stringResource(R.string.player),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 2.dp),
                        text = stringResource(R.string.per_ten),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 2.dp),
                        text = stringResource(R.string.total),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                }
                uiState.players
                    .sortedByDescending {
                        it.stats.extractValue(statType)
                    }
                    .forEach { player ->
                        Row {
                            Spacer(modifier = Modifier.width(14.dp))
                            Text(
                                modifier = Modifier
                                    .weight(2f)
                                    .padding(horizontal = 2.dp),
                                text = player.name,
                                fontSize = 13.sp
                            )
                            Text(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 2.dp),
                                text = player.stats
                                    .extractValue(statType)
                                    .formatStatValue(),
                                fontSize = 13.sp
                            )
                            Text(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 2.dp),
                                text = player.stats
                                    .extractValue(statType, false)
                                    .formatStatValue(),
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.width(14.dp))
                        }
                    }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        is StatDetailState.Error -> {} // TODO
    }
}
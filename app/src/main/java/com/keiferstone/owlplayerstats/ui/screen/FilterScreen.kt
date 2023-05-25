package com.keiferstone.owlplayerstats.ui.screen


import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.keiferstone.data.model.PlayerRoles
import com.keiferstone.data.model.StatType
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.keiferstone.owlplayerstats.R
import com.keiferstone.owlplayerstats.extension.select
import com.keiferstone.owlplayerstats.model.Filter
import com.keiferstone.owlplayerstats.ui.composable.hiltActivityViewModel
import com.keiferstone.owlplayerstats.vm.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    navController: NavController,
    initialFilters: List<Filter>,
    viewModel: MainViewModel = hiltActivityViewModel()
) {
    val scrollState = rememberScrollState()
    val selectedFilters = remember { mutableStateListOf(*initialFilters.toTypedArray()) }
    val selectedTimePlayedValue = remember { mutableStateOf((5 * 60).toFloat()) }
    val timePlayedFilter = Filter.TimePlayed(selectedTimePlayedValue.value.toLong())

    BackHandler {
        viewModel.filterData(selectedFilters)
        navController.popBackStack()
    }

    Column {
        TopAppBar(
            title = { Text(stringResource(R.string.filters)) },
            actions = {
                IconButton(
                    modifier = Modifier.padding(16.dp),
                    onClick = {
                        viewModel.filterData(selectedFilters)
                        navController.popBackStack()
                    }) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "Save filters"
                    )
                }
            }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Text(
                text = stringResource(R.string.player_status),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 4.dp)
            )
            CheckboxFilter(
                filter = Filter.OnTeam,
                selectedFilters = selectedFilters
            )
            Text(
                text = stringResource(R.string.player_role),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 4.dp)
            )
            RadioFilter(
                filter = Filter.PlaysRole(PlayerRoles.TANK),
                selectedFilters = selectedFilters
            )
            RadioFilter(
                filter = Filter.PlaysRole(PlayerRoles.DPS),
                selectedFilters = selectedFilters
            )
            RadioFilter(
                filter = Filter.PlaysRole(PlayerRoles.SUPPORT),
                selectedFilters = selectedFilters
            )
            Text(
                text = stringResource(R.string.player_stats),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 4.dp)
            )
            CheckboxFilter(
                filter = Filter.HasStats,
                selectedFilters = selectedFilters
            )
            Column(
                modifier = Modifier.padding(start = 16.dp)
            ) {
                StatType.allStatTypes().forEach {
                    CheckboxFilter(
                        filter = Filter.HasStat(it),
                        selectedFilters = selectedFilters
                    )
                }
            }
            Text(
                text = timePlayedFilter.toString(LocalContext.current.resources),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 4.dp)
            )
            Slider(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                value = selectedTimePlayedValue.value,
                onValueChange = { selectedTimePlayedValue.value = it },
                valueRange = 0f.rangeTo((20 * 60 * 60).toFloat()), // 0 to 20 hours
                onValueChangeFinished = { selectedFilters.select(timePlayedFilter) }
            )
        }
    }
}

@Composable
fun CheckboxFilter(
    filter: Filter,
    selectedFilters: MutableList<Filter>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .toggleable(
                value = selectedFilters.contains(filter),
                onValueChange = {
                    selectedFilters.select(filter)
                },
                role = Role.Checkbox
            )
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = selectedFilters.contains(filter),
            onCheckedChange = null
        )
        Text(
            text = filter.toString(LocalContext.current.resources),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
fun RadioFilter(
    filter: Filter,
    selectedFilters: MutableList<Filter>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .toggleable(
                value = selectedFilters.contains(filter),
                onValueChange = {
                    selectedFilters.select(filter)
                },
                role = Role.RadioButton
            )
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selectedFilters.contains(filter),
            onClick = null
        )
        Text(
            text = filter.toString(LocalContext.current.resources),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}
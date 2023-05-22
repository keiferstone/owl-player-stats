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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.keiferstone.data.model.StatType
import com.keiferstone.owlplayerstats.R
import com.keiferstone.owlplayerstats.extension.nameResId
import com.keiferstone.owlplayerstats.model.Filter
import com.keiferstone.owlplayerstats.ui.navigation.NavArgs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(navController: NavController, initialFilters: List<Filter>) {
    val scrollState = rememberScrollState()
    val filters = remember {
        buildList {
            add(Filter.OnTeam)
            add(Filter.PlaysTank)
            add(Filter.PlaysDps)
            add(Filter.PlaysSupport)
            add(Filter.HasStats)
            addAll(StatType.allStatTypes().map { Filter.HasStat(it) })
            add(Filter.TimePlayed(5 * 60))
        }
    }
    val selectedFilters = remember { mutableStateListOf(*initialFilters.toTypedArray()) }

    fun saveAndPop() {
        navController
            .previousBackStackEntry
            ?.savedStateHandle
            ?.set(NavArgs.FILTERS, selectedFilters.toList().joinToString { it.id })
        navController.popBackStack()
    }

    BackHandler { saveAndPop() }
    Column {
        TopAppBar(
            title = {
                Text(stringResource(R.string.filters))
            },
            actions = {
                IconButton(
                    modifier = Modifier.padding(16.dp),
                    onClick = { saveAndPop() }) {
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
            Spacer(modifier = Modifier.height(12.dp))
            filters.forEach { filter ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .toggleable(
                            value = selectedFilters.contains(filter),
                            onValueChange = {
                                if (selectedFilters.contains(filter)) selectedFilters.remove(filter)
                                else selectedFilters.add(filter)
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
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
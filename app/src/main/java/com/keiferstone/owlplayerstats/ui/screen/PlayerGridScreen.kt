package com.keiferstone.owlplayerstats.ui.screen


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.keiferstone.owlplayerstats.R
import com.keiferstone.owlplayerstats.composable.hiltActivityViewModel
import com.keiferstone.owlplayerstats.ui.component.PlayerItem
import com.keiferstone.owlplayerstats.ui.theme.OwlPlayerStatsTheme
import com.keiferstone.owlplayerstats.vm.MainViewModel

@Composable
fun PlayerGridScreen(viewModel: MainViewModel = hiltActivityViewModel()) {
    val players = viewModel.playerSummariesFlow.collectAsState().value

    var searchQuery by remember { mutableStateOf("") }

    Column {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = searchQuery,
            onValueChange = {
                searchQuery = it
            },
            label = {
                Text(stringResource(R.string.search))
            })
        LazyVerticalGrid(
            columns = GridCells.Adaptive(120.dp),
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 4.dp)
        ) {
            items(players
                .filter {
                    it.name.contains(searchQuery, ignoreCase = true)
                            || it.givenName.contains(searchQuery, ignoreCase = true)
                            || it.familyName.contains(searchQuery, ignoreCase = true)
                }
                .sortedBy { it.name }) { player ->
                PlayerItem(
                    player = player,
                    team = player.currentTeam?.let { viewModel.getTeamSummary(it) }) {
                    // TODO: Navigate to player details
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlayerGridScreenPreview() {
    OwlPlayerStatsTheme {
        PlayerGridScreen()
    }
}
package com.keiferstone.owlplayerstats.ui.screen


import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.keiferstone.owlplayerstats.composable.hiltActivityViewModel
import com.keiferstone.owlplayerstats.ui.component.PlayerItem
import com.keiferstone.owlplayerstats.ui.theme.OwlPlayerStatsTheme
import com.keiferstone.owlplayerstats.vm.MainViewModel

@Composable
fun PlayerGridScreen(viewModel: MainViewModel = hiltActivityViewModel()) {
    val players = viewModel.playerSummariesFlow.collectAsState().value

    LazyVerticalGrid(
        columns = GridCells.Adaptive(120.dp)
    ) {
        items(players) { player ->
            PlayerItem(
                player = player,
                team = player.currentTeam?.let { viewModel.getTeamSummary(it) } )
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
package com.keiferstone.owlplayerstats.screen


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.keiferstone.data.model.PlayerSummary
import com.keiferstone.owlplayerstats.R
import com.keiferstone.owlplayerstats.component.PlayerItem
import com.keiferstone.owlplayerstats.state.PlayerGridState
import com.keiferstone.owlplayerstats.vm.PlayerGridViewModel

@Composable
fun PlayerGridScreen(
    viewModel: PlayerGridViewModel = hiltViewModel(),
    onPlayerSelected: (PlayerSummary) -> Unit) {

    var searchQuery by remember { mutableStateOf("") }

    when (val uiState = viewModel.uiState.collectAsState().value) {
        is PlayerGridState.Loading -> Unit // TODO
        is PlayerGridState.Content -> {
            Column {
                OutlinedTextField(
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
                    items(uiState
                        .playerData
                        .filter {
                            it.player.name.contains(searchQuery, ignoreCase = true)
                                    || it.player.givenName.contains(searchQuery, ignoreCase = true)
                                    || it.player.familyName.contains(searchQuery, ignoreCase = true)
                        }
                        .sortedBy { it.player.name }) { playerDatum ->
                        PlayerItem(
                            player = playerDatum.player,
                            team = playerDatum.team) {
                            onPlayerSelected(it)
                        }
                    }
                }
            }
        }
        is PlayerGridState.Error -> Unit // TODO
    }
}

@Preview(showBackground = true)
@Composable
fun PlayerGridScreenPreview() {
    MaterialTheme {
        PlayerGridScreen {}
    }
}
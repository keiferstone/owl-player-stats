package com.keiferstone.owlplayerstats.screen


import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import com.keiferstone.owlplayerstats.composable.hiltActivityViewModel
import com.keiferstone.owlplayerstats.theme.OwlPlayerStatsTheme
import com.keiferstone.owlplayerstats.vm.PlayerGridViewModel

@Composable
fun PlayerDetailScreen(viewModel: PlayerGridViewModel = hiltActivityViewModel()) {
    val uiState = viewModel.uiState.collectAsState().value

    Column {

    }
}

@Preview(showBackground = true)
@Composable
fun PlayerDetailScreenPreview() {
    OwlPlayerStatsTheme {
        PlayerDetailScreen()
    }
}
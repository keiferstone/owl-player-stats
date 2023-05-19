package com.keiferstone.owlplayerstats.ui.screen


import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.keiferstone.owlplayerstats.state.StatListState
import com.keiferstone.owlplayerstats.ui.component.StatLeadersRow
import com.keiferstone.owlplayerstats.vm.StatListViewModel

@Composable
fun StatListScreen(
    viewModel: StatListViewModel = hiltViewModel()) {

    when (val uiState = viewModel.uiState.collectAsState().value) {
        StatListState.Loading -> {} // TODO
        is StatListState.Content -> {
            Column {
                uiState.data.forEach {
                    StatLeadersRow(
                        name = stringResource(it.nameResId),
                        leaders = it.leaders)
                }
            }
        }
        is StatListState.Error -> {} // TODO
    }
}
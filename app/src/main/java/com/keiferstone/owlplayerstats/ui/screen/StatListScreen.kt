package com.keiferstone.owlplayerstats.ui.screen


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.keiferstone.owlplayerstats.state.StatListState
import com.keiferstone.owlplayerstats.ui.component.StatLeadersRow
import com.keiferstone.owlplayerstats.vm.StatListViewModel

@Composable
fun StatListScreen(
    viewModel: StatListViewModel = hiltViewModel()) {

    val scrollState = rememberScrollState()

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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                uiState.data.forEach {
                    StatLeadersRow(
                        statType = it.statType,
                        leaders = it.leaders)
                }
            }
        }
        is StatListState.Error -> {} // TODO
    }
}
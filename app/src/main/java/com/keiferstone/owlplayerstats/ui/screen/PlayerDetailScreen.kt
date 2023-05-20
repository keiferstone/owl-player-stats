package com.keiferstone.owlplayerstats.ui.screen


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.keiferstone.data.model.StatType
import com.keiferstone.owlplayerstats.R
import com.keiferstone.owlplayerstats.extension.extractValue
import com.keiferstone.owlplayerstats.extension.nameResId
import com.keiferstone.owlplayerstats.ui.component.PlayerStatRow
import com.keiferstone.owlplayerstats.extension.parseHexColor
import com.keiferstone.owlplayerstats.state.PlayerDetailState
import com.keiferstone.owlplayerstats.vm.PlayerDetailViewModel

@Composable
fun PlayerDetailScreen(
    playerId: Long,
    viewModel: PlayerDetailViewModel = hiltViewModel()) {
    viewModel.loadPlayer(playerId)

    when (val uiState = viewModel.uiState.collectAsState().value) {
        is PlayerDetailState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        is PlayerDetailState.Content -> {
            uiState.player.let { player ->
                val primaryColor = player.currentTeam?.primaryColor?.parseHexColor()?.let { Color(it) } ?: Color.Black
                val secondaryColor = player.currentTeam?.secondaryColor?.parseHexColor()?.let { Color(it) } ?: Color.White

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = secondaryColor)
                        .verticalScroll(rememberScrollState())
                ) {
                    Box(
                        modifier = Modifier
                            .background(color = Color.LightGray)
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .height(260.dp)
                                .fillMaxWidth(),
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(player.headshotUrl)
                                .build(),
                            contentDescription = "${player.name} headshot",
                            contentScale = ContentScale.FillHeight,
                        )
                    }
                    Text(
                        modifier = Modifier
                            .padding(16.dp),
                        text = player.name,
                        color = primaryColor,
                        style = MaterialTheme.typography.displayLarge
                    )
                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                        border = BorderStroke(
                            width = 1.dp,
                            color = primaryColor),
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                            text = stringResource(R.string.stats_per_ten),
                            style = MaterialTheme.typography.headlineMedium
                        )
                        StatType.allStatTypes().forEach {
                            PlayerStatRow(
                                name = stringResource(it.nameResId()),
                                value = player.stats.extractValue(it))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
        is PlayerDetailState.Error -> Unit // TODO
    }
}

@Preview(showBackground = true)
@Composable
fun PlayerDetailScreenPreview() {
    MaterialTheme {
        PlayerDetailScreen(0)
    }
}
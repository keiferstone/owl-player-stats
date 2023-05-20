package com.keiferstone.owlplayerstats.ui.screen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.graphics.Brush
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
import com.keiferstone.owlplayerstats.ui.component.PlayerStatComparisonRow
import com.keiferstone.owlplayerstats.extension.parseHexColor
import com.keiferstone.owlplayerstats.state.PlayerComparisonState
import com.keiferstone.owlplayerstats.vm.PlayerComparisonViewModel

@Composable
fun PlayerComparisonScreen(
    player1Id: Long,
    player2Id: Long,
    viewModel: PlayerComparisonViewModel = hiltViewModel()) {
    viewModel.loadPlayers(player1Id, player2Id)

    when (val uiState = viewModel.uiState.collectAsState().value) {
        is PlayerComparisonState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        is PlayerComparisonState.Content -> {
            val player1 = uiState.player1
            val player2 = uiState.player2
            val player1PrimaryColor = player1.currentTeam?.primaryColor?.parseHexColor()?.let { Color(it) } ?: Color.Black
            val player1SecondaryColor = player1.currentTeam?.secondaryColor?.parseHexColor()?.let { Color(it) } ?: Color.White
            val player2PrimaryColor = player2.currentTeam?.primaryColor?.parseHexColor()?.let { Color(it) } ?: Color.Black
            val player2SecondaryColor = player2.currentTeam?.secondaryColor?.parseHexColor()?.let { Color(it) } ?: Color.White

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            0f to player1SecondaryColor,
                            0.45f to player1SecondaryColor,
                            0.55f to player2SecondaryColor,
                            1f to player2SecondaryColor
                        )
                    )
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier
                        .background(color = Color.LightGray)
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .height(260.dp)
                            .weight(1f),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(player1.headshotUrl)
                            .build(),
                        contentDescription = "${player1.name} headshot",
                        contentScale = ContentScale.FillHeight,
                    )
                    AsyncImage(
                        modifier = Modifier
                            .height(260.dp)
                            .weight(1f),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(player2.headshotUrl)
                            .build(),
                        contentDescription = "${player2.name} headshot",
                        contentScale = ContentScale.FillHeight,
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        modifier = Modifier
                            .padding(16.dp),
                        text = player1.name,
                        color = player1PrimaryColor,
                        style = MaterialTheme.typography.displayMedium
                    )
                    Text(
                        modifier = Modifier
                            .padding(16.dp),
                        text = player2.name,
                        color = player2PrimaryColor,
                        style = MaterialTheme.typography.displayMedium
                    )
                }
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                        text = stringResource(R.string.stats_per_ten),
                        style = MaterialTheme.typography.headlineMedium
                    )
                    StatType.allStatTypes().forEach {
                        PlayerStatComparisonRow(
                            name = stringResource(it.nameResId()),
                            value1 = player1.stats.extractValue(it),
                            value2 = player2.stats.extractValue(it))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
        is PlayerComparisonState.Error -> Unit // TODO
    }
}

@Preview(showBackground = true)
@Composable
fun PlayerComparisonScreenPreview() {
    MaterialTheme {
        PlayerDetailScreen(0)
    }
}
package com.keiferstone.owlplayerstats.screen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.keiferstone.owlplayerstats.R
import com.keiferstone.owlplayerstats.extension.parseHexColor
import com.keiferstone.owlplayerstats.state.PlayerDetailState
import com.keiferstone.owlplayerstats.vm.PlayerDetailViewModel

@Composable
fun PlayerDetailScreen(
    playerId: Long,
    viewModel: PlayerDetailViewModel = hiltViewModel()) {
    viewModel.loadPlayer(playerId)
    val bigNoodleFontFamily = remember {
        FontFamily(Font(R.font.big_noodle_titling_oblique, FontWeight.Normal))
    }

    when (val uiState = viewModel.uiState.collectAsState().value) {
        is PlayerDetailState.Loading -> Unit // TODO
        is PlayerDetailState.Content -> {
            uiState.playerDetail.let { player ->
                val primaryColor = player.currentTeam?.primaryColor?.parseHexColor()?.let { Color(it) } ?: Color.Black
                val secondaryColor = player.currentTeam?.secondaryColor?.parseHexColor()?.let { Color(it) } ?: Color.White

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = secondaryColor)
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
                            .padding(
                                start = 8.dp,
                                end = 8.dp,
                                top = 2.dp,
                                bottom = 8.dp
                            ),
                        text = player.name,
                        color = primaryColor,
                        fontSize = 50.sp,
                        fontFamily = bigNoodleFontFamily
                    )
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
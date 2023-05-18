package com.keiferstone.owlplayerstats.screen


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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
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
import com.keiferstone.owlplayerstats.component.StatRow
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
                        fontSize = 56.sp,
                        fontFamily = bigNoodleFontFamily
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
                            text = "All-time stats",
                            fontSize = 28.sp,
                            fontFamily = bigNoodleFontFamily,
                        )
                        StatRow(
                            name = "Damage done",
                            value = player.stats.heroDamageDone)
                        StatRow(
                            name = "Healing done",
                            value = player.stats.healingDone)
                        StatRow(
                            name = "Damage taken",
                            value = player.stats.damageTaken)
                        StatRow(
                            name = "Final blows",
                            value = player.stats.finalBlows)
                        StatRow(
                            name = "Eliminations",
                            value = player.stats.eliminations)
                        StatRow(
                            name = "Deaths",
                            value = player.stats.deaths)
                        StatRow(
                            name = "Time spent on fire",
                            value = player.stats.timeSpentOnFire)
                        StatRow(
                            name = "Solo kills",
                            value = player.stats.soloKills)
                        StatRow(
                            name = "Ultimates used",
                            value = player.stats.ultsUsed)
                        StatRow(
                            name = "Ultimates earned",
                            value = player.stats.ultsEarned)
                        StatRow(
                            name = "Time played",
                            value = player.stats.timePlayed)
                        StatRow(
                            name = "Dragonstrike kills",
                            value = player.stats.dragonstrikeKills)
                        StatRow(
                            name = "Players teleported",
                            value = player.stats.playersTeleported)
                        StatRow(
                            name = "Critical hits",
                            value = player.stats.criticalHits)
                        StatRow(
                            name = "Shots hit",
                            value = player.stats.shotsHit)
                        StatRow(
                            name = "Enemies hacked",
                            value = player.stats.heroDamageDone)
                        StatRow(
                            name = "Enemies EMPd",
                            value = player.stats.enemiesEMPd)
                        StatRow(
                            name = "Storm arrow kills",
                            value = player.stats.stormArrowKills)
                        StatRow(
                            name = "Scoped hits",
                            value = player.stats.scopedHits)
                        StatRow(
                            name = "Bob kills",
                            value = player.stats.scopedCriticalHits)
                        StatRow(
                            name = "Scoped critical hits",
                            value = player.stats.bobKills)
                        StatRow(
                            name = "Scoped critical hit kills",
                            value = player.stats.scopedCriticalHitKills)
                        StatRow(
                            name = "Charged shot kills",
                            value = player.stats.chargedShotKills)
                        StatRow(
                            name = "Knockback kills",
                            value = player.stats.knockbackKills)
                        StatRow(
                            name = "Deadeye kills",
                            value = player.stats.deadeyeKills)
                        StatRow(
                            name = "Overclock kills",
                            value = player.stats.overclockKills)
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
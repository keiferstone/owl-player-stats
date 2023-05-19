package com.keiferstone.owlplayerstats.ui.screen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.graphics.Brush
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
import com.keiferstone.owlplayerstats.ui.component.StatComparisonRow
import com.keiferstone.owlplayerstats.extension.parseHexColor
import com.keiferstone.owlplayerstats.state.PlayerComparisonState
import com.keiferstone.owlplayerstats.vm.PlayerComparisonViewModel

@Composable
fun PlayerComparisonScreen(
    player1Id: Long,
    player2Id: Long,
    viewModel: PlayerComparisonViewModel = hiltViewModel()) {
    viewModel.loadPlayers(player1Id, player2Id)
    val bigNoodleFontFamily = remember {
        FontFamily(Font(R.font.big_noodle_titling_oblique, FontWeight.Normal))
    }

    when (val uiState = viewModel.uiState.collectAsState().value) {
        is PlayerComparisonState.Loading -> Unit // TODO
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
                    .background(brush = Brush.horizontalGradient(
                        0f to player1SecondaryColor,
                        0.45f to player1SecondaryColor,
                        0.55f to player2SecondaryColor,
                        1f to player2SecondaryColor
                    ))
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
                        fontSize = 48.sp,
                        fontFamily = bigNoodleFontFamily
                    )
                    Text(
                        modifier = Modifier
                            .padding(16.dp),
                        text = player2.name,
                        color = player2PrimaryColor,
                        fontSize = 48.sp,
                        fontFamily = bigNoodleFontFamily
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
                        text = "All-time stats",
                        fontSize = 28.sp,
                        fontFamily = bigNoodleFontFamily,
                    )
                    StatComparisonRow(
                        name = "Damage done",
                        value1 = player1.stats.heroDamageDone,
                        value2 = player2.stats.heroDamageDone
                    )
                    StatComparisonRow(
                        name = "Healing done",
                        value1 = player1.stats.healingDone,
                        value2 = player2.stats.healingDone
                    )
                    StatComparisonRow(
                        name = "Damage taken",
                        value1 = player1.stats.damageTaken,
                        value2 = player2.stats.damageTaken
                    )
                    StatComparisonRow(
                        name = "Final blows",
                        value1 = player1.stats.finalBlows,
                        value2 = player2.stats.finalBlows
                    )
                    StatComparisonRow(
                        name = "Eliminations",
                        value1 = player1.stats.eliminations,
                        value2 = player2.stats.eliminations
                    )
                    StatComparisonRow(
                        name = "Deaths",
                        value1 = player1.stats.deaths,
                        value2 = player2.stats.deaths
                    )
                    StatComparisonRow(
                        name = "Time spent on fire",
                        value1 = player1.stats.timeSpentOnFire,
                        value2 = player2.stats.timeSpentOnFire
                    )
                    StatComparisonRow(
                        name = "Solo kills",
                        value1 = player1.stats.soloKills,
                        value2 = player2.stats.soloKills
                    )
                    StatComparisonRow(
                        name = "Ultimates used",
                        value1 = player1.stats.ultsUsed,
                        value2 = player2.stats.ultsUsed
                    )
                    StatComparisonRow(
                        name = "Ultimates earned",
                        value1 = player1.stats.ultsEarned,
                        value2 = player2.stats.ultsEarned
                    )
                    StatComparisonRow(
                        name = "Time played",
                        value1 = player1.stats.timePlayed,
                        value2 = player2.stats.timePlayed
                    )
                    StatComparisonRow(
                        name = "Dragonstrike kills",
                        value1 = player1.stats.dragonstrikeKills,
                        value2 = player2.stats.dragonstrikeKills
                    )
                    StatComparisonRow(
                        name = "Players teleported",
                        value1 = player1.stats.playersTeleported,
                        value2 = player2.stats.playersTeleported
                    )
                    StatComparisonRow(
                        name = "Critical hits",
                        value1 = player1.stats.criticalHits,
                        value2 = player2.stats.criticalHits
                    )
                    StatComparisonRow(
                        name = "Shots hit",
                        value1 = player1.stats.shotsHit,
                        value2 = player2.stats.shotsHit
                    )
                    StatComparisonRow(
                        name = "Enemies hacked",
                        value1 = player1.stats.enemiesHacked,
                        value2 = player2.stats.enemiesHacked
                    )
                    StatComparisonRow(
                        name = "Enemies EMPd",
                        value1 = player1.stats.enemiesEMPd,
                        value2 = player2.stats.enemiesEMPd
                    )
                    StatComparisonRow(
                        name = "Storm arrow kills",
                        value1 = player1.stats.stormArrowKills,
                        value2 = player2.stats.stormArrowKills
                    )
                    StatComparisonRow(
                        name = "Scoped hits",
                        value1 = player1.stats.scopedHits,
                        value2 = player2.stats.scopedHits
                    )
                    StatComparisonRow(
                        name = "Bob kills",
                        value1 = player1.stats.bobKills,
                        value2 = player2.stats.bobKills
                    )
                    StatComparisonRow(
                        name = "Scoped critical hits",
                        value1 = player1.stats.scopedCriticalHits,
                        value2 = player2.stats.scopedCriticalHits
                    )
                    StatComparisonRow(
                        name = "Charged shot kills",
                        value1 = player1.stats.chargedShotKills,
                        value2 = player2.stats.chargedShotKills
                    )
                    StatComparisonRow(
                        name = "Knockback kills",
                        value1 = player1.stats.knockbackKills,
                        value2 = player2.stats.knockbackKills
                    )
                    StatComparisonRow(
                        name = "Deadeye kills",
                        value1 = player1.stats.deadeyeKills,
                        value2 = player2.stats.deadeyeKills
                    )
                    StatComparisonRow(
                        name = "Overclock kills",
                        value1 = player1.stats.overclockKills,
                        value2 = player2.stats.overclockKills
                    )
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
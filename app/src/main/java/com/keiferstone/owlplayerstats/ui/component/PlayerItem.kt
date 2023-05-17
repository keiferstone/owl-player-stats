package com.keiferstone.owlplayerstats.ui.component


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.FixedScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.keiferstone.owlplayerstats.R
import com.keiferstone.data.model.PlayerSummary
import com.keiferstone.data.model.TeamSummary
import com.keiferstone.owlplayerstats.extension.parseHexColor

@Composable
fun PlayerItem(player: PlayerSummary, team: TeamSummary?, onClick: (PlayerSummary) -> Unit) {
    val bigNoodleFontFamily = remember {
        FontFamily(Font(R.font.big_noodle_titling_oblique, FontWeight.Normal))
    }

    val primaryColor = team?.primaryColor?.parseHexColor()?.let { Color(it) } ?: Color.Black
    val secondaryColor = team?.secondaryColor?.parseHexColor()?.let { Color(it) } ?: Color.White

    Box(
        modifier = Modifier
            .padding(4.dp),
    ) {
        OutlinedCard(
            modifier = Modifier
                .clickable { onClick(player) },
            colors = CardDefaults.outlinedCardColors(containerColor = secondaryColor),
            border = BorderStroke(
                width = 1.dp,
                color = primaryColor),
        ) {
            Column(
                verticalArrangement = Arrangement.Bottom
            ) {
                Box(
                    modifier = Modifier
                        .background(color = Color.LightGray)
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .height(140.dp),
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
                            bottom = 8.dp),
                    text = player.name,
                    color = primaryColor,
                    fontSize = 30.sp,
                    fontFamily = bigNoodleFontFamily
                )
            }
        }
    }
}
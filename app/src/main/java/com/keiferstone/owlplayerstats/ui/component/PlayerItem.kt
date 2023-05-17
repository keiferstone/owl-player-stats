package com.keiferstone.owlplayerstats.ui.component

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.keiferstone.owlplayerstats.R
import com.keiferstone.data.model.Player

@Composable
fun PlayerItem(player: Player) {
    val bigNoodleFontFamily = remember {
        FontFamily(Font(R.font.big_noodle_titling_oblique, FontWeight.Normal))
    }

    Surface(
        modifier = Modifier
            .height(120.dp)
            .wrapContentWidth()) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(player.headshotUrl)
                .build(),
            contentDescription = "${player.name} headshot"
        )
    }
}
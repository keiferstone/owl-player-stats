package com.keiferstone.owlplayerstats.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import android.graphics.Color as SysColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.keiferstone.data.model.TeamSummary
import com.keiferstone.owlplayerstats.ui.shape.RightSlantRectangle
import com.keiferstone.owlplayerstats.R

@Composable
fun TeamItem(team: TeamSummary, onClick: () -> Unit = {}) {
    val bigNoodleFontFamily = remember {
        FontFamily(Font(R.font.big_noodle_titling_oblique, FontWeight.Normal))
    }
    val primaryColor = remember {
        Color(SysColor.parseColor("#${team.primaryColor}"))
    }
    val secondaryColor = remember {
        Color(SysColor.parseColor("#${team.secondaryColor}"))
    }

    Surface(
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        color = primaryColor) {
        Row(modifier = Modifier.padding(10.dp)) {
            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .graphicsLayer {
                        shape = RightSlantRectangle()
                    },
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(team.icon)
                        .decoderFactory(SvgDecoder.Factory())
                        .build(),
                    contentDescription = "${team.name} icon"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = team.name,
                color = secondaryColor,
                fontSize = 38.sp,
                fontFamily = bigNoodleFontFamily,
            )
        }
    }
}
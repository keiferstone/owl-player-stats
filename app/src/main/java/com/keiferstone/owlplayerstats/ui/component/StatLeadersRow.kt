package com.keiferstone.owlplayerstats.ui.component


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keiferstone.data.db.Player
import com.keiferstone.data.model.PlayerDetail
import com.keiferstone.data.model.StatType
import com.keiferstone.owlplayerstats.R
import com.keiferstone.owlplayerstats.extension.extractValue
import com.keiferstone.owlplayerstats.extension.formatStatValue
import com.keiferstone.owlplayerstats.extension.nameResId

@Composable
fun StatLeadersRow(statType: StatType, leaders: List<PlayerDetail>, onClick: (StatType) -> Unit) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .clickable {
                    onClick(statType)
                }
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                text = stringResource(statType.nameResId()),
                style = MaterialTheme.typography.headlineMedium,
            )
            Row {
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    modifier = Modifier
                        .weight(2f)
                        .padding(horizontal = 2.dp),
                    text = stringResource(R.string.player),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 2.dp),
                    text = stringResource(R.string.per_ten),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 2.dp),
                    text = stringResource(R.string.total),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(14.dp))
            }
            leaders.forEach { player ->
                Row {
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(
                        modifier = Modifier
                            .weight(2f)
                            .padding(horizontal = 2.dp),
                        text = player.name,
                        fontSize = 13.sp
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 2.dp),
                        text = player.stats
                            .extractValue(statType)
                            .formatStatValue(),
                        fontSize = 13.sp
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 2.dp),
                        text = player.stats
                            .extractValue(statType, false)
                            .formatStatValue(),
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
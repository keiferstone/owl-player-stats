package com.keiferstone.owlplayerstats.ui.component


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keiferstone.data.model.PlayerDetail

@Composable
fun StatLeadersRow(name: String, leaders: List<PlayerDetail>) {
    Row {
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                text = name,
                style = MaterialTheme.typography.headlineMedium,
            )
        }
        leaders.forEach { player ->
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                text = player.name,
                fontSize = 13.sp
            )
        }
    }
}
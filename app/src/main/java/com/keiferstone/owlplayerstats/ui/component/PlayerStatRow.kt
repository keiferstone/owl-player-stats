package com.keiferstone.owlplayerstats.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keiferstone.owlplayerstats.extension.formatStatValue

@Composable
fun PlayerStatRow(name: String, value: Long?) {
    Row {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 4.dp),
            text = name,
            fontSize = 13.sp
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 4.dp),
            text = value.formatStatValue(),
            fontSize = 14.sp
        )
    }
}
package com.keiferstone.owlplayerstats.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.keiferstone.owlplayerstats.R
import com.keiferstone.owlplayerstats.model.PlayerFilter

@Composable
fun FilterLabel(filter: PlayerFilter) {
    when (filter) {
        PlayerFilter.OnTeam -> Text(stringResource(R.string.on_team))
        PlayerFilter.PlaysTank -> Text(stringResource(R.string.tank))
        PlayerFilter.PlaysDps -> Text(stringResource(R.string.dps))
        PlayerFilter.PlaysSupport -> Text(stringResource(R.string.support))
        PlayerFilter.HasStats -> Text(stringResource(R.string.has_stats))
        PlayerFilter.PlayedFiveMinutes -> Text(stringResource(R.string.played_five_minutes))
    }
}
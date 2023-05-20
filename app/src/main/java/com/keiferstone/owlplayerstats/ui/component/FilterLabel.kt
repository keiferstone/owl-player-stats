package com.keiferstone.owlplayerstats.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.keiferstone.owlplayerstats.R
import com.keiferstone.owlplayerstats.model.Filter

@Composable
fun FilterLabel(filter: Filter) {
    when (filter) {
        Filter.OnTeam -> Text(stringResource(R.string.on_team))
        Filter.PlaysTank -> Text(stringResource(R.string.tank))
        Filter.PlaysDps -> Text(stringResource(R.string.dps))
        Filter.PlaysSupport -> Text(stringResource(R.string.support))
        Filter.HasStats -> Text(stringResource(R.string.has_stats))
        is Filter.HasStat -> Text(stringResource(R.string.has_stat))
        Filter.PlayedFiveMinutes -> Text(stringResource(R.string.played_five_minutes))
    }
}
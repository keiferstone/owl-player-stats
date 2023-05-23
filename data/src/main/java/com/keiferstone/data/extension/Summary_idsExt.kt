package com.keiferstone.data.extension

import com.keiferstone.data.db.Summary_ids
import com.keiferstone.data.model.SummaryIds
import com.keiferstone.data.model.Ttls


fun Summary_ids.isStale(ttl: Long = Ttls.ONE_DAY): Boolean {
    return System.currentTimeMillis().let { currentTime ->
        currentTime - (last_fetched_at ?: currentTime) > ttl
    }
}

fun Summary_ids?.toSummaryIds(): SummaryIds {
    return SummaryIds(
        playerIds = this?.player_ids ?: emptyList(),
        teamIds = this?.team_ids ?: emptyList()
    )
}
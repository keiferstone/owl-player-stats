package com.keiferstone.data.extension

import com.keiferstone.data.model.Summary
import com.keiferstone.data.model.SummaryIds


fun Summary.toSummaryIds(): SummaryIds {
    return SummaryIds(
        playerIds = players.map { it.id },
        teamIds = teams.map { it.id }
    )
}
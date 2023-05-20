package com.keiferstone.data.extension

import com.keiferstone.data.db.Team
import com.keiferstone.data.model.PlayerDetailTeam
import com.keiferstone.data.model.TeamSummary


fun Team.toTeamSummary(): TeamSummary? {
    return if (code != null && name != null && logo != null) {
        TeamSummary(
            id = id,
            code = code,
            name = name,
            logo = logo,
            icon = icon,
            primaryColor = primary_color,
            secondaryColor = secondary_color
        )
    } else null
}

fun Team.toPlayerDetailTeam(): PlayerDetailTeam {
    return PlayerDetailTeam(
        id = id,
        logo = logo,
        icon = icon,
        primaryColor = primary_color,
        secondaryColor = secondary_color
    )
}
package com.keiferstone.data.extension

import com.keiferstone.data.db.Team
import com.keiferstone.data.model.PlayerDetailTeam

fun Team.toPlayerDetailTeam(): PlayerDetailTeam {
    return PlayerDetailTeam(
        id = id,
        logo = logo,
        icon = icon,
        primaryColor = primary_color,
        secondaryColor = secondary_color
    )
}
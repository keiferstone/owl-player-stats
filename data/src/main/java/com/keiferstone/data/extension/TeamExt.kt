package com.keiferstone.data.extension

import android.util.Log
import com.keiferstone.data.db.Team
import com.keiferstone.data.model.PlayerDetailTeam
import com.keiferstone.data.model.TeamSummary


fun Team.toPlayerDetailTeam(): PlayerDetailTeam {
    return PlayerDetailTeam(
        id = id,
        logo = logo,
        icon = icon,
        primaryColor = primary_color,
        secondaryColor = secondary_color
    )
}
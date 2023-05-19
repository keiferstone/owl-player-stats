package com.keiferstone.data.extension

import com.keiferstone.data.db.Player
import com.keiferstone.data.db.Team
import com.keiferstone.data.model.PlayerDetail
import com.keiferstone.data.model.TeamSummary

fun PlayerDetail.toDbRow(lastFetchedAt: Long = System.currentTimeMillis()): Player {
    return Player(
        id, 
        name,
        number,
        role,
        preferredSlot,
        currentTeams,
        givenName,
        familyName,
        headshotUrl,
        stats.heroDamageDone,
        stats.healingDone,
        stats.damageTaken,
        stats.finalBlows,
        stats.eliminations,
        stats.deaths,
        stats.timeSpentOnFire,
        stats.soloKills,
        stats.ultsUsed,
        stats.ultsEarned,
        stats.timePlayed,
        stats.dragonstrikeKills,
        stats.playersTeleported,
        stats.criticalHits,
        stats.shotsHit,
        stats.enemiesHacked,
        stats.enemiesEMPd,
        stats.stormArrowKills,
        stats.scopedHits,
        stats.scopedCriticalHits,
        stats.bobKills,
        stats.scopedCriticalHitKills,
        stats.chargedShotKills,
        stats.knockbackKills,
        stats.deadeyeKills,
        stats.overclockKills,
        teams.map { it.id },
        lastFetchedAt
    )
}

fun TeamSummary.toDbRow(): Team {
    return Team(
        id = id,
        code = code,
        name = name,
        logo = logo,
        icon = icon,
        primary_color = primaryColor,
        secondary_color = secondaryColor
    )
}
package com.keiferstone.data.extension

import com.keiferstone.data.db.Player
import com.keiferstone.data.db.Team
import com.keiferstone.data.model.PlayerDetail
import com.keiferstone.data.model.PlayerSummary
import com.keiferstone.data.model.TeamSummary


fun PlayerDetail.toDbRow(lastFetchedAt: Long = System.currentTimeMillis()): Player {
    return Player(
        id = id,
        name = name,
        number = number,
        role = role,
        preferred_slot = preferredSlot,
        current_teams = currentTeams,
        given_name = givenName,
        family_name = familyName,
        headshot_url = headshotUrl,
        damage_done = stats.heroDamageDone,
        healing_done = stats.healingDone,
        damage_taken = stats.damageTaken,
        final_blows = stats.finalBlows,
        eliminations = stats.eliminations,
        deaths = stats.deaths,
        time_spent_on_fire = stats.timeSpentOnFire,
        solo_kills = stats.soloKills,
        ults_used = stats.ultsUsed,
        ults_earned = stats.ultsEarned,
        time_played = stats.timePlayed,
        dragonstrike_kills = stats.dragonstrikeKills,
        players_teleported = stats.playersTeleported,
        critical_hits = stats.criticalHits,
        shots_hit = stats.shotsHit,
        enemies_hacked = stats.enemiesHacked,
        enemies_empd = stats.enemiesEMPd,
        storm_arrow_kills = stats.stormArrowKills,
        scoped_hits = stats.scopedHits,
        scoped_critical_hits = stats.scopedCriticalHits,
        bob_kills = stats.bobKills,
        scoped_critical_hit_kills = stats.scopedCriticalHitKills,
        charged_shot_kills = stats.chargedShotKills,
        knockback_kills = stats.knockbackKills,
        deadeye_kills = stats.deadeyeKills,
        overclock_kills = stats.overclockKills,
        team_ids = teams.map { it.id },
        last_fetched_at = lastFetchedAt
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
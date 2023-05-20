package com.keiferstone.data.extension

import com.keiferstone.data.db.Player
import com.keiferstone.data.model.PlayerDetail
import com.keiferstone.data.model.PlayerDetailStats
import com.keiferstone.data.model.PlayerDetailTeam
import com.keiferstone.data.model.PlayerSummary
import com.keiferstone.data.model.Ttls

fun Player.isStale(ttl: Long = Ttls.ONE_DAY): Boolean {
    return System.currentTimeMillis().let { currentTime ->
        currentTime - (last_fetched_at ?: currentTime) > ttl
    }
}

fun Player.hasStats(): Boolean {
    return damage_done != null
            || healing_done != null
            || damage_taken != null
            || final_blows != null
			|| eliminations != null
			|| deaths != null
			|| time_spent_on_fire != null
			|| solo_kills != null
			|| ults_used != null
			|| ults_earned != null
			|| time_played != null
			|| dragonstrike_kills != null
			|| players_teleported != null
			|| critical_hits != null
			|| shots_hit != null
			|| enemies_hacked != null
			|| enemies_empd != null
			|| storm_arrow_kills != null
			|| scoped_hits != null
			|| scoped_critical_hits != null
			|| bob_kills != null
			|| scoped_critical_hit_kills != null
			|| charged_shot_kills != null
			|| knockback_kills != null
			|| deadeye_kills != null
			|| overclock_kills != null
}

fun Player.toPlayerSummary(): PlayerSummary {
    return PlayerSummary(
        id = id,
        name = name,
        number = number,
        role = role,
        preferredSlot = preferred_slot,
        currentTeam = current_teams?.firstOrNull(),
        givenName = given_name,
        familyName = family_name,
        headshotUrl = headshot_url
    )
}

fun Player.toPlayerDetail(loadTeams: (List<Long>) -> List<PlayerDetailTeam>): PlayerDetail {
    return PlayerDetail(
        id = id,
        name = name,
        number = number,
        role = role,
        preferredSlot = preferred_slot,
        currentTeams = current_teams ?: emptyList(),
        givenName = given_name,
        familyName = family_name,
        headshotUrl = headshot_url,
        teams = loadTeams(team_ids ?: emptyList()),
        stats = PlayerDetailStats(
            heroDamageDone =  damage_done,
            healingDone = healing_done,
            damageTaken = damage_taken,
            finalBlows = final_blows,
            eliminations = eliminations,
            deaths = deaths,
            timeSpentOnFire = time_spent_on_fire,
            soloKills = solo_kills,
            ultsUsed = ults_used,
            ultsEarned = ults_earned,
            timePlayed = time_played,
            dragonstrikeKills = dragonstrike_kills,
            playersTeleported = players_teleported,
            criticalHits = critical_hits,
            shotsHit = shots_hit,
            enemiesHacked = enemies_hacked,
            enemiesEMPd = enemies_empd,
            stormArrowKills = storm_arrow_kills,
            scopedHits = scoped_hits,
            scopedCriticalHits = scoped_critical_hits,
            bobKills = bob_kills,
            scopedCriticalHitKills = scoped_critical_hit_kills,
            chargedShotKills = charged_shot_kills,
            knockbackKills = knockback_kills,
            deadeyeKills = deadeye_kills,
            overclockKills = overclock_kills
        )
    )
}
package com.keiferstone.data.extension

import com.keiferstone.data.db.Player
import com.keiferstone.data.model.PlayerDetail
import com.keiferstone.data.model.PlayerDetailStats
import com.keiferstone.data.model.PlayerDetailTeam
import com.keiferstone.data.model.Ttls

fun Player.isStale(ttl: Long = Ttls.ONE_DAY): Boolean {
    return System.currentTimeMillis().let { currentTime ->
        (last_fetched_at ?: currentTime) + ttl < currentTime
    }
}

fun Player.toPlayerDetail(loadTeams: (List<Long>) -> List<PlayerDetailTeam>): PlayerDetail {
    return PlayerDetail(
        id = id,
        name = name,
        number = number,
        role = role,
        preferredSlot = preferred_slot,
        currentTeams = current_teams,
        givenName = given_name,
        familyName = family_name,
        headshotUrl = headshot_url,
        loadTeams(team_ids),
        PlayerDetailStats(
            damage_done,
            healing_done,
            damage_taken,
            final_blows,
            eliminations,
            deaths,
            time_spent_on_fire,
            solo_kills,
            ults_used,
            ults_earned,
            time_played,
            dragonstrike_kills,
            players_teleported,
            critical_hits,
            shots_hit,
            enemies_hacked,
            enemies_empd,
            storm_arrow_kills,
            scoped_hits,
            scoped_critical_hits,
            bob_kills,
            scoped_critical_hit_kills,
            charged_shot_kills,
            knockback_kills,
            deadeye_kills,
            overclock_kills
        )
    )
}
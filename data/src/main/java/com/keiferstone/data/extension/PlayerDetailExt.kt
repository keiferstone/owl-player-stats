package com.keiferstone.data.extension

import com.keiferstone.data.model.PlayerDetail

fun PlayerDetail.hasDetails(): Boolean {
    return currentTeam != null || hasStats()
}

fun PlayerDetail.hasStats(): Boolean {
    return stats.heroDamageDone != null
            || stats.healingDone != null
            || stats.damageTaken != null
            || stats.finalBlows != null
            || stats.eliminations != null
            || stats.deaths != null
            || stats.timeSpentOnFire != null
            || stats.soloKills != null
            || stats.ultsUsed != null
            || stats.ultsEarned != null
            || stats.timePlayed != null
            || stats.dragonstrikeKills != null
            || stats.playersTeleported != null
            || stats.criticalHits != null
            || stats.shotsHit != null
            || stats.enemiesHacked != null
            || stats.enemiesEMPd != null
            || stats.stormArrowKills != null
            || stats.scopedHits != null
            || stats.scopedCriticalHits != null
            || stats.bobKills != null
            || stats.scopedCriticalHitKills != null
            || stats.chargedShotKills != null
            || stats.knockbackKills != null
            || stats.deadeyeKills != null
            || stats.overclockKills != null
}
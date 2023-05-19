package com.keiferstone.owlplayerstats.extension


import com.keiferstone.data.model.PlayerDetailStats
import com.keiferstone.data.model.StatType
import kotlin.math.roundToLong


fun PlayerDetailStats.extractValue(
    statType: StatType,
    perTenMinutes: Boolean = true): Long? {
    return when (statType) {
        StatType.DAMAGE_DONE -> heroDamageDone
        StatType.HEALING_DONE -> healingDone
        StatType.DAMAGE_TAKEN -> damageTaken
        StatType.FINAL_BLOWS -> finalBlows
        StatType.ELIMINATIONS -> eliminations
        StatType.DEATHS -> deaths
        StatType.TIME_SPENT_ON_FIRE -> timeSpentOnFire
        StatType.SOLO_KILLS -> soloKills
        StatType.ULTS_USED -> ultsUsed
        StatType.ULTS_EARNED -> ultsEarned
        StatType.TIME_PLAYED -> timePlayed
        StatType.DRAGONSTRIKE_KILLS -> dragonstrikeKills
        StatType.PLAYERS_TELEPORTED -> playersTeleported
        StatType.CRITICAL_HITS -> criticalHits
        StatType.SHOTS_HIT -> shotsHit
        StatType.ENEMIES_HACKED -> enemiesHacked
        StatType.ENEMIES_EMPD -> enemiesEMPd
        StatType.STORM_ARROW_KILLS -> stormArrowKills
        StatType.SCOPED_HITS -> scopedHits
        StatType.SCOPED_CRITICAL_HITS -> scopedCriticalHits
        StatType.BOB_KILLS -> bobKills
        StatType.SCOPED_CRITICAL_HIT_KILLS -> scopedCriticalHitKills
        StatType.CHARGED_SHOT_KILLS -> chargedShotKills
        StatType.KNOCKBACK_KILLS -> knockbackKills
        StatType.DEADEYE_KILLS -> deadeyeKills
        StatType.OVERCLOCK_KILLS -> overclockKills
    }?.let { value ->
        timePlayed?.let {
            if (perTenMinutes) (value.toFloat() / it.toFloat() * 600f).roundToLong()
            else value
        }?: value
    }
}
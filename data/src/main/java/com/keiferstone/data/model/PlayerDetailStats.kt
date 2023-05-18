package com.keiferstone.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlayerDetailStats(
    val heroDamageDone: Long?,
    val healingDone: Long?,
    val damageTaken: Long?,
    val finalBlows: Long?,
    val eliminations: Long?,
    val deaths: Long?,
    val timeSpentOnFire: Long?,
    val soloKills: Long?,
    val ultsUsed: Long?,
    val ultsEarned: Long?,
    val timePlayed: Long?,
    val dragonstrikeKills: Long?,
    val playersTeleported: Long?,
    val criticalHits: Long?,
    val shotsHit: Long?,
    val enemiesHacked: Long?,
    val enemiesEMPd: Long?,
    val stormArrowKills: Long?,
    val scopedHits: Long?,
    val scopedCriticalHits: Long?,
    val bobKills: Long?,
    val scopedCriticalHitKills: Long?,
    val chargedShotKills: Long?,
    val knockbackKills: Long?,
    val deadeyeKills: Long?,
    val overclockKills: Long?,
)
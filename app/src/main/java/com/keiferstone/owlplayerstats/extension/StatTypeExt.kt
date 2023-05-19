package com.keiferstone.owlplayerstats.extension

import androidx.annotation.StringRes
import com.keiferstone.data.model.StatType
import com.keiferstone.owlplayerstats.R

@StringRes
fun StatType.nameResId() = when (this) {
    StatType.DAMAGE_DONE -> R.string.damage_done
    StatType.HEALING_DONE -> R.string.healing_done
    StatType.DAMAGE_TAKEN -> R.string.damage_taken
    StatType.FINAL_BLOWS -> R.string.final_blows
    StatType.ELIMINATIONS -> R.string.eliminations
    StatType.DEATHS -> R.string.deaths
    StatType.TIME_SPENT_ON_FIRE -> R.string.time_spent_on_fire
    StatType.SOLO_KILLS -> R.string.solo_kills
    StatType.ULTS_USED -> R.string.ults_used
    StatType.ULTS_EARNED -> R.string.ults_earned
    StatType.TIME_PLAYED -> R.string.time_played
    StatType.DRAGONSTRIKE_KILLS -> R.string.dragonstrike_kills
    StatType.PLAYERS_TELEPORTED -> R.string.players_teleported
    StatType.CRITICAL_HITS -> R.string.critical_hits
    StatType.SHOTS_HIT -> R.string.shots_hit
    StatType.ENEMIES_HACKED -> R.string.enemies_hacked
    StatType.ENEMIES_EMPD -> R.string.enemies_empd
    StatType.STORM_ARROW_KILLS -> R.string.storm_arrow_kills
    StatType.SCOPED_HITS -> R.string.scoped_hits
    StatType.SCOPED_CRITICAL_HITS -> R.string.scoped_critical_hits
    StatType.BOB_KILLS -> R.string.bob_kills
    StatType.SCOPED_CRITICAL_HIT_KILLS -> R.string.scoped_critical_hit_kills
    StatType.CHARGED_SHOT_KILLS -> R.string.charged_shot_kills
    StatType.KNOCKBACK_KILLS -> R.string.knockback_kills
    StatType.DEADEYE_KILLS -> R.string.deadeye_kills
    StatType.OVERCLOCK_KILLS -> R.string.overclock_kills
}
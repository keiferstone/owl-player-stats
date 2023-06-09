package com.keiferstone.data.di

import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.keiferstone.data.adapter.listOfLongsAdapter
import com.keiferstone.data.db.Player
import com.keiferstone.data.db.Summary_ids
import com.keiferstone.data.model.Summary
import com.keiferstone.owlplayerstats.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): Database {
        return Database(
            driver = AndroidSqliteDriver(Database.Schema, context, "owl-player-stats.db"),
            summary_idsAdapter = Summary_ids.Adapter(
                player_idsAdapter = listOfLongsAdapter,
                team_idsAdapter = listOfLongsAdapter
            ),
            playerAdapter = Player.Adapter(
                current_teamsAdapter = listOfLongsAdapter,
                team_idsAdapter = listOfLongsAdapter,
            ))
    }
}
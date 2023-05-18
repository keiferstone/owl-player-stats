package com.keiferstone.owlplayerstats.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.keiferstone.owlplayerstats.screen.PlayerGridScreen
import com.keiferstone.owlplayerstats.theme.OwlPlayerStatsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OwlPlayerStatsTheme {
                PlayerGridScreen()
            }
        }
    }
}
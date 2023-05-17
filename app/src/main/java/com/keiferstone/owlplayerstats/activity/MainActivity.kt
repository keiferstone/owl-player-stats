package com.keiferstone.owlplayerstats.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.keiferstone.owlplayerstats.ui.screen.PlayerGridScreen
import com.keiferstone.owlplayerstats.ui.theme.OwlPlayerStatsTheme
import com.keiferstone.owlplayerstats.vm.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OwlPlayerStatsTheme {
                PlayerGridScreen()
            }
        }

        viewModel.getSummary()
    }
}
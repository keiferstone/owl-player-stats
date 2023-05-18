package com.keiferstone.owlplayerstats.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.keiferstone.owlplayerstats.screen.PlayerDetailScreen
import com.keiferstone.owlplayerstats.screen.PlayerGridScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            MaterialTheme {
                NavHost(
                    navController = navController,
                    startDestination = "player-grid") {
                    composable("player-grid") {
                        PlayerGridScreen {
                            navController.navigate("player-detail/${it.id}")
                        }
                    }
                    composable(
                        route = "player-detail/{playerId}",
                        arguments = listOf(navArgument("playerId") { type = NavType.LongType })) { backStackEntry ->
                        backStackEntry.arguments?.getLong("playerId")?.let {
                            PlayerDetailScreen(it)
                        }
                    }
                }
            }
        }
    }
}
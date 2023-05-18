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
import com.keiferstone.owlplayerstats.screen.PlayerComparisonScreen
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
                        PlayerGridScreen(
                            onPlayerSelected =  { player ->
                                navController.navigate("player-detail/${player.id}")
                            },
                            onPlayerPairSelected = { first, second ->
                                navController.navigate("player-comparison/${first.id}/${second.id}")
                            }
                        )
                    }
                    composable(
                        route = "player-detail/{playerId}",
                        arguments = listOf(navArgument("playerId") { type = NavType.LongType })) { backStackEntry ->
                        backStackEntry.arguments?.getLong("playerId")?.let {
                            PlayerDetailScreen(it)
                        }
                    }
                    composable(
                        route = "player-comparison/{player1Id}/{player2Id}",
                        arguments = listOf(
                            navArgument("player1Id") { type = NavType.LongType },
                            navArgument("player2Id") { type = NavType.LongType })) { backStackEntry ->
                        val player1Id = backStackEntry.arguments?.getLong("player1Id")
                        val player2Id = backStackEntry.arguments?.getLong("player2Id")
                        if (player1Id != null && player2Id != null) {
                            PlayerComparisonScreen(player1Id, player2Id)
                        }
                    }
                }
            }
        }
    }
}